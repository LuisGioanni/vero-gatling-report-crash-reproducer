package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.{EditContacts, ListContacts}
import io.gatling.core.Predef.exec
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

object ChangeLoopAction {
  val action: ChainBuilder = exec(ListContacts.fetchRandomContactIds(1))
    .doIf(ListContacts.resultSaved) {
      exec(ListContacts.pickResult("userIdToDisconnect"))
        .pause(2 seconds, 5 seconds)
        .randomSwitch(
          33.3 -> exec(EditContacts.edit("${userIdToDisconnect}", "friends")),
          33.3 -> exec(EditContacts.edit("${userIdToDisconnect}", "acquaintances")),
          33.3 -> exec(EditContacts.edit("${userIdToDisconnect}", "closefriends"))
        )

    }
}
