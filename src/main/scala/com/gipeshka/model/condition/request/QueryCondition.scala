package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.HttpRequest
import com.gipeshka.model.condition.GenericCondition

class QueryCondition(query: Map[String, String]) extends GenericCondition[HttpRequest]
{
  def apply(request: HttpRequest): Boolean = {
    val requestQuery = request.uri.query().toMap

    (query.toSet diff requestQuery.toSet).isEmpty
  }
}
