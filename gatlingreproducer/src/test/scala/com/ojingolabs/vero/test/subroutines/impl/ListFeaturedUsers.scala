package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.conf.SimulationConfig._
import io.gatling.core.Predef._

object ListFeaturedUsers {

  def fetch = foreach(featuredUsers, "featuredUser") {
    exec(flattenMapIntoAttributes("${featuredUser}"))
      .exec(ViewProfile.fetch("${featuredUser.featuredId}"))
      .exec(StreamFilter.latest(2,"${featuredUser.featuredId}"))
  }

}
