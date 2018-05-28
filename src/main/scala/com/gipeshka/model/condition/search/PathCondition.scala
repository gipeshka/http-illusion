package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart

class PathCondition(path: String) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.path.contains(path)
  }
}
