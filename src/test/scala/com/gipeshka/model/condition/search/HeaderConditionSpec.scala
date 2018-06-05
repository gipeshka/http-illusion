package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.{GenericCondition, MapMatchingConditionSpec}
import com.gipeshka.request.ReactionRequestPart

class HeaderConditionSpec extends MapMatchingConditionSpec[ReactionRequestPart]
{
  protected def spawnCondition(data: Map[String, String]): GenericCondition[ReactionRequestPart] = {
    new HeaderCondition(data)
  }

  protected def spawnRequest(withData: Option[Map[String, String]]): ReactionRequestPart = {
    ReactionRequestPart(
      headers = withData
    )
  }
}
