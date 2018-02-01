package com.ojingolabs.vero.test.subroutines.impl

import java.util.{Map => JMap}

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint
import io.gatling.core.Predef._
import io.gatling.core.session.Expression
import com.ojingolabs.vero.test.utils.SimulationUtils._

object CollectionEvents extends SavableResultEndpoint("social:collection:events", "CollectionsEvents") {

  def latestAndSaveAll(limit: Int) = call(Map("limit" -> limit)).saveAll("$[2].items[*]")

  def fetchAfterConnect(userId: String, limit: Int) =
    call(Map("limit" -> limit, "newer" -> 0))
      .saveFirst("$[2].items[*][?(@.type == '" + CollectionEventsType.connect + "')][?(@.actor == '" + userId + "')]")

  def fetchBootstrap(limit: Int) =
    call(Map("limit" -> limit, "newer" -> 0))
      .saveFirst("$[2].items[*][?(@.type == '" + CollectionEventsType.bootstrap + "')]")

  def pickEventId(name: String): Expression[Session] = session => {
    for {
      collections <- session(saveAs).validate[JMap[String, Any]]
      eventId <- collections.mandatory[String]("id")
    } yield {
      session.set(name, eventId)
    }
  }

}

object CollectionEventsType {
  val bootstrap = "bootstrap"
  val connect = "connect"
  val disconnect = "disconnect"
  val refresh = "refresh"
  val removed = "removed"
  val added = "added"
}