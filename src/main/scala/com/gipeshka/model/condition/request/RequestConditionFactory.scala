package com.gipeshka.model.condition.request

import javax.inject.Inject

import scala.concurrent.ExecutionContext

import akka.http.scaladsl.model.HttpRequest
import akka.stream.Materializer
import com.gipeshka.model.condition.{ConditionFactory, GenericCondition}
import spray.json.JsValue

class RequestConditionFactory @Inject()(implicit
  ec: ExecutionContext,
  m: Materializer
) extends ConditionFactory[HttpRequest]
{
  def bodyJson(json: JsValue): GenericCondition[HttpRequest] = {
    new BodyJsonCondition(json)
  }

  def bodyUrl(expected: Map[String, String]): GenericCondition[HttpRequest] = {
    new BodyUrlEncodedCondition(expected)
  }

  def header(headers: Map[String, String]): GenericCondition[HttpRequest] = {
    new HeaderCondition(headers)
  }

  def method(method: String): GenericCondition[HttpRequest] = {
    new MethodCondition(method)
  }

  def path(path: String): GenericCondition[HttpRequest] = {
    new PathCondition(path)
  }

  def query(query: Map[String, String]): GenericCondition[HttpRequest] = {
    new QueryCondition(query)
  }
}
