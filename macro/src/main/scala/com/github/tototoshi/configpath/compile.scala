package com.github.tototoshi.configpath

import java.io.File

import com.typesafe.config._

import scala.annotation.StaticAnnotation
import scala.collection.JavaConverters._
import scala.collection.immutable.Seq
import scala.meta._

class compile extends StaticAnnotation {

  inline def apply(defn: Any): Any = meta {

    trait HasTemplate[T] {
      def templ(t: T): Template
      def withTemplate(t: T, templ: Template): T
      def withTemplate(t: T, f: Template => Template): T = withTemplate(t, f(templ(t)))
    }

    object HasTemplate {
      implicit val objHasTemplate = new HasTemplate[Defn.Object] {
        override def templ(t: Defn.Object): Template = t.templ
        override def withTemplate(t: Defn.Object, templ: Template): Defn.Object = t.copy(templ = templ)
      }
      implicit val clsHasTemplate = new HasTemplate[Defn.Class] {
        override def templ(t: Defn.Class): Template = t.templ
        override def withTemplate(t: Defn.Class, templ: Template): Defn.Class = t.copy(templ = templ)
      }
    }

    val configTrait = Seq(
      q"""
          import com.typesafe.config._
        """,
      q"""
         abstract class ConfigTree(val name: String, val full: String)
        """
    )

    def defConfigTree(name: String, path: String, fullPath: String): Defn.Object = {
      q"object ${Term.Name(s"`$name`")} extends ConfigTree(${Lit(path)}, ${Lit(fullPath)})"
    }

    def addStatsToTempl(templ: Template, prepend: Seq[Stat], append: Seq[Stat]): Template = {
      val stats = templ.stats.getOrElse(Seq.empty[Stat])
      val newStats = prepend ++ stats ++ append
      templ.copy(stats = Some(newStats))
    }

    def addStats[T: HasTemplate](obj: T, prepend: Seq[Stat], append: Seq[Stat]): T = {
      val imp = implicitly[HasTemplate[T]]
      imp.withTemplate(obj, templ => addStatsToTempl(templ = templ, prepend, append))
    }

    val configFile = this match {
      case q"new $_(${Lit(path)})" => new File(path.toString)
      case _ => abort("config file is not specified")
    }

    val config = ConfigFactory.parseFile(configFile).resolve()

    val rootConfig: ConfigObject = config.root()

    def go[T: HasTemplate](obj: T, config: ConfigValue, path: List[String]): T = {
      config.valueType() match {
        case ConfigValueType.OBJECT =>
          val configObject = config.asInstanceOf[ConfigObject]
          configObject.keySet()
          val children = configObject.asScala.toMap.map { case (key, value) =>
            val child = defConfigTree(key, key, (path :+ key).mkString("."))
            go(child, value, path :+ key)
          }.to[Seq]
          addStats(obj, Seq.empty[Stat], children)
        case ConfigValueType.LIST |
             ConfigValueType.NUMBER |
             ConfigValueType.BOOLEAN |
             ConfigValueType.STRING |
             ConfigValueType.NULL =>
          obj
      }
    }

    val annotated = defn match {
      case obj: Defn.Object=>
        addStats(go(obj, rootConfig, path = Nil), configTrait, Seq.empty[Stat])
      case cls: Defn.Class =>
        addStats(go(cls, rootConfig, path = Nil), configTrait, Seq.empty[Stat])
      case _ =>
        abort("@compile must annotate object or class")
    }

    annotated
  }

}
