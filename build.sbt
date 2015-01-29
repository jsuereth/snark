

val core = project.settings(
  scalaVersion := "2.11.5",
  organization := "com.jsuereth.snark",
  name := "snark-core",
  version := "1.0-SNAPSHOT",
  libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "4.0.2",
    "org.scribe" % "scribe" % "1.3.6",
    "org.scala-sbt" % "completion" % "0.13.7"
  )
)


