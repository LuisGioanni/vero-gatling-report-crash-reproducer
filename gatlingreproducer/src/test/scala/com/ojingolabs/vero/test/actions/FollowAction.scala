package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.Follow
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._

object FollowAction {
  val action: ChainBuilder = randomSwitch(
    60.0 -> ViewProfileAction.featuredUser
      .pause(2 seconds, 5 seconds)
      .exec(Follow.call(Map("id" -> "${featuredId}"))),
    10.0 -> ViewProfileAction.action("f_user_id")
      .pause(2 seconds, 5 seconds)
      .exec(Follow.call(Map("id" -> "${f_user_id}"))),
    30.0 -> SearchUserAction.actionWithFollowablePicked("followableId")(
      pause(2 seconds, 5 seconds)
        .exec(Follow.call(Map("id" -> "${followableId}")))
    )
  )
}
