package com.gipeshka.model.condition

import com.gipeshka.model.condition.ConditionTestHelper._
import org.scalatest.WordSpec
import org.scalatest.compatible.Assertion

abstract class MapMatchingConditionSpec[T]
  extends WordSpec
    with ConditionSpec[T, Map[String, String]]
{
  protected val defaultData: Map[String, String] = Map(
    "test" -> "values",
    "that" -> "match"
  )

  protected def otherData: Map[String, String] = Map(
    "test" -> "values"
  )

  s"Search ${this.condition.getClass.getSimpleName}" should {
    "be true" when {
      "maps match" in fullMatchCase
      "condition map is part of request map" in includeMatchCase
    }
    "be false" when {
      "maps don't match" in noMatchCase
      "ReactionRequestPart misses required field" in requiredFieldMissingCase
    }
  }

  def includeMatchCase: Assertion = {
    condition should stand(
      spawnRequest(
        Some(
          Map(
            "test" -> "values",
            "that" -> "match",
            "and_something" -> "else"
          )
        )
      )
    )
  }
}
