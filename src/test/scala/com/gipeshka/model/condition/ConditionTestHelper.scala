package com.gipeshka.model.condition

import org.scalactic.source
import org.scalatest.compatible.Assertion
import org.scalatest.exceptions.TestFailedException
import org.scalatest.{Matchers, Succeeded, exceptions}

object ConditionTestHelper
{
  case class StandWord[T](value: T)
  {
    override def toString: String = "stand"
  }

  def stand[T](value: T): StandWord[T] = StandWord(value)

  class ConditionShouldWrapper[T](left: GenericCondition[T], pos: source.Position) extends Matchers
  {
    def should(right: StandWord[T]): Assertion = shouldStand(right, true)

    def shouldNot(right: StandWord[T]): Assertion = shouldStand(right, false)

    private def shouldStand(right: StandWord[T], shouldStand: Boolean = true): Assertion = {
      if (left.apply(right.value) != shouldStand) {
        throw new TestFailedException(
          (_: exceptions.StackDepthException) => Some(
            s"Condition should have ${if (shouldStand) "stood" else "failed"} with ${right.value}"
          ),
          None,
          pos
        )
      } else {
        Succeeded
      }
    }
  }

  implicit def convertToConditionShouldWrapper[T](
    condition: GenericCondition[T]
  )(implicit
    pos: source.Position
  ): ConditionShouldWrapper[T] = new ConditionShouldWrapper(condition, pos)
}
