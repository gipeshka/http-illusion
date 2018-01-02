package com.gipeshka.handler

import javax.inject.{Inject, Named}

import scala.concurrent.{ExecutionContext, Future}
import scala.util.control.NonFatal

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.Materializer
import com.gipeshka.request.{AddReactionRequest, AddReactionRequestFormat, DeleteReactionRequest, DeleteReactionRequestFormat}


class SetupHandler @Inject()(
  @Named("Actual") mockReactionManager: MockReactionManager
)(implicit
  executionContext: ExecutionContext,
  system: ActorSystem,
  materializer: Materializer
) extends HandlerClient with AddReactionRequestFormat with DeleteReactionRequestFormat
{
  private val handler: HttpRequest => Future[HttpResponse] = {
    val route = {
      path("setup") {
        post {
          entity(as[AddReactionRequest]) { request =>
            complete {
              mockReactionManager.add(request).map { _ =>
                HttpResponse(entity = "Added new reaction")
              }
            }
          }
        } ~ get {
          complete {
            mockReactionManager.state
          }
        } ~ delete {
          entity(as[DeleteReactionRequest]) { request =>
            complete {
              mockReactionManager.delete(request).map { _ =>
                HttpResponse(entity = "Reaction removed")
              }
            }
          }
        }
      }
    }

    Route.asyncHandler(route)
  }

  def canHandle(httpRequest: HttpRequest): Future[Boolean] = {
    val result = try {
      httpRequest.uri.path.tail.head == "setup"
    } catch {
      case NonFatal(e) => false
    }

    Future.successful(result)
  }

  def handle(httpRequest: HttpRequest): Future[HttpResponse] = {
    handler(httpRequest)
  }
}
