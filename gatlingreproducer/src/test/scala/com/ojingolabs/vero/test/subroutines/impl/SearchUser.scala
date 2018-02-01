package com.ojingolabs.vero.test.subroutines.impl

import com.ojingolabs.vero.test.subroutines.SavableResultEndpoint


object SearchUser extends SavableResultEndpoint("profile:search", "UsersSearch") {

  def findUsers(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))

  def findUsersAndSaveAll(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))
    .saveAll("$[2].items[*].uid")

  def findConnectableUsersAndSaveAll(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))
    .saveAll("$[2].items[*][?(@.connectable == true)].uid")

  def findConnectableUsersAndSaveRandom(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))
    .saveRandom("$[2].items[*][?(@.connectable == true)].uid", 1)

  def findFollowableUsersAndSaveAll(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))
    .saveAll("$[2].items[*][?(@.followable == true)].uid")

  def findFollowableUsersAndSaveRandom(name: String, offset: Int = 0) = call(Map("name" -> name, "offset" -> offset))
    .saveRandom("$[2].items[*][?(@.followable == true)].uid", 1)

}
