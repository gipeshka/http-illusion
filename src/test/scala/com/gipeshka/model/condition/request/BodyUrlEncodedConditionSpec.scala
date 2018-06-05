package com.gipeshka.model.condition.request

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.model._
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.gipeshka.model.condition.{GenericCondition, MapMatchingConditionSpec}

class BodyUrlEncodedConditionSpec
  extends MapMatchingConditionSpec[HttpRequest]
{
  protected def spawnCondition(data: Map[String, String]): GenericCondition[HttpRequest] = {
    val ec = ExecutionContext.global
    implicit val system = ActorSystem("BodyUrlEncodedConditionSpec")
    val materializer = ActorMaterializer()

    new BodyUrlEncodedCondition(data)(ec, materializer)
  }

  protected def spawnRequest(withData: Option[Map[String, String]]): HttpRequest = {
    HttpRequest(
      entity = HttpEntity.Strict(
        MediaTypes.`application/x-www-form-urlencoded`.toContentType(HttpCharsets.`UTF-8`),
        ByteString(Uri.Query.apply(withData.getOrElse(Map())).toString)
      )
    )
  }
}
