package com.gipeshka.model.condition

import akka.http.scaladsl.model.HttpRequest

class ChainCondition(conditions: List[RequestCondition] = List()) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    conditions.forall(_(request))
  }

  def and(condition: RequestCondition): ChainCondition = {
    new ChainCondition(condition :: this.conditions)
  }

  def and(condition: Option[RequestCondition]): ChainCondition = {
    condition match {
      case Some(condition) => and(condition)
      case None => this
    }
  }
}
