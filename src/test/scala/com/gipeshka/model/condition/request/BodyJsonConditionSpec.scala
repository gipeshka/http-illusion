package com.gipeshka.model.condition.request

import scala.concurrent.ExecutionContext

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest}
import akka.stream.ActorMaterializer
import akka.util.ByteString
import com.gipeshka.model.condition.{GenericCondition, JsonConditionSpec}
import spray.json._

class BodyJsonConditionSpec extends JsonConditionSpec[HttpRequest]
{
  protected def spawnCondition(data: JsValue): GenericCondition[HttpRequest] = {
    val ec = ExecutionContext.global
    implicit val system = ActorSystem("BodyUrlEncodedConditionSpec")
    val materializer = ActorMaterializer()

    new BodyJsonCondition(data)(ec, materializer)
  }

  protected def spawnRequest(withData: Option[JsValue]): HttpRequest = {
    HttpRequest(
      entity = HttpEntity.Strict(
        ContentTypes.`application/json`,
        ByteString(withData.map(_.toString).getOrElse("{}"))
      )
    )
  }
}
