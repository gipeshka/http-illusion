package com.gipeshka.model.condition

import com.gipeshka.model.condition.ConditionTestHelper._
import org.scalatest.compatible.Assertion

trait ConditionSpec[T, Data]
{
  protected def defaultData: Data

  protected def otherData: Data

  protected def spawnCondition(data: Data): GenericCondition[T]

  protected def spawnRequest(withData: Option[Data]): T

  protected lazy val condition = spawnCondition(defaultData)

  def fullMatchCase: Assertion = {
    condition should stand(
      spawnRequest(
        Some(defaultData)
      )
    )
  }

  def noMatchCase: Assertion = {
    condition shouldNot stand(
      spawnRequest(
        Some(otherData)
      )
    )
  }

  def requiredFieldMissingCase: Assertion = {
    condition shouldNot stand(
      spawnRequest(
        None
      )
    )
  }
}
