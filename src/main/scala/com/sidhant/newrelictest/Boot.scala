package com.sidhant.newrelictest

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer

object Boot extends App {
  implicit val system = ActorSystem("transaction-api")
  implicit val materializer = ActorMaterializer()
  Http().bindAndHandle((new Router).routes, "0.0.0.0", 9998)
  println("Starting HTTP Server at port 9998")
}
