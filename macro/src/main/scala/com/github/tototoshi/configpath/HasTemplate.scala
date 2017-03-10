package com.github.tototoshi.configpath

import scala.meta._

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
