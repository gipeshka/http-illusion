package com.gipeshka.request

case class SearchReactionRequest(
  request: ReactionRequestPart
)

trait SearchReactionRequestFormat extends ReactionRequestPartFormat
{
  implicit val searchReactionRequestFormat = jsonFormat1(SearchReactionRequest)
}
