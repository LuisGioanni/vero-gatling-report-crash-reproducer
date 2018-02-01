package com.ojingolabs.vero.test.subroutines.domain

import java.util.concurrent.ThreadLocalRandom

import scala.util.Random

import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import io.gatling.core.structure.ChainBuilder

object Loop {

  private def setRandomLoop: Expression[Session] = session => {
    val loop = ThreadLocalRandom.current.nextInt(3) match {
      case 0 => "closefriends"
      case 1 => "friends"
      case _ => "acquaintances"
    }
    session.set("loop", loop)
  }

  def withRandomLoop(chain: ChainBuilder) =
    exec(setRandomLoop)
    .exec(chain)
}
