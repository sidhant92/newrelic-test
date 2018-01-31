package com.sidhant.newrelictest.services

import akka.actor.{Actor, OneForOneStrategy, Props, SupervisorStrategy}
import akka.routing.BalancingPool
import com.sidhant.newrelictest.Boot

import com.sidhant.newrelictest.Messages.TestMessage

import scala.util.control.NonFatal


class TestService extends Actor {
	override def receive = {
		case TestMessage => {
			sender ! "Service Returned"
		}
	}
}

object TestServiceConfig {
	val supervisorStrategy = OneForOneStrategy() {
    	case NonFatal(ex) => {
      		SupervisorStrategy.resume
    	}
    	case _ => {
      		SupervisorStrategy.restart
    	}
  	}
  	val testServiceProps = Props[TestService]
  	val testServiceActorRef = Boot.system.actorOf(BalancingPool(5).withSupervisorStrategy(supervisorStrategy).props(testServiceProps), "test-service")
}
