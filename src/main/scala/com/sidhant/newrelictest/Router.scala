package com.sidhant.newrelictest

import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

class Router extends ImperativeComplete {
  implicit val timeout = Timeout(5.seconds)

  val routes = {
    path("v1" / "test") {
      pathEndOrSingleSlash {
        get {
          imperativelyComplete { ctx =>
            ctx.complete("done")
          }
        }
      }
    }
  }
}
