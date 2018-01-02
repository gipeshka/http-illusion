package com.gipeshka.request

case class DeleteReactionRequest(
  request: ReactionRequestPart
)

trait DeleteReactionRequestFormat extends ReactionRequestPartFormat
{
  implicit val deleteReactionRequestFormat = jsonFormat1(DeleteReactionRequest)
}
