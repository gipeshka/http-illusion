package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.ConditionSpec
import com.gipeshka.request.ReactionRequestPart
import org.scalatest.WordSpec

abstract class StringMatchingConditionSpec
  extends WordSpec
    with ConditionSpec[ReactionRequestPart, String]
{
  s"Search ${this.condition.getClass.getSimpleName}" should {
    "be true when values match" in fullMatchCase
    "be false" when {
      "values don't match" in noMatchCase
      "ReactionRequestPart misses required field" in requiredFieldMissingCase
    }
  }

  protected def defaultData: String = "right value"

  protected def otherData: String = "other value"
}
