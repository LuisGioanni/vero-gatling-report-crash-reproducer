package com.ojingolabs.vero.test.utils

import io.gatling.core.Predef._
import io.gatling.core.check.{Check, CheckBuilder}
import io.gatling.core.check.extractor.jsonpath.JsonPathCheckType
import io.gatling.core.json.Json
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import io.gatling.http.action.ws2.{WsCheckSequence, WsSendBuilder, WsTextCheck}

import scala.concurrent.duration._

object Wamp {

  object WampCall {

    implicit def asWsSendBuilder(call: WampCall): ChainBuilder =
      exec(_.set("wampId", generateWampId))
        .exec(call.req)
  }

  class WampCall(val req: WsSendBuilder) {

    def check(checkBuilder: CheckBuilder[JsonPathCheckType, Any, _]): WampCall =
      new WampCall(
        WsSendBuilder(req.requestName, req.wsName, req.message, req.checkSequences.map(sequence =>
          WsCheckSequence(sequence.timeout, sequence.checks.map(check => check.check(checkBuilder)))))
      )

    def matching(checkBuilder: CheckBuilder[JsonPathCheckType, Any, _]):
    WampCall =
      new WampCall(
        WsSendBuilder(req.requestName, req.wsName, req.message, req.checkSequences.map(sequence =>
          WsCheckSequence(sequence.timeout, sequence.checks.map(check => check.matching(checkBuilder)))))
      )
  }

  private def generateWampId = System.nanoTime.toHexString

  def wampCall(requestName: String, timeout: FiniteDuration, endpoint: String, args: Any*)
  : WampCall =
    new WampCall(ws2(requestName).sendText(Json.stringify(Array(2, "${wampId}", endpoint) ++ args)).wait(timeout)(
      ws2.checkTextMessage(requestName + " check")
    ))
}
