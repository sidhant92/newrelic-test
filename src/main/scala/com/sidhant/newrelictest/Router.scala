package com.sidhant.newrelictest

import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success}

import akka.actor.Props

import com.sidhant.newrelictest.Messages.TestMessage
import com.sidhant.newrelictest.controllers.TestController

class Router extends ImperativeComplete {
  implicit val timeout = Timeout(5.seconds)

  val routes = {
    path("v1" / "test") {
      pathEndOrSingleSlash {
        get {
          imperativelyComplete { ctx =>
            Boot.system.actorOf(Props(new TestController(ctx))) ! TestMessage
          }
        }
      }
    }
  }
}
