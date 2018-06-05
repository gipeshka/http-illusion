package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.{HttpMethod, HttpMethods, HttpRequest}
import com.gipeshka.model.condition.{ConditionSpec, GenericCondition}
import org.scalatest.WordSpec

class MethodConditionSpec
  extends WordSpec
    with ConditionSpec[HttpRequest, HttpMethod]
{
  s"Search ${this.condition.getClass.getSimpleName}" should {
    "be true when values match" in fullMatchCase
    "be false when values don't match" in noMatchCase
  }

  protected def defaultData: HttpMethod = HttpMethods.GET

  protected def otherData: HttpMethod = HttpMethods.DELETE

  protected def spawnCondition(data: HttpMethod): GenericCondition[HttpRequest] = {
    new MethodCondition(data.value)
  }

  protected def spawnRequest(withData: Option[HttpMethod]): HttpRequest = {
    HttpRequest(method = withData.get)
  }
}
