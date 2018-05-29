package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.GenericCondition
import com.gipeshka.request.ReactionRequestPart
import gnieh.diffson.sprayJson._
import spray.json._

class BodyJsonCondition(
  json: JsValue
) extends GenericCondition[ReactionRequestPart]
{
  def apply(request: ReactionRequestPart): Boolean = {
    request.bodyJson.exists { requestJson =>
      JsonDiff.diff(json, requestJson, false).ops.forall(_.isInstanceOf[Add])
    }
  }
}
