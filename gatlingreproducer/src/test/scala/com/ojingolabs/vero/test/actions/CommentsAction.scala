package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.conf.SimulationConfig.tagsOrMentionsEnabled
import com.ojingolabs.vero.test.subroutines.impl.{CreateComment, ViewStream}
import com.ojingolabs.vero.test.utils.MentionsTagsUtil
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder


object CommentsAction {

  val action: ChainBuilder =
    doIf(ViewStream.resultSaved) {
      exec(ViewStream.pickResult("postId"))
        .doIfOrElse(tagsOrMentionsEnabled) {
          CreateComment.call(Map("post" -> "${postId}",
            "content" -> (MentionsTagsUtil.mentions ++ MentionsTagsUtil.tags)))
        } {
          CreateComment.call(Map("post" -> "${postId}",
            "body" -> "No mentions or tags here"))
        }
    }

}
