package com.gipeshka.exception

import akka.http.scaladsl.model.HttpRequest

class CannotHandleRequestException(
  handlerName: String,
  request: HttpRequest,
  msg: Option[String] = None
) extends RuntimeException(
  s"Handler $handlerName was unable to handle request $request. $msg"
)
