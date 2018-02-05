package com.gipeshka.model.condition

import scala.concurrent.{Await, ExecutionContext}

import akka.http.scaladsl.unmarshalling.Unmarshal
import akka.http.scaladsl.model.{FormData, HttpRequest}
import akka.http.scaladsl.unmarshalling.PredefinedFromEntityUnmarshallers
import scala.concurrent.duration._
import scala.util.control.NonFatal

import akka.stream.Materializer

class BodyUrlEncodedCondition(
  expected: Map[String, String]
)(implicit
  ec: ExecutionContext,
  m: Materializer
) extends RequestCondition
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
