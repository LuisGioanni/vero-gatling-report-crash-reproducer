package com.ojingolabs.vero.test.subroutines

import com.ojingolabs.vero.test.conf.SimulationConfig._
import com.ojingolabs.vero.test.utils.SimulationUtils._
import com.ojingolabs.vero.test.utils.Wamp._

import scala.concurrent.duration.FiniteDuration

class Endpoint(val endpoint: String, val alias: Option[String] = None, val timeout: FiniteDuration = UXTimeout) {

  val call = namedCall(endpoint)

  def call(parameters: Any*) = namedCall(endpoint, parameters: _*)

  def namedCall(requestName: String, parameters: Any*): WampCall = wampCall(requestName, timeout, endpoint, parameters:
    _*).matching(basicJsonPathMatch).check(basicJsonPathCheck)

}
