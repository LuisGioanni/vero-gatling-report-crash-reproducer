package com.ojingolabs.vero.test.actions

import com.ojingolabs.vero.test.conf.SimulationConfig._
import com.ojingolabs.vero.test.subroutines.impl.{StreamFilter, ViewProfile}
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration._


object ViewProfileAction {

  val featuredUser: ChainBuilder = feed(userToView).exec(action("featuredId"))

  def featuredUser(postType: String): ChainBuilder = feed(userToView).exec(action("featuredId", postType))

  def action(userIdSessionKey: String): ChainBuilder =
    exec(ViewProfile.fetch("${" + userIdSessionKey + "}"))
      .exec(StreamFilter.latestAndSaveAll(40, "${" + userIdSessionKey + "}"))
      .pause(3 seconds, 8 seconds)
      .repeat(2) {
        doIf(StreamFilter.resultSaved) {
          exec(StreamFilter.withOlderOffsetAndSaveAll(20, "${" + userIdSessionKey + "}"))
            .pause(3 seconds, 7 seconds)
        }
      }


  def action(userIdSessionKey: String, postType: String): ChainBuilder = exec(action(userIdSessionKey))
    .exec(StreamFilter.latest(40, userIdSessionKey, postType), pause(5 seconds, 10 seconds))


  val checkConnectableFeaturedUser: ChainBuilder = feed(userToView)
    .exec(ViewProfile.fetchConnectable("${featuredId}"))
    .exec(StreamFilter.latestAndSaveAll(40, "${featuredId}"))
    .pause(3 seconds, 8 seconds)
    .repeat(2) {
      doIf(StreamFilter.resultSaved) {
        exec(StreamFilter.withOlderOffsetAndSaveAll(20, "${featuredId}"))
          .pause(3 seconds, 7 seconds)
      }
    }

}
