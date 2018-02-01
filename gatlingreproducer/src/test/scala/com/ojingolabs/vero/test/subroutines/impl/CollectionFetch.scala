package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import io.gatling.core.Predef.exec

object CollectionFetch extends SavableResultEndpoint("social:collection:fetch", "CollectionsFetch") {

  def fetchEventAndSaveAll(eventId: String, limit: Int) = call(Map("limit" -> limit, "event" -> eventId)).saveAll("$[2].items[*].id")

  def withOlderOffsetAndSaveAll(eventId: String, limit: Int) =
    withOffset {
      exec(clearSavedResults)
        .exec(namedCall("social:collection:fetch with offset", Map("older" -> "${offset}", "limit" -> limit, "event" -> eventId)).saveAll
        ("$[2].items[*].id"))
    }


}