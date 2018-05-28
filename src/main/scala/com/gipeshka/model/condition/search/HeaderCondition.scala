package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class HeaderCondition(headers: Map[String, String]) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.headers.exists { requestHeaders =>
      (headers.toSet diff requestHeaders.toSet).isEmpty
    }
  }
}
