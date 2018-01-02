package com.gipeshka.exception

import akka.http.scaladsl.model.HttpRequest

case class MockerNoReactionFoundException(
  request: HttpRequest
) extends CannotHandleRequestException(
  "Mocker",
  request
)
