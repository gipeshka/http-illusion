package com.gipeshka.model.condition
import akka.http.scaladsl.model.HttpRequest

class QueryCondition(query: Map[String, String]) extends RequestCondition
{
  def apply(request: HttpRequest): Boolean = {
    val requestQuery = request.uri.query().toMap

    (query.toSet diff requestQuery.toSet).isEmpty
  }
}
