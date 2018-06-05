package com.gipeshka.model.condition

import com.gipeshka.model.condition.ConditionTestHelper.{stand, _}
import org.scalatest.WordSpec
import org.scalatest.compatible.Assertion
import spray.json._

abstract class JsonConditionSpec[T]
  extends WordSpec
    with ConditionSpec[T, JsValue]
{
  "Search BodyJsonCondition" should {
    "be true" when {
      "json match" in fullMatchCase
      "condition json is part of request json" in includeMatchCase
    }
    "be false" when {
      "json doesn't match" in noMatchCase
      "ReactionRequestPart misses bodyJson" in requiredFieldMissingCase
    }
  }

  protected val defaultData: JsValue = """{
      "first": "test",
      "second": {
        "inner": "test"
      }
    }""".parseJson

  protected val otherData: JsValue = """{
      "first": "test",
      "second": {

      }
    }""".parseJson

  private def includeMatchCase: Assertion = {
    condition should stand(
      spawnRequest(
        Some(
          """{
            "first": "test",
            "second": {
              "inner": "test",
              "with": "present"
            },
            "some": "additional",
            "fields": 123
          }""".parseJson
        )
      )
    )
  }
}
