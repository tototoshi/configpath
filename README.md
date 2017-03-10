# configpath

[![Build Status](https://travis-ci.org/tototoshi/configpath.png)](https://travis-ci.org/tototoshi/configpath)


## Install

```scala
resolvers +=
  "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"

libraryDependencies += "com.github.tototoshi" %% "configpath" % "0.1.0-SNAPSHOT"

addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M7" cross CrossVersion.full)
```


## Example

```scala
import com.typesafe.config.ConfigFactory
import com.github.tototoshi.configpath.compile

@compile("example/src/main/resources/application.conf")
object path

object Example {

  def main(args: Array[String]): Unit = {
    val config = ConfigFactory.load()
    val serializer1 = config.getString(path.akka.actor.serializers.`akka-containers`.full)
    val serializer2 = config.getString("akka.actor.serializers.akka-containers")
    assert(serializer1 == serializer2)
  }

}
```
