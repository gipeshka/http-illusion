package com.gipeshka.model.condition

import spray.json.JsValue

trait ConditionFactory[T]
{
  def bodyJson(json: JsValue): GenericCondition[T]

  def bodyUrl(expected: Map[String, String]): GenericCondition[T]

  def header(headers: Map[String, String]): GenericCondition[T]

  def method(method: String): GenericCondition[T]

  def path(path: String): GenericCondition[T]

  def query(query: Map[String, String]): GenericCondition[T]
}
