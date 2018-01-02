package com.gipeshka.handler
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.gipeshka.utils.Config

@Singleton
class FallbackProxyHandler @Inject()(
  config: Config
)(implicit
  actorSystem: ActorSystem,
  executionContext: ExecutionContext
) extends HandlerClient
{
  private val fallbackConnection = config.app.fallback

  def canHandle(httpRequest: HttpRequest): Future[Boolean] = Future.successful(true)

  def handle(httpRequest: HttpRequest): Future[HttpResponse] = {
    val request = changeRequestDesitnation(httpRequest)

    Http().singleRequest(
      changeRequestDesitnation(httpRequest)
    ).map { response =>
      val a = 1

      response
    }
  }

  private def changeRequestDesitnation(httpRequest: HttpRequest): HttpRequest = {
    httpRequest.withUri(
      httpRequest.uri
        .withAuthority(fallbackConnection.host, fallbackConnection.port)
        .withScheme(fallbackConnection.scheme)
    )
  }
}
