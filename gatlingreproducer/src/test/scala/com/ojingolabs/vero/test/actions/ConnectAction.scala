package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.ContactRequest
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

object ConnectAction {
  val action: ChainBuilder = randomSwitch(
    60.0 -> doWhile(!_.contains("connectableFeaturedUser")) {
      ViewProfileAction.checkConnectableFeaturedUser
    }
      .exec(_.remove("connectableFeaturedUser"))
      .pause(2 seconds, 5 seconds)
      .exec(ContactRequest.connect("${featuredId}", "acquaintances")),
    40.0 -> SearchUserAction.actionWithConnectablePicked("connectableId")(
      pause(2 seconds, 5 seconds)
        .exec(ContactRequest.connect("${connectableId}", "acquaintances"))
    )
  )

}
