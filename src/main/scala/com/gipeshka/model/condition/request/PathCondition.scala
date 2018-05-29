package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.HttpRequest
import com.gipeshka.model.condition.GenericCondition

class PathCondition(path: String) extends GenericCondition[HttpRequest]
{
  def apply(request: HttpRequest): Boolean = {
    request.uri.path.toString == path
  }
}
