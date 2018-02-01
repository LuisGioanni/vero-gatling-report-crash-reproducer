package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint
import com.ojingolabs.vero.test.utils.SimulationUtils._
import com.ojingolabs.vero.test.utils.Wamp._
import io.gatling.core.Predef._

object ViewProfile extends Endpoint("profile:view") {

  def fetch(profileId: String) = exec(wampCall("fetch contact profile", timeout, endpoint, Map("id" -> profileId))
    .matching(basicJsonPathMatch).check(basicJsonPathCheck))

  def fetchConnectable(profileId: String) = namedCall("fetch connectable profile", Map("id" -> profileId))
    .check(jsonPath("$[2][?(@.connectable == true)].connectable").find.optional.saveAs("connectableFeaturedUser"))

}
