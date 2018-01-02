package com.gipeshka.model.condition

import akka.http.scaladsl.model.HttpRequest

trait RequestCondition
{
  def apply(request: HttpRequest): Boolean
}
