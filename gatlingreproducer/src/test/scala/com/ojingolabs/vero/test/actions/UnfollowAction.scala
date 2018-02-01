package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.{ListFollowing, UnFollow}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

object UnfollowAction {
  val action: ChainBuilder = exec(ListFollowing.latestAndSaveRandom("${user_id}", 50, 1))
    .doIf(ListFollowing.resultSaved) {
      exec(ListFollowing.pickResult("toUnfollowId"))
        .pause(2 seconds, 5 seconds)
        .exec(UnFollow.call(Map("id" -> "${toUnfollowId}")))
    }

}
