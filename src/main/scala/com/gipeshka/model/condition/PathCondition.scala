package com.gipeshka.model.condition
import akka.http.scaladsl.model.HttpRequest

class PathCondition(path: String) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    request.uri.path.toString == path
  }
}
