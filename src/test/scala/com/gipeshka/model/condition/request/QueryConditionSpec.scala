package com.gipeshka.model.condition.request

import scala.collection.immutable.Seq

import akka.http.scaladsl.model.{HttpRequest, Uri}
import com.gipeshka.model.condition.{GenericCondition, MapMatchingConditionSpec}

class QueryConditionSpec extends MapMatchingConditionSpec[HttpRequest]
{
  protected def spawnCondition(data: Map[String, String]): GenericCondition[HttpRequest] = {
    new QueryCondition(data)
  }

  protected def spawnRequest(withData: Option[Map[String, String]]): HttpRequest = {
    val seq = withData.map(_.toSeq).getOrElse(Seq())

    HttpRequest(
      uri = Uri./.withQuery(
        Uri.Query(seq:_*)
      )
    )
  }
}
