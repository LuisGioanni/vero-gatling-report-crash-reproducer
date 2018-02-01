package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint

object EditContacts extends Endpoint("contacts:edit") {

  def edit(contactId: String, loop: String) = call(Map("contact" -> contactId, "loop" -> loop))
}
