package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.{GenericCondition, JsonConditionSpec}
import com.gipeshka.request.ReactionRequestPart
import spray.json._

class BodyJsonConditionSpec extends JsonConditionSpec[ReactionRequestPart]
{
  protected def spawnCondition(data: JsValue): GenericCondition[ReactionRequestPart] = {
    new BodyJsonCondition(data)
  }

  protected def spawnRequest(withData: Option[JsValue]): ReactionRequestPart = {
    ReactionRequestPart(
      bodyJson = withData
    )
  }
}
