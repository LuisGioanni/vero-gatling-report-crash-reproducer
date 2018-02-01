package com.ojingolabs.vero.test.subroutines.impl

import io.gatling.core.Predef._

object InitAction {

  def initAction = exec(ViewStream.latest(40), ListContacts.fetchContactIds, ListContactRequests.fetchContactRequestsIds, ListChats
      .call, ListFeaturedUsers.fetch, ViewActivities.call)

}
