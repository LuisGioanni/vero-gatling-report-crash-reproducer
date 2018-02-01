package com.ojingolabs.vero.test

import java.util.UUID
import java.util.concurrent.ThreadLocalRandom

import com.ojingolabs.vero.test.actions._
import com.ojingolabs.vero.test.conf.SimulationConfig._
import com.ojingolabs.vero.test.subroutines.impl._
import io.gatling.core.scenario.Simulation
import io.gatling.core.Predef._
import io.gatling.core.session.Session
import io.gatling.commons.validation._
import io.gatling.core.structure.ScenarioBuilder

import scala.concurrent.duration._

class ReproducerSimulation extends Simulation {

  val basicScenario: ScenarioBuilder = scenario("Reproducer")
    .feed(users)
    .exec(Connection.connect("login", "password"))
    .exec(InitAction.initAction)
    .exec(Connection.disconnect)


  setUp(basicScenario
    .inject(
      //atOnceUsers(1))
      constantUsersPerSec(1).during(10 seconds).randomized)
    .protocols(httpConfig(List("localhost:8080"))))

}
