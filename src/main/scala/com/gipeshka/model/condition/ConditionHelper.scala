package com.gipeshka.model.condition

object ConditionHelper
{
  implicit class OptionConditionWrapper[T](condition: Option[GenericCondition[T]])
  {
    def and(otherCondition: Option[GenericCondition[T]], defaultValue: Boolean = true): GenericCondition[T] = {
      GenericCondition[T](_ => defaultValue).and(otherCondition).and(condition)
    }

    def or(otherCondition: Option[GenericCondition[T]], defaultValue: Boolean = false): GenericCondition[T] = {
      GenericCondition[T](_ => defaultValue).or(otherCondition).or(condition)
    }
  }

  implicit def functionToGenericCondition[T](function: T => Boolean): GenericCondition[T] = {
    GenericCondition[T](function)
  }
}
