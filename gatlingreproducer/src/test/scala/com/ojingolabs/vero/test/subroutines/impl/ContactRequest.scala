package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.Endpoint

object ContactRequest extends Endpoint("contactrequest:connect") {

  def connect(contactId: String, loop: String) = call(Map("contact" -> contactId, "loop" -> loop))

}
