package com.gipeshka.handler

import scala.concurrent.Future

import akka.http.scaladsl.model._
import com.google.inject.ImplementedBy

@ImplementedBy(classOf[DefaultHandlerChain])
trait HandlerClient
{
  def canHandle(httpRequest: HttpRequest): Future[Boolean]

  def handle(httpRequest: HttpRequest): Future[HttpResponse]
}
