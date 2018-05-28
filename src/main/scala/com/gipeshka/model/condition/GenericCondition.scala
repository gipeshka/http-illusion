package com.gipeshka.model.condition

trait GenericCondition[T]
{
  def apply(request: T): Boolean
}
