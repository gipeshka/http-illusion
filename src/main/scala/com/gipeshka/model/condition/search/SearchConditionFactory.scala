package com.gipeshka.model.condition.search

import com.gipeshka.model.condition.{ConditionFactory, GenericCondition}
import com.gipeshka.request.ReactionRequestPart
import spray.json.JsValue

class SearchConditionFactory extends ConditionFactory[ReactionRequestPart]
{
  def bodyJson(json: JsValue): GenericCondition[ReactionRequestPart] = {
    new BodyJsonCondition(json)
  }

  def bodyUrl(expected: Map[String, String]): GenericCondition[ReactionRequestPart] = {
    new BodyUrlEncodedCondition(expected)
  }

  def header(headers: Map[String, String]): GenericCondition[ReactionRequestPart] = {
    new HeaderCondition(headers)
  }

  def method(method: String): GenericCondition[ReactionRequestPart] = {
    new MethodCondition(method)
  }

  def path(path: String): GenericCondition[ReactionRequestPart] = {
    new PathCondition(path)
  }

  def query(query: Map[String, String]): GenericCondition[ReactionRequestPart] = {
    new QueryCondition(query)
  }
}
