import Dependencies._

name := "repositories"

libraryDependencies ++= Seq(
  kantan_csv,
  scala_test % Test
)
