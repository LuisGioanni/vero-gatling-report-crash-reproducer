package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import io.gatling.core.Predef._

object ViewStream extends SavableResultEndpoint("social:stream:view", "Streams", Some("stream:view")) {

  def latest(limit: Int) = call(Map("limit" -> limit))

  def latestAndSaveAll(limit: Int) = call(Map("limit" -> limit)).saveAll("$[2].items[*].id")

  def latestAndSaveRandom(limit: Int, randomCount: Int) = call(Map("limit" -> limit)).saveRandom("$[2].items[*].id", randomCount)

  def withOlderOffsetAndSaveAll(limit: Int) =
    withOffset {
      exec(clearSavedResults)
        .exec(namedCall("social:stream:view with offset", Map("after" -> "${offset}", "limit" -> limit)).saveAll
        ("$[2].items[*].id"))
    }
}
