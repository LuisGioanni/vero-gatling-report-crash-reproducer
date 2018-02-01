package com.ojingolabs.vero.test.subroutines.impl

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

import scala.concurrent.duration._

object Connection {

  def connect(username: String, password: String) =
    ws2("start session").connect("/wamp").wait(2 seconds) {
      ws2.checkTextMessage("start session check").check(regex(".*Vero.*").exists)
    }.onConnected(
      Authentication.login(username, password)
    )

  val disconnect: ChainBuilder = exec(Authentication.logout)
    .exec(ws2("Close WS").close)

}