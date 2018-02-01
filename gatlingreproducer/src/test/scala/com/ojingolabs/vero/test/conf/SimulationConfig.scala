package com.ojingolabs.vero.test.conf

import io.gatling.core.Predef._
import io.gatling.core.feeder.{Feeder, Record, SourceFeederBuilder}
import io.gatling.http.Predef._

import scala.collection.breakOut
import scala.concurrent.duration._
import scala.util.Random

object SimulationConfig {

  def httpConfig(baseUrls: List[String]) = http
    .baseURLs(baseUrls.map(baseUrl => "http://" + baseUrl))
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Gatling2")
    .wsBaseURLs(baseUrls.map(baseUrl => "ws://" + baseUrl))

  val maxUsers: Int = System.getProperty("simulation.users.absoluteMax", "2000").toInt
  val rampUsersMin: Double = System.getProperty("simulation.users.min", "0.5").toDouble
  val rampUsersMax: Double = System.getProperty("simulation.users.max", "1.5").toDouble
  val rampUsersMaxBurst: Double = System.getProperty("simulation.users.maxBurst", "10").toDouble
  val rampDuration: Int = System.getProperty("simulation.duration", "10").toInt
  val rampBurstDuration: Int = System.getProperty("simulation.burst.duration", "2").toInt

  val authTimeout: FiniteDuration = Integer.parseInt(System.getProperty("authTimeout", "5")) seconds
  val UXTimeout: FiniteDuration = Integer.parseInt(System.getProperty("UXTimeout", "5")) seconds

  //val baseUrls: List[String] = removed for reproducer

  val users_file: String = System.getProperty("usersFile", "users_stg.json")
  val users: SourceFeederBuilder[Any] = jsonFile(users_file).random
  val new_users: SourceFeederBuilder[Any] = jsonFile("new-users.json").random
  val followables: SourceFeederBuilder[Any] = jsonFile("followables.json").random
  val usersWithManyPosts: SourceFeederBuilder[Any] = jsonFile("usersWithManyPosts.json").random
  val featuredUsers: Seq[Record[Any]] = jsonFile("featuredUsers.json").readRecords
  val userToView: SourceFeederBuilder[Any] = jsonFile("featuredUsers.json").random

  val randomTagsEnabled: Boolean = System.getProperty("simulation.posts.randomTags", "false").toBoolean
  val randomTags: Iterator[Map[String, String]] = Iterator.continually(Map("tag" -> Random.alphanumeric.take(10).mkString))
  val listedTags: SourceFeederBuilder[Any] = jsonFile("tags.json").random
  val tags: Feeder[Any] = if (randomTagsEnabled) randomTags else listedTags.apply()
  val mentionsTagsCount: Int = System.getProperty("simulation.posts.mentionsTagsCount", "5").toInt
  val mentionsEnabled: Boolean = System.getProperty("simulation.posts.hasMentions", "true").toBoolean
  val tagsEnabled: Boolean = System.getProperty("simulation.posts.hasTags", "false").toBoolean
  val tagsOrMentionsEnabled: Boolean = tagsEnabled || mentionsEnabled
  val filterPostWithType: Boolean = System.getProperty("simulation.stream.filter.withType", "false").toBoolean
  val collectionsFetchLimit: Int = 200

  val postTypeFeeder = Array(
    Map("postType" -> "movie"),
    Map("postType" -> "link"),
    Map("postType" -> "photo"),
    Map("postType" -> "video"),
    Map("postType" -> "place"),
    Map("postType" -> "music"),
    Map("postType" -> "book"),
    Map("postType" -> "product"),
    Map("postType" -> "person")).random
}