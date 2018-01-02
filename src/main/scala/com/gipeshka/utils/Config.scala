package com.gipeshka.utils

import pureconfig.loadConfig

private[utils] case class HttpConfig(host: String, port: Int)

private[utils] case class AppConfig(fallback: FallbackConfig, handlerParallelism: Int)

private[utils] case class FallbackConfig(host: String, port: Int, scheme: String)

case class Config(
  http: HttpConfig,
  app: AppConfig
)

object Config
{
  def load: Config = {
    loadConfig[Config] match {
      case Right(config) => config
      case Left(error) =>
        throw new RuntimeException("Cannot read config file, errors:\n" + error.toList.mkString("\n"))
    }
  }
}
