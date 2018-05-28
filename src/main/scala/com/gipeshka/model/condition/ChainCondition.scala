package com.gipeshka.model.condition

class ChainCondition[T](conditions: List[GenericCondition[T]] = List()) extends GenericCondition[T]
{
  def apply(request: T): Boolean = {
    conditions.forall(_(request))
  }

  def and(condition: GenericCondition[T]): ChainCondition[T] = {
    new ChainCondition(condition :: this.conditions)
  }

  def and(condition: Option[GenericCondition[T]]): ChainCondition[T] = {
    condition match {
      case Some(condition) => and(condition)
      case None => this
    }
  }
}
