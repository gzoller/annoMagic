package co.blocke.annoMagic

import java.beans.Introspector

import scala.collection.immutable.{ List, ListMap }
import scala.language.existentials
import scala.reflect.runtime.currentMirror
import scala.reflect.runtime.universe._

/*
  Find annotations on:

  * Class
  * Term (val/var)
  * Methods
  * Constructor parameters (case and non-case classes)

 */

object Inspector {

  def methodAnnotations[T]()(implicit tt: TypeTag[T]): Map[String, Map[String, Map[String, Any]]] = {
    tt.tpe.decls.collect {
      case m: MethodSymbol => m
      case f: TermSymbol   => f
    }.withFilter {
      _.annotations.nonEmpty
    }.map { m =>
      if (m.annotations.head.toString.contains("ValueAnno"))
        println("A: " + m.annotations.head) //.tree.children.tail)
      m.name.toString.stripSuffix("_$eq") -> m.annotations.map { a =>
        a.tree.tpe.typeSymbol.name.toString -> a.tree.children.withFilter {
          _.productPrefix eq "AssignOrNamedArg"
        }.map { tree =>
          tree.productElement(0).toString -> tree.productElement(1)
        }.toMap
      }.toMap
    }.toMap
  }

}

//--------------------------

class Foo() {
  @TestAnno var a: Int = 4
  @ValueAnno(desc = "thing") var b: Int = 5
  var c: Int = 6
}

object Runme extends App {
  val f = new Foo()
  println(Inspector.methodAnnotations[Foo])
}
