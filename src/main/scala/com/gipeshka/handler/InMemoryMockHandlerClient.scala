package com.gipeshka.handler

import javax.inject.{Inject, Singleton}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

import akka.Done
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import com.gipeshka.exception.MockerNoReactionFoundException
import com.gipeshka.model.condition._
import com.gipeshka.request._
import spray.json._

@Singleton
class InMemoryMockHandlerClient @Inject()(implicit
  executionContext: ExecutionContext,
  materializer: Materializer
) extends HandlerClient with MockReactionManager with AddReactionRequestFormat
{
  type HttpAction = HttpRequest => Future[HttpResponse]

  private var reactions: mutable.LinkedHashMap[ReactionRequestPart, Reaction] = mutable.LinkedHashMap()

  def canHandle(httpRequest: HttpRequest): Future[Boolean] = {
    Future.successful(reactions.exists(_._2.condition(httpRequest)))
  }

  def handle(httpRequest: HttpRequest): Future[HttpResponse] = {
    val reactionsIterator = reactions.values

    reactionsIterator.find(
      _.condition(httpRequest)
    ).map(_.response) match {
      case Some(httpResponse) => Future.successful(httpResponse)
      case _ => Future.failed(MockerNoReactionFoundException(httpRequest))
    }
  }

  def add(addReactionRequest: AddReactionRequest): Future[Done] = this.synchronized {
    val reaction = Reaction(
      buildCondition(addReactionRequest.request).apply,
      buildResponse(addReactionRequest.response),
      addReactionRequest.toJson
    )

    reactions = mutable.LinkedHashMap(
      (addReactionRequest.request, reaction) :: reactions.toList.filter(_._1 != addReactionRequest.request):_*
    )

    Future.successful(Done)
  }

  def delete(deleteReactionRequest: DeleteReactionRequest): Future[Done] = this.synchronized {
    reactions.remove(deleteReactionRequest.request)

    Future.successful(Done)
  }

  def state: Future[List[JsValue]] = Future.successful(reactions.map(_._2.description).toList)

  private def buildCondition(request: ReactionRequestPart): RequestCondition = {
    val pathCondition = request.path.map(path => new PathCondition(path))
    val methodCondition = request.method.map(method => new MethodCondition(method))
    val queryCondition = request.query.map(query => new QueryCondition(query))
    val headerCondition = request.headers.map(headers => new HeaderCondition(headers))
    val bodyJsonCondition = request.bodyJson.map(json => new BodyJsonCondition(json))
    val bodyUrlEncoded = request.bodyUrlEncoded.map(urlEncoded => new BodyUrlEncodedCondition(urlEncoded))

    new ChainCondition()
      .and(pathCondition)
      .and(methodCondition)
      .and(queryCondition)
      .and(headerCondition)
      .and(bodyJsonCondition)
      .and(bodyUrlEncoded)
  }

  private def buildResponse(response: ReactionResponsePart): HttpResponse = {
    HttpResponse(
      entity = response.body.toString,
      status = response.status
    )
  }
}
