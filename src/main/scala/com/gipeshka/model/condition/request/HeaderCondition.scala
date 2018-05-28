package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.HttpRequest
import com.gipeshka.model.condition.GenericCondition

class HeaderCondition(headers: Map[String, String]) extends GenericCondition[HttpRequest]
{
  def apply(request: HttpRequest): Boolean = {
    val requestHeaders = request.headers.map { header =>
      (header.name, header.value)
    }

    (headers.toSet diff requestHeaders.toSet).isEmpty
  }
}
