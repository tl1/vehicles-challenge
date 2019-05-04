import sbt._

object Dependencies {

  // Test --------------------------------------------------------------------------------------------------------------
  
  lazy val scala_test = "org.scalatest" %% "scalatest" % "3.0.5"
  
  // Overrides ---------------------------------------------------------------------------------------------------------

  lazy val scala_parser_combinators = "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.7"
  lazy val akka_stream = "com.typesafe.akka" %% "akka-stream" % "2.5.19"
  lazy val akka_actor = "com.typesafe.akka" %% "akka-actor" % "2.5.19"
  lazy val guava = "com.google.guava" % "guava" % "23.6.1-jre"

}
