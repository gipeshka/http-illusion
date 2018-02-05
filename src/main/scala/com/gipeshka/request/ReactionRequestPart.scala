package com.gipeshka.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsValue}

case class ReactionRequestPart(
  method: Option[String],
  path: Option[String],
  query: Option[Map[String, String]],
  headers: Option[Map[String, String]],
  bodyJson: Option[JsValue],
  bodyUrlEncoded: Option[Map[String, String]]
)

trait ReactionRequestPartFormat extends SprayJsonSupport with DefaultJsonProtocol
{
  implicit val formatRequestPart = jsonFormat6(ReactionRequestPart)
}
