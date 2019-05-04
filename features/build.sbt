name := "features"

libraryDependencies := Seq(
  "org.scala-lang" % "scala-library" % "2.12.8",
  "com.novocode" % "junit-interface" % "0.11" % Test exclude("junit", "junit-dep"),
  "io.cucumber" %% "cucumber-scala" % "4.3.0" % Test,
  "io.cucumber" % "cucumber-junit" % "4.3.0" % Test
)
