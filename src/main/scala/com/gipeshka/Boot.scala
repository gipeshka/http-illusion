package com.gipeshka

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import akka.stream.Materializer
import akka.stream.scaladsl.Flow
import com.google.inject.Guice
import com.gipeshka.handler.HandlerClient
import com.gipeshka.utils.Config
import net.codingwell.scalaguice.InjectorExtensions._

object Boot extends App
{
  def startApplication = {
    val injector = Guice.createInjector(new Module)
    val config = injector.instance[Config]

    val handler = injector.instance[HandlerClient]

    Http()(injector.instance[ActorSystem]).bindAndHandle(
      Flow[HttpRequest].mapAsync(config.app.handlerParallelism) { handler.handle },
      config.http.host,
      config.http.port
    )(injector.instance[Materializer])
  }

  startApplication
}
