package com.gipeshka.handler
import javax.inject.{Inject, Singleton}

import scala.concurrent.{ExecutionContext, Future}

import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.gipeshka.exception.CannotHandleRequestException

class HandlerChain(
  handlers: Seq[HandlerClient],
  logger: LoggingAdapter
)(implicit
  executionContext: ExecutionContext
) extends HandlerClient
{
  def canHandle(httpRequest: HttpRequest): Future[Boolean] = {
    Future.sequence(
      handlers.map(_.canHandle(httpRequest))
    ).map {
      _.exists(p => p)
    }
  }

  def handle(httpRequest: HttpRequest): Future[HttpResponse] = {
    handlers.foldLeft(
      Future.failed[HandlerClient](new CannotHandleRequestException("chain", httpRequest))
    ) { case (acc, handler) =>
      acc.recoverWith {
        case e: CannotHandleRequestException =>
          handler.canHandle(httpRequest).flatMap { canHandle =>
            if (canHandle) {
              logger.info(s"${handler.getClass.getCanonicalName} is going to handle request " +
                s"to ${httpRequest.uri.path.toString}")
              Future.successful(handler)
            } else {
              Future.failed(e)
            }
          }
      }
    }.flatMap {
      _.handle(httpRequest)
    }
  }
}

@Singleton
class DefaultHandlerChain @Inject()(
  setupHandler: SetupHandler,
  mockHandler: InMemoryMockHandlerClient,
  externalProxyHandler: FallbackProxyHandler,
  logger: LoggingAdapter
)(implicit
  executionContext: ExecutionContext
) extends HandlerChain(Seq(setupHandler, mockHandler, externalProxyHandler), logger)(executionContext)
