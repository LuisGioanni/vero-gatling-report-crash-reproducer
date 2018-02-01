package com.ojingolabs.vero.test.subroutines

import com.ojingolabs.vero.test.conf.SimulationConfig._
import com.ojingolabs.vero.test.utils.Wamp._
import io.gatling.commons.validation.{Failure, Success}
import io.gatling.core.Predef._
import io.gatling.core.json.JsonParsers
import io.gatling.core.session.Expression
import io.gatling.core.structure.ChainBuilder

import scala.concurrent.duration.FiniteDuration
import scala.util.Random


object SavableWampCall {

}

class SavableWampCall(wc: WampCall, saveAs: String, timeout: FiniteDuration) {

  def saveFirst(subQuery: String): WampCall = {
    wc.check(jsonPath(subQuery).find.transform(value => {
      JsonParsers().safeParse(value) match {
        case Success(obj) => obj
        case Failure(error) => error
      }
    }).optional.saveAs(saveAs))
  }


  def saveAll(subQuery: String): WampCall = {
    wc.check(jsonPath(subQuery).findAll.optional.saveAs(saveAs))
  }

  def saveRandom(subQuery: String, randomCount: Int): WampCall = {
    wc.check(jsonPath(subQuery).findRandom(randomCount).optional.saveAs(saveAs))
  }
}

class SavableResultEndpoint(endpoint: String, val saveAs: String, alias: Option[String] = None, timeout: FiniteDuration = UXTimeout) extends Endpoint(endpoint, alias, timeout) {

  implicit def toSavableWampCall(wc: WampCall): SavableWampCall =
    new SavableWampCall(wc, saveAs, timeout)

  def resultSaved: Expression[Boolean] = _.contains(saveAs)

  def clearSavedResults: Expression[Session] =
    _.remove(saveAs)

  def pickResult(name: String): Expression[Session] = session => {
    val results: Vector[Any] = session(saveAs).as[Vector[Any]]
    session.set(name, results(0))
  }

  def pickRandomResults(name: String, max: Int): Expression[Session] = session => {
    val results: Vector[Any] = session(saveAs).as[Vector[Any]]
    session.set(name, Random.shuffle(results).take(max))
  }

  def withOffset(chain: ChainBuilder) =
    exec { session =>
      for {
        res <- session(saveAs).validate[Seq[String]]
      } yield session.set("offset", res.last)
    }.exec(chain)
}
