package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.{LikePost, ViewStream}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object LikesAction {
  val action: ChainBuilder = doIf(ViewStream.resultSaved) {
    exec(ViewStream.pickResult("postId"))
      .exec(LikePost.call(Map("post" -> "${postId}")))
  }

}
