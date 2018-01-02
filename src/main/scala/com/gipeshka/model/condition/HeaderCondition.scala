package com.gipeshka.model.condition
import akka.http.scaladsl.model.HttpRequest

class HeaderCondition(headers: Map[String, String]) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    val requestHeaders = request.headers.map { header =>
      (header.name, header.value)
    }

    (headers.toSet diff requestHeaders.toSet).isEmpty
  }
}
