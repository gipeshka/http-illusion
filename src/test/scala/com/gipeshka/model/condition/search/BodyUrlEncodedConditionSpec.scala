package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.{GenericCondition, MapMatchingConditionSpec}
import com.gipeshka.request.ReactionRequestPart

class BodyUrlEncodedConditionSpec extends MapMatchingConditionSpec[ReactionRequestPart]
{
  protected def spawnCondition(data: Map[String, String]): GenericCondition[ReactionRequestPart] = {
    new BodyUrlEncodedCondition(data)
  }

  protected def spawnRequest(withData: Option[Map[String, String]]): ReactionRequestPart = {
    ReactionRequestPart(
      bodyUrlEncoded = withData
    )
  }
}
