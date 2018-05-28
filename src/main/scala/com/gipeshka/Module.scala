package com.gipeshka

import java.time.Clock
import javax.inject.Singleton

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.model.HttpRequest
import akka.stream.{ActorMaterializer, Materializer}
import com.gipeshka.handler.{CouchbaseReactionManagerDecorator, InMemoryMockHandlerClient, MockReactionManager}
import com.gipeshka.model.condition.ConditionFactory
import com.gipeshka.model.condition.request.RequestConditionFactory
import com.gipeshka.model.condition.search.SearchConditionFactory
import com.gipeshka.request.ReactionRequestPart
import com.gipeshka.utils.{Config, CouchbaseConfig}
import com.google.inject.name.Names
import com.google.inject.{AbstractModule, TypeLiteral}

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

    bind(new TypeLiteral[ConditionFactory[HttpRequest]] {})
      .to(classOf[RequestConditionFactory])
      .in(classOf[Singleton])

    bind(new TypeLiteral[ConditionFactory[ReactionRequestPart]] {})
      .to(classOf[SearchConditionFactory])
      .in(classOf[Singleton])

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
