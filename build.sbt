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

lazy val macro = project.in(file("macro"))
  .settings(publishSettings)
  .settings(
    organization := "com.github.tototoshi",
    name := """configpath""",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1",
      "org.scalameta" %% "scalameta" % "1.6.0",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    ),
    addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M7" cross CrossVersion.full)
)

lazy val example = project.in(file("example"))
  .settings(noPublishSettings)
  .settings(
    organization := "com.github.tototoshi",
    name := """configpath-example""",
    version := "0.1.0-SNAPSHOT",
    scalaVersion := "2.11.8",
    libraryDependencies ++= Seq(
      "com.typesafe" % "config" % "1.3.1"
    ),
    addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M7" cross CrossVersion.full)
).dependsOn(macro)

lazy val root = project.in(file("."))
  .settings(noPublishSettings)
  .aggregate(macro, example)
