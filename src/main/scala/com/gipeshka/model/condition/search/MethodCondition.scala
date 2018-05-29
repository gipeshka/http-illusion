package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class MethodCondition(method: String) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.method.map(_.toLowerCase).contains(method.toLowerCase)
  }
}
