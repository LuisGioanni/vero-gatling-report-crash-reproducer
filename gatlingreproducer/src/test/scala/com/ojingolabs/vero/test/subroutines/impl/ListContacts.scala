package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint

object ListContacts extends SavableResultEndpoint("contacts:list", "Contacts") {

  def fetchContactIds = call.saveAll("$[2].items[*].id")

  def fetchRandomContactIds(randomCount: Int) = call.saveRandom("$[2].items[*].id", randomCount)

}
