package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import com.ojingolabs.vero.test.utils.Wamp.WampCall
import io.gatling.core.Predef.exec

object ListFollowing extends SavableResultEndpoint("social:following:view", "Following") {

  def latest(userId: String, limit: Int): WampCall = call(Map("id" -> userId, "limit" -> limit))

  def latestAndSaveAll(userId: String, limit: Int) = call(Map("id" -> userId, "limit" -> limit)).saveAll("$[2]" +
    ".items[*].id")

  def latestAndSaveRandom(userId: String, limit: Int, randomCount: Int) = call(Map("id" -> userId, "limit" -> limit))
    .saveRandom("$[2].items[*].id", randomCount)

  def withAfterOffsetAndSaveAll(userId: String, limit: Int) =
    withOffset {
      exec(clearSavedResults)
        .exec(namedCall("social:stream:view with offset", Map("id" -> userId, "after" -> "${offset}", "limit" ->
          limit)).saveAll("$[2].items[*].id"))
    }
}
