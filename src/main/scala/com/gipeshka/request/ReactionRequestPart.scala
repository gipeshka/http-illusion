package com.gipeshka.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsValue}

case class ReactionRequestPart(
  method: Option[String] = None,
  path: Option[String] = None,
  query: Option[Map[String, String]] = None,
  headers: Option[Map[String, String]] = None,
  bodyJson: Option[JsValue] = None,
  bodyUrlEncoded: Option[Map[String, String]] = None
)

trait ReactionRequestPartFormat extends SprayJsonSupport with DefaultJsonProtocol
{
  implicit val formatRequestPart = jsonFormat6(ReactionRequestPart)
}
