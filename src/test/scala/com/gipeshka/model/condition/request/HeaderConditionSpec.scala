package com.gipeshka.model.condition.request

import scala.collection.immutable.Seq

import akka.http.scaladsl.model.HttpHeader.ParsingResult
import akka.http.scaladsl.model.{HttpHeader, HttpRequest}
import com.gipeshka.model.condition.{GenericCondition, MapMatchingConditionSpec}

class HeaderConditionSpec extends MapMatchingConditionSpec[HttpRequest]
{
  protected def spawnCondition(data: Map[String, String]): GenericCondition[HttpRequest] = {
    new HeaderCondition(data)
  }

  protected def spawnRequest(withData: Option[Map[String, String]]): HttpRequest = {
    val seq = withData.map(_.to[collection.immutable.Seq]).getOrElse(Seq())

    HttpRequest(
      headers = seq.map { case (name, value) =>
        HttpHeader.parse(name, value) match {
          case ParsingResult.Ok(header, _) => header
          case ParsingResult.Error(error) => throw new Exception(error.format(true))
        }
      }
    )
  }
}
