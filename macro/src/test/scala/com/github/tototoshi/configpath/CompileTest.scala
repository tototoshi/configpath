package com.github.tototoshi.configpath

import com.typesafe.config.ConfigFactory
import org.scalatest.FunSuite

class CompileTest extends FunSuite {

  test("@compile can annotate class") {
    @compile("macro/src/test/resources/application.conf")
    class Path

    val path = new Path
    assert(path.akka.actor.serializers.`akka-containers`.full === "akka.actor.serializers.akka-containers")
    assert(path.akka.actor.serializers.`akka-containers`.name === "akka-containers")
  }

  test("@compile can annotate object") {
    @compile("macro/src/test/resources/application.conf")
    object path

    assert(path.akka.actor.serializers.`akka-containers`.full === "akka.actor.serializers.akka-containers")
    assert(path.akka.actor.serializers.`akka-containers`.name === "akka-containers")
  }

  test("@compile can annotate a case class with companion object") {
    @compile("macro/src/test/resources/application.conf")
    case class path()
    object path

    assert(path().akka.actor.serializers.`akka-containers`.full === "akka.actor.serializers.akka-containers")
    assert(path().akka.actor.serializers.`akka-containers`.name === "akka-containers")
  }

}
