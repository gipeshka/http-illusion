package com.gipeshka.handler

import scala.concurrent.Future

import akka.Done
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import com.google.inject.ImplementedBy
import com.gipeshka.handler.MockReactionManager.HttpRequestCondition
import com.gipeshka.request.{AddReactionRequest, DeleteReactionRequest}
import spray.json.JsValue

case class Reaction(condition: HttpRequestCondition, response: HttpResponse, description: JsValue)

@ImplementedBy(classOf[InMemoryMockHandlerClient])
trait MockReactionManager
{
  def add(addReactionRequest: AddReactionRequest): Future[Done]

  def delete(deleteReactionRequest: DeleteReactionRequest): Future[Done]

  def state: Future[List[JsValue]]
}

object MockReactionManager
{
  type HttpRequestCondition = HttpRequest => Boolean
}
