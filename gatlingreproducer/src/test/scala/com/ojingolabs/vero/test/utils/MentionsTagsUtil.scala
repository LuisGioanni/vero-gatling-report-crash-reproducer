package com.ojingolabs.vero.test.utils

import com.ojingolabs.vero.test.conf.SimulationConfig._

object MentionsTagsUtil {

  val mentions: Array[Map[String, String]] = {
    if (mentionsEnabled) {
      (1 to mentionsTagsCount).toArray.map(elem => {
        Map("type" -> "person", "label" -> ("mention" + elem.toString), "value" -> (("${uid" + elem.toString) + "}") )
      })
    } else {
      Array()
    }
  }

  val tags: Array[Map[String, String]] = {
    if (tagsEnabled) {
      (1 to mentionsTagsCount).toArray.map(elem => {
        Map("type" -> "tag", "value" -> (("${tag" + elem.toString) + "}") )
      })
    } else {
      Array()
    }
  }
}
