name := "api"

libraryDependencies ++= Seq(
  guice
)

PlayKeys.devSettings += "play.server.http.port" -> "8081"