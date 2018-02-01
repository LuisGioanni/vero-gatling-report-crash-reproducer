package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.SearchUser
import io.gatling.core.Predef.exec
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration.DurationInt

object SearchUserAction {

  val usersPattern1: String = "searchme"
  val usersPattern2: String = "connectme"
  val usersPattern3: String = "followme"

  val action: ChainBuilder = exec(SearchUser.findUsers(usersPattern1))
    .pause(3 seconds, 6 seconds).exec(SearchUser.findUsers(usersPattern1, 50))
    .pause(3 seconds, 6 seconds).exec(SearchUser.findUsers(usersPattern1, 100))
    .pause(3 seconds, 6 seconds).exec(SearchUser.findUsers(usersPattern1, 150))

  def actionWithFollowablePicked(saveAs: String)(thenNext: ChainBuilder): ChainBuilder = exec(SearchUser
    .findFollowableUsersAndSaveRandom(usersPattern3))
    .pause(3 seconds, 6 seconds)
    .doIf(SearchUser.resultSaved) {
      exec(SearchUser.pickResult(saveAs)).exec(thenNext)
    }

  def actionWithConnectablePicked(saveAs: String)(thenNext: ChainBuilder): ChainBuilder = exec(SearchUser
    .findConnectableUsersAndSaveRandom(usersPattern2))
    .pause(3 seconds, 6 seconds)
    .doIf(SearchUser.resultSaved) {
      exec(SearchUser.pickResult(saveAs)).exec(thenNext)
    }
}
