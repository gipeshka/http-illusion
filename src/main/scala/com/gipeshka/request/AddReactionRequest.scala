package com.gipeshka.request

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json._

case class AddReactionRequest(
  request: ReactionRequestPart,
  response: ReactionResponsePart
)

case class ReactionResponsePart(
  status: Int,
  body: JsValue
)

trait AddReactionRequestFormat extends ReactionRequestPartFormat
{
  implicit val formatResponsePart = jsonFormat2(ReactionResponsePart)
  implicit val addReactionRequestformat = jsonFormat2(AddReactionRequest)
}
