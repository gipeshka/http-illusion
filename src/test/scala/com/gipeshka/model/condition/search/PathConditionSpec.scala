package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class PathConditionSpec extends StringMatchingConditionSpec
{
  protected def spawnCondition(data: String): GenericCondition[ReactionRequestPart] = {
    new PathCondition(data)
  }

  protected def spawnRequest(withData: Option[String]): ReactionRequestPart = {
    ReactionRequestPart(
      path = withData
    )
  }
}
