package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import io.gatling.core.Predef._

object StreamFilter extends SavableResultEndpoint("social:stream:filter", "Streams", Some("stream:filter")) {

  def latest(limit: Int, author: String) = call(Map("limit" -> limit, "author" -> author))
    //.check(jsonPath("$[2].typehints").find) removed for dummy purpose

  def latest(limit: Int, author: String, postType: String) =
    namedCall(endpoint + " with type filter",
      Map("limit" -> limit, "author" -> author, "type" -> postType))


  def latestAndSaveAll(limit: Int, author: String) = call(Map("limit" -> limit, "author" -> author))
    .check(jsonPath("$[2].typehints").find).saveAll("$[2].items[*].id")

  def latestAndSaveAll(limit: Int, author: String, postType: String) = call(Map("limit" -> limit, "author" -> author,
    "type" -> postType)).saveAll("$[2].items[*].id")

  def latestAndSaveRandom(limit: Int, author: String, randomCount: Int) = call(Map("limit" -> limit, "author" -> author))
    .check(jsonPath("$[2].typehints").find).saveRandom("$[2].items[*].id", randomCount)

  def latestAndSaveRandom(limit: Int, author: String, randomCount: Int, postType: String) = call(Map("limit" -> limit,
    "author" -> author, "type" -> postType)).saveRandom("$[2].items[*].id", randomCount)

  def withOlderOffsetAndSaveAll(limit: Int, author: String) =
    withOffset {
      exec(clearSavedResults)
        .exec(namedCall("social:stream:filter with offset", Map("after" -> "${offset}", "author" -> author,
          "limit" -> limit))
          .saveAll("$[2].items[*].id"))
    }

  def withOlderOffsetAndSaveAll(limit: Int, author: String, postType: String) =
    withOffset {
      exec(clearSavedResults)
        .exec(
          namedCall("social:stream:filter with offset and type", Map("after" -> "${offset}",
            "author" -> author, "limit" -> limit, "type" -> postType))
            .saveAll("$[2].items[*].id"))
    }

}
