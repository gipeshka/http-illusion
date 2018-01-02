package com.gipeshka.model.condition

import akka.http.scaladsl.model.HttpRequest

class MethodCondition(method: String) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    request.method.value.toLowerCase == method.toLowerCase
  }
}
