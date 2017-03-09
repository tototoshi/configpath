package com.github.tototoshi.configpath

import com.typesafe.config.ConfigFactory

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
