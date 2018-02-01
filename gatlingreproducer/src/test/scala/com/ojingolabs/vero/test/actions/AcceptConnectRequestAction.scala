package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.{AcceptConnectRequest, ListContactRequests}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object AcceptConnectRequestAction {
  val action: ChainBuilder = exec(ListContactRequests.fetchRandomContactRequestIdToAccept)
    .doIf(ListContactRequests.resultSaved) {
      exec(ListContactRequests.pickResult("userToConnectToId"))
        .exec(AcceptConnectRequest.call(Map("contact" -> "${userToConnectToId}")))
    }

}
