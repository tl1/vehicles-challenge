import Dependencies._

name := "challenge"

inThisBuild(
  List(
    version := "1",
    scalaVersion := "2.12.8",
    dependencyOverrides := Seq(
      scala_parser_combinators,
      akka_stream,
      akka_actor,
      guava
    )
  )
)

