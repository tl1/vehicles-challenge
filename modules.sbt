lazy val vehicles = project in file("./vehicles")

lazy val repositories = (project in file("./repositories"))
  .dependsOn(vehicles)

lazy val api = (project in file("./api"))
  .enablePlugins(PlayScala)
  .dependsOn(vehicles, repositories)

lazy val features = project in file("./features")

lazy val root = (project in file("."))
  .aggregate(vehicles, repositories, api)

