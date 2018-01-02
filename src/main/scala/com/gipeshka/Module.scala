package com.gipeshka

import java.time.Clock

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.stream.{ActorMaterializer, Materializer}
import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.gipeshka.handler.{CouchbaseReactionManagerDecorator, InMemoryMockHandlerClient, MockReactionManager}
import com.gipeshka.utils.{Config, CouchbaseConfig}

class Module extends AbstractModule
{
  def configure(): Unit = {
    implicit val actorSystem = ActorSystem()

    // general bindings
    bind(classOf[Config])
      .toInstance(Config.load)
    bind(classOf[ActorSystem])
      .toInstance(actorSystem)
    bind(classOf[ExecutionContext])
      .toInstance(actorSystem.dispatcher)
    bind(classOf[Materializer])
      .toInstance(ActorMaterializer())
    bind(classOf[LoggingAdapter])
      .toInstance(Logging(actorSystem, getClass))
    bind(classOf[MockReactionManager])
      .annotatedWith(Names.named("Default"))
      .to(classOf[InMemoryMockHandlerClient])

    // connector specific bindings
    bind(classOf[CouchbaseConfig])
      .toInstance(CouchbaseConfig.load)
    bind(classOf[MockReactionManager])
      .annotatedWith(Names.named("Actual"))
      .to(classOf[CouchbaseReactionManagerDecorator])
    bind(classOf[Clock])
      .toInstance(Clock.systemDefaultZone)
  }
}
