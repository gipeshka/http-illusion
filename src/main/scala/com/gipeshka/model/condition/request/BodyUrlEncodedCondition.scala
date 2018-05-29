package com.gipeshka.model.condition.request

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}
import scala.util.control.NonFatal

import akka.http.scaladsl.model.{FormData, HttpRequest}
import akka.http.scaladsl.unmarshalling.{PredefinedFromEntityUnmarshallers, Unmarshal}
import akka.stream.Materializer
import com.gipeshka.model.condition.GenericCondition

class BodyUrlEncodedCondition(
  expected: Map[String, String]
)(implicit
  ec: ExecutionContext,
  m: Materializer
) extends GenericCondition[HttpRequest]
{
  implicit val unmarshaller = PredefinedFromEntityUnmarshallers.defaultUrlEncodedFormDataUnmarshaller

  def apply(request: HttpRequest): Boolean = {
    try {
      val formData = Await.result(
        Unmarshal(request.entity).to[FormData],
        1 second
      )

      val entityMap = formData.fields.toMap

      (expected.toSet diff entityMap.toSet).isEmpty
    } catch {
      case NonFatal(_) => false
    }
  }
}
