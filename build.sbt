name := "Http Illusion"

version := "0.1"

scalaVersion := "2.12.4"

libraryDependencies ++= {
  Seq(
    // general dependencies
    "com.typesafe.akka" %% "akka-http" % "10.0.11",
    "net.codingwell" %% "scala-guice" % "4.1.1",
    "com.github.pureconfig" %% "pureconfig" % "0.8.0",
    "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.11",
    "org.gnieh" %% "diffson-spray-json" % "2.2.4",

    // connector specific dependencies
    "com.couchbase.client" % "java-client" % "2.5.3",

    // test dependencies
    "org.scalatest" % "scalatest_2.12" % "3.0.5" % "test"
  )
}
