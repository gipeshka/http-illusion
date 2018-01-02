package com.gipeshka.model.condition

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext}

import akka.http.scaladsl.model.HttpRequest
import akka.stream.Materializer
import gnieh.diffson.sprayJson._
import spray.json._

class BodyJsonCondition(json: JsValue)(implicit ec: ExecutionContext, m: Materializer) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    try {
      val body = Await.result(request.entity.toStrict(1 second).map(_.data.utf8String), 1 second)

      val requestJson = body.parseJson

      JsonDiff.diff(json, requestJson, false).ops.forall(_.isInstanceOf[Add])
    } catch {
      case e: JsonParser.ParsingException => false
    }
  }
}
