val scalametaVersion = "1.8.0"
val paradiseVersion = "3.0.0-M8"
val typesafeConfigVersion = "1.3.1"

lazy val publishSettings = Seq(
  publishMavenStyle := true,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (version.value.trim.endsWith("SNAPSHOT")) Some("snapshots" at nexus + "content/repositories/snapshots")
    else Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  publishArtifact in Test := false,
  pomExtra :=
    <url>http://github.com/tototoshi/configpath</url>
    <licenses>
      <license>
        <name>Apache License, Version 2.0</name>
        <url>http://github.com/tototoshi/configpath/blob/master/LICENSE.txt</url>
        <distribution>repo</distribution>
      </license>
    </licenses>
    <scm>
      <url>git@github.com:tototoshi/configpath.git</url>
      <connection>scm:git:git@github.com:tototoshi/configpath.git</connection>
    </scm>
    <developers>
      <developer>
        <id>tototoshi</id>
        <name>Toshiyuki Takahashi</name>
        <url>http://tototoshi.github.com</url>
      </developer>
    </developers>
)

lazy val noPublishSettings = Seq(
  publishArtifact := false,
  publish := {},
  publishLocal := {}
)

lazy val baseSettings = Seq(
  organization := "com.github.tototoshi",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.2"
)

lazy val macro = project.in(file("macro"))
  .settings(baseSettings)
  .settings(publishSettings)
  .settings(
    organization := "com.github.tototoshi",
    name := """configpath""",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % typesafeConfigVersion,
      "org.scalameta" %% "scalameta" % "1.8.0",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),
    addCompilerPlugin("org.scalameta" % "paradise" % paradiseVersion cross CrossVersion.full)
)

lazy val example = project.in(file("example"))
  .settings(baseSettings)
  .settings(noPublishSettings)
  .settings(
    name := """configpath-example""",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % typesafeConfigVersion
    ),
    addCompilerPlugin("org.scalameta" % "paradise" % paradiseVersion cross CrossVersion.full)
).dependsOn(macro)

lazy val root = project.in(file("."))
  .settings(baseSettings)
  .settings(noPublishSettings)
  .aggregate(macro, example)
