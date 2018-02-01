package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder


object ReadActivities extends Endpoint("activity:read") {

  val readAll: ChainBuilder = tryMax(2)(
    call(Map("all" -> true))
  )
}
