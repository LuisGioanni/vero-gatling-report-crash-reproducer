package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint
import com.ojingolabs.vero.test.utils.SimulationUtils._
import com.ojingolabs.vero.test.utils.Wamp._
import io.gatling.core.Predef._

object GetProfile extends Endpoint("profile:get") {

  val fetch = exec(wampCall("fetch user profile", timeout, endpoint).matching(basicJsonPathMatch).check(basicJsonPathCheck))

}
