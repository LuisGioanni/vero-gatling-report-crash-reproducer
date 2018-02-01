package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.{DisconnectRequest, ListContacts}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object DisconnectAction {
  val action: ChainBuilder = exec(ListContacts.fetchRandomContactIds(1))
    .doIf(ListContacts.resultSaved){
      exec(ListContacts.pickResult("userIdToDisconnect"))
        .exec(DisconnectRequest.call(Map("contact" -> "${userIdToDisconnect}")))
    }

}
