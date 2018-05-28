package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class QueryCondition(query: Map[String, String]) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.query.exists { requestQuery =>
      (query.toSet diff requestQuery.toSet).isEmpty
    }
  }
}
