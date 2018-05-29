package com.gipeshka.model.condition

trait GenericCondition[T] extends Function1[T, Boolean]
{
  def and(otherCondition: GenericCondition[T]): GenericCondition[T] = {
    GenericCondition[T](v1 => this.apply(v1) && otherCondition(v1))
  }

  def and(otherCondition: Option[GenericCondition[T]]): GenericCondition[T] = {
    optionalCondition(otherCondition, and)
  }

  def or(otherCondition: GenericCondition[T]): GenericCondition[T] = {
    GenericCondition[T](v1 => this.apply(v1) || otherCondition(v1))
  }

  def or(otherCondition: Option[GenericCondition[T]]): GenericCondition[T] = {
    optionalCondition(otherCondition, or)
  }

  private def optionalCondition(
    otherCondition: Option[GenericCondition[T]],
    ifPresent: GenericCondition[T] => GenericCondition[T]
  ): GenericCondition[T] = {
    otherCondition match {
      case Some(presentCondition) => ifPresent(presentCondition)
      case None => this
    }
  }
}

object GenericCondition
{
  def apply[T](predicate: T => Boolean): GenericCondition[T] = {
    new GenericCondition[T]
    {
      def apply(t: T) = predicate(t)
    }
  }
}
