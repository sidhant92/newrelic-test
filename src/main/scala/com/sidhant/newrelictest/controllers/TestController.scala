package com.sidhant.newrelictest.controllers

import com.sidhant.newrelictest.ImperativeRequestContext
import com.sidhant.newrelictest.Messages.TestMessage

import com.sidhant.newrelictest.services.TestServiceConfig

import akka.actor.Actor
import akka.pattern.{ AskTimeoutException, ask }
import akka.util.Timeout
import scala.concurrent.duration.DurationInt

import scala.util.{ Failure, Success }

import scala.concurrent.ExecutionContext.Implicits.global

class TestController(ctx: ImperativeRequestContext) extends Actor {
	implicit val timeout = Timeout(5.seconds)

	override def receive = {
		case TestMessage => {
			val results = TestServiceConfig.testServiceActorRef ? TestMessage
			results.map {
				case res: String => {
					ctx.complete(res)
					context.stop(self)
				}
			}
		}
	}
}
