package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.subroutines.impl.ViewStream
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._


object ViewStreamAction {

  val action: ChainBuilder = exec(ViewStream.latestAndSaveAll(40))
    .pause(3 seconds, 6 seconds)
    .repeat(2) {
      doIf(ViewStream.resultSaved) {
        exec(ViewStream.withOlderOffsetAndSaveAll(20))
          .pause(3 seconds, 6 seconds)
      }
    }
}
