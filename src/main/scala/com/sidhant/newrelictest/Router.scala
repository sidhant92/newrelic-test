package com.sidhant.newrelictest

import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import scala.util.{Failure, Success}

import akka.actor.Props

import com.sidhant.newrelictest.Messages.TestMessage
import com.sidhant.newrelictest.controllers.TestController

import akka.http.scaladsl.server.directives.CachingDirectives

import akka.http.scaladsl.server.RequestContext
import akka.http.caching.scaladsl.Cache
import akka.http.caching.scaladsl.CachingSettings
import akka.http.caching.LfuCache
import akka.http.scaladsl.model.Uri
import akka.http.scaladsl.server.RouteResult

class Router extends ImperativeComplete with CachingDirectives {
  implicit val timeout = Timeout(5.seconds)

  val routes = {
    path("v1" / "test") {
      pathEndOrSingleSlash {
        get {
          alwaysCache(HttpCache.lfuCache, HttpCache.keyerFunction) {
            imperativelyComplete { ctx =>
              Boot.system.actorOf(Props(new TestController(ctx))) ! TestMessage
            }
          }
        }
      }
    }
  }
}

object HttpCache {
  val keyerFunction: PartialFunction[RequestContext, Uri] = {
    case r: RequestContext ⇒ r.request.uri
  }
  val defaultCachingSettings = CachingSettings(Boot.system)
  val lfuCacheSettings = defaultCachingSettings.lfuCacheSettings
    .withInitialCapacity(10000)
    .withMaxCapacity(100000)
    .withTimeToLive(600.seconds)
    .withTimeToIdle(480.seconds)
  val cachingSettings = defaultCachingSettings.withLfuCacheSettings(lfuCacheSettings)
  val lfuCache: Cache[Uri, RouteResult] = LfuCache(cachingSettings)
}
