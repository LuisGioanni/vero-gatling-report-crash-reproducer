package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint
import io.gatling.core.Predef._


object SearchTag extends Endpoint("social:suggest#tag") {

  def findAtLeastOne(tag: String) = call(Map("value" -> tag)).check(jsonPath("$[2].items[*]").count.greaterThan(0))

}
