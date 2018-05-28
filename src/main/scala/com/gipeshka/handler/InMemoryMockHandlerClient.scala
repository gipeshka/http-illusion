package com.gipeshka.handler

import javax.inject.{Inject, Singleton}

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}

import akka.Done
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.stream.Materializer
import com.gipeshka.exception.MockerNoReactionFoundException
import com.gipeshka.model.condition.ConditionHelper._
import com.gipeshka.model.condition._
import com.gipeshka.request._
import spray.json._

@Singleton
class InMemoryMockHandlerClient @Inject()(
  requestConditionFactory: ConditionFactory[HttpRequest],
  searchConditionFactory: ConditionFactory[ReactionRequestPart]
)(implicit
  val executionContext: ExecutionContext,
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
      buildCondition(addReactionRequest.request, requestConditionFactory).apply,
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

  def search(searchReactionRequest: SearchReactionRequest): Future[Map[ReactionRequestPart, Reaction]] = {
    val condition = buildCondition(searchReactionRequest.request, searchConditionFactory)

    Future.successful(reactions.filterKeys(condition.apply).toMap)
  }

  def state: Future[Map[ReactionRequestPart, Reaction]] = {
    Future.successful(reactions.toMap)
  }

  private def buildCondition[T](
    request: ReactionRequestPart,
    conditionFactory: ConditionFactory[T]
  ): GenericCondition[T] = {
    val pathCondition = request.path.map(path => conditionFactory.path(path))
    val methodCondition = request.method.map(method => conditionFactory.method(method))
    val queryCondition = request.query.map(query => conditionFactory.query(query))
    val headerCondition = request.headers.map(headers => conditionFactory.header(headers))
    val bodyJsonCondition = request.bodyJson.map(body => conditionFactory.bodyJson(body))
    val bodyUrlEncoded = request.bodyUrlEncoded.map(urlEncoded => conditionFactory.bodyUrl(urlEncoded))

    pathCondition
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
