package com.gipeshka.model.condition.request

import akka.http.scaladsl.model.{HttpMethod, HttpRequest, Uri}
import com.gipeshka.model.condition.{ConditionSpec, GenericCondition}
import org.scalatest.WordSpec

class PathConditionSpec
  extends WordSpec
    with ConditionSpec[HttpRequest, String]
{
  protected def defaultData: String = "/some/uri"

  protected def otherData: String = "/other/uri"

  protected def spawnCondition(data: String): GenericCondition[HttpRequest] = {
    new PathCondition(data)
  }

  protected def spawnRequest(withData: Option[String]): HttpRequest = {
    HttpRequest(
      uri = Uri(withData.get)
    )
  }
}
