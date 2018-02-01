package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.conf.SimulationConfig.tagsOrMentionsEnabled
import com.ojingolabs.vero.test.subroutines.impl.PostLink
import com.ojingolabs.vero.test.utils.MentionsTagsUtil
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

object PostAction {



  val action: ChainBuilder = doIfOrElse(tagsOrMentionsEnabled) {
    PostLink.call(Map("url" -> "http://www.vero.co/",
      "details" -> "Home", "loop" -> "public",
      "image" -> Map("url" -> "ce0a9f50-5423-11e6-8905-67a87137a0e6/7625fd9a-091e-4b26-b6b6-99dc5972a150", "width" -> 720, "height" -> 448),
      "text" -> "We believe that social means interacting with people you know...",
      "caption" -> (MentionsTagsUtil.mentions ++ MentionsTagsUtil.tags)
    ))
  } {
    PostLink.call(Map("url" -> "http://www.vero.co/",
      "details" -> "Home", "loop" -> "public",
      "image" -> Map("url" -> "ce0a9f50-5423-11e6-8905-67a87137a0e6/7625fd9a-091e-4b26-b6b6-99dc5972a150", "width" -> 720, "height" -> 448),
      "text" -> "We believe that social means interacting with people you know..."))
  }

}
