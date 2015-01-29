lazy val makeConscriptProperties = taskKey[File]("makeConscriptProperties")

val commonSettings: Seq[Setting[_]] =
  Seq(
    scalaVersion := "2.11.5",
    organization := "com.jsuereth.snark",
    version := "0.1"
  )

val core = project.settings(commonSettings:_*).settings(
  name := "snark-core",
  libraryDependencies ++= Seq(
    "org.twitter4j" % "twitter4j-core" % "4.0.2",
    "org.scribe" % "scribe" % "1.3.6",
    "org.scala-sbt" % "completion" % "0.13.7"
  ),
  publishTo :=
   Some("Bintray Snark API" at "https://api.bintray.com/maven/jsuereth/libs-and-utils/snark")
)


val native = project.dependsOn(core).settings(commonSettings:_*).settings(
  name := "snark",
  maintainer := "Josh Suereth <joshua.suereth@gmail.com>",
  packageSummary := "snark is a twitter CLI",
  packageDescription :=
    """|Find out what your friends are tweeting.""".stripMargin,
  mainClass in Compile := Some("com.jsuereth.snark.Main")
).enablePlugins(JavaAppPackaging)


makeConscriptProperties := {
  val csDir = (sourceDirectory in Compile).value / "cs" / "snark"
  val launchConfigFile = csDir / "launchconfig"
  IO.write(launchConfigFile,
    s"""
       |[scala]
       |  version: auto
       |[app]
       |  org: ${(organization in core).value}
       |  name: ${(name in core).value}_${(scalaBinaryVersion in core).value}
       |  version: ${(version in core).value}
       |  class:   ${(mainClass in Compile in native).value.get}
       |  cross-versioned: false
       |
       |[repositories]
       |  bintray-jsuereth-libs: http://dl.bintray.com/jsuereth/libs-and-utils
       |  maven-central
       |  typesafe-ivy-releases: https://repo.typesafe.com/typesafe/ivy-releases/, [organization]/[module]/[revision]/[type]s/[artifact](-[classifier]).[ext], bootOnly
       |
       |[boot]
       |  directory: $${sbt.boot.directory-$${sbt.global.base-$${user.home}/.sbt}/boot/}
       |
       |[ivy]
       |  ivy-home: $${sbt.ivy.home-$${user.home}/.ivy2/}
       |  checksums: $${sbt.checksums-sha1,md5}
       |  override-build-repos: $${sbt.override.build.repos-false}
       |  repository-config: $${sbt.repository.config-$${sbt.global.base-$${user.home}/.sbt}/repositories}
       |""".stripMargin)
  launchConfigFile
}


