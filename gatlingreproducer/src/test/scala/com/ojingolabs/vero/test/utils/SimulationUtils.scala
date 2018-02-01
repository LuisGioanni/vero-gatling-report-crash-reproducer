package com.ojingolabs.vero.test.utils

import java.util.{Map => JMap}

import io.gatling.commons.validation._
import io.gatling.core.Predef._

object SimulationUtils {


  def basicJsonPathMatch = jsonPath("$[0]").find.in("3","4")
  def basicJsonPathCheck = jsonPath("$[0]").find.is("3")

  implicit class PimpedOption[T](val opt: Option[T]) extends AnyVal {

    def mandatory(name: String): Validation[T] = opt match {
      case Some(value) => value.asInstanceOf[T].success
      case None => s"$name is missing".failure
    }
  }

  implicit class PimpedMap(val map: JMap[String, Any]) extends AnyVal {

    def mandatory[T](name: String): Validation[T] = Option(map.get(name)).map(_.asInstanceOf[T]).mandatory(name)
  }
}
