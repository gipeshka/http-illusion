package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.HttpRequest
import com.gipeshka.model.condition.GenericCondition

class MethodCondition(method: String) extends GenericCondition[HttpRequest]
{
  def apply(request: HttpRequest): Boolean = {
    request.method.value.toLowerCase == method.toLowerCase
  }
}
