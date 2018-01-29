package com.sidhant.newrelictest

import akka.http.scaladsl.marshalling.ToResponseMarshallable
import akka.http.scaladsl.server.{ RequestContext, Route, RouteResult }

import scala.concurrent.Promise

trait ImperativeComplete {
  // a custom directive
  def imperativelyComplete(inner: ImperativeRequestContext => Unit): Route = { ctx: RequestContext =>
    val p = Promise[RouteResult]()
    inner(new ImperativeRequestContext(ctx, p))
    p.future
  }
}

// an imperative wrapper for request context
final class ImperativeRequestContext(ctx: RequestContext, promise: Promise[RouteResult]) {
  val httpRequest = ctx.request
  private implicit val ec = ctx.executionContext
  def complete(obj: ToResponseMarshallable): Unit = ctx.complete(obj).onComplete(promise.complete)
  def fail(error: Throwable): Unit = ctx.fail(error).onComplete(promise.complete)
}
