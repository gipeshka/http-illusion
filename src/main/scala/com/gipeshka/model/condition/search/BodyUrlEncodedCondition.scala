package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class BodyUrlEncodedCondition(
  expected: Map[String, String]
) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.bodyUrlEncoded.exists { urlEncodedRequest =>
      (expected.toSet diff urlEncodedRequest.toSet).isEmpty
    }
  }
}
