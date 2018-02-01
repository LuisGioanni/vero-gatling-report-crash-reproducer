package com.ojingolabs.vero.test.subroutines.impl

import java.math.BigInteger
import java.util.{Map => JMap}

import com.ojingolabs.vero.test.conf.SimulationConfig.authTimeout
import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import com.ojingolabs.vero.test.utils.SimulationUtils._
import com.ojingolabs.vero.test.utils.Srp
import com.ojingolabs.vero.test.utils.Wamp.wampCall
import io.gatling.core.Predef._
import org.bouncycastle.util.encoders.Hex

object Authentication extends SavableResultEndpoint("auth:req", "auth1", None, authTimeout) {

  def login(username: String, password: String) =
    exec { session =>
      session.markAsSucceeded
    }.exec(call("test", "test").saveFirst("$[2]"))
      .exitHereIfFailed
      .exec(
        wampCall("complete auth", authTimeout, "auth", "key")
          .matching(basicJsonPathMatch).check(basicJsonPathCheck).check(jsonPath("$[2].id").find.saveAs("user_id"))
      ).exitHereIfFailed

  def logout = wampCall("logout", authTimeout, "logout").matching(basicJsonPathMatch).check(basicJsonPathCheck)
}
