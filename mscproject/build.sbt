lazy val root = (project in file(".")).
  settings(
    name := "MScProject",
    version := "1.0",
    scalaVersion := "2.11.8",
    resolvers += "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test",
    libraryDependencies += "com.typesafe.akka" % "akka-actor_2.11" % "2.4.7",
    libraryDependencies += "com.typesafe.akka" % "akka-remote_2.11" % "2.4.7",
    libraryDependencies += "org.scala-stm" % "scala-stm_2.11" % "0.7",
    libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.2.3" % "provided",
	libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.2.3",
	libraryDependencies += "com.softwaremill.macwire" %% "proxy" % "2.2.3",
	libraryDependencies += "com.typesafe.play" % "play-json_2.11" % "2.4.6",
	libraryDependencies += "io.spray" %%  "spray-json" % "1.3.2",
	libraryDependencies += "io.spray" % "spray-httpx_2.11" % "1.3.3"
)