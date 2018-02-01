package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import io.gatling.core.Predef._

object ListContactRequests extends SavableResultEndpoint("contactrequest:list", "contactRequests") {

  def fetchContactRequestsIds = call.saveAll("$[2].items[*].id")

  def fetchRandomContactRequestId = call.saveRandom("$[2].items[*].id", 1)

  def fetchRandomContactRequestIdToAccept = exec(clearSavedResults)
      .exec(call.saveRandom("$[2].items[*][?(@.status != 'pending')].id", 1))

}
