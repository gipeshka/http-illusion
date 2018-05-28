package com.gipeshka.handler

import scala.concurrent.{ExecutionContext, Future}

import akka.Done
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import com.google.inject.ImplementedBy
import com.gipeshka.handler.MockReactionManager.HttpRequestCondition
import com.gipeshka.request.{AddReactionRequest, DeleteReactionRequest, ReactionRequestPart, SearchReactionRequest}
import spray.json.JsValue

case class Reaction(condition: HttpRequestCondition, response: HttpResponse, description: JsValue)

@ImplementedBy(classOf[InMemoryMockHandlerClient])
trait MockReactionManager
{
  protected implicit val executionContext: ExecutionContext

  def add(addReactionRequest: AddReactionRequest): Future[Done]

  def delete(deleteReactionRequest: DeleteReactionRequest): Future[Done]

  def search(searchReactionRequest: SearchReactionRequest): Future[Map[ReactionRequestPart, Reaction]]

  def state: Future[Map[ReactionRequestPart, Reaction]]

  def searchAndDelete(searchReactionRequest: SearchReactionRequest): Future[Done] = {
    val toDelete = search(searchReactionRequest)

    toDelete.flatMap { reactionList =>
      Future.sequence(
        reactionList.map { case (reactionRequest, reaction) =>
          delete(
            DeleteReactionRequest(
              request = reactionRequest
            )
          )
        }
      ).map { _ =>
        Done
      }
    }
  }
}

object MockReactionManager
{
  type HttpRequestCondition = HttpRequest => Boolean
}
