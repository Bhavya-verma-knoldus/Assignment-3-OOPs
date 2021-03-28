// Copyright (C) 2011-2012 the original author or authors.
// See the LICENCE.txt file distributed with this work for additional
// information regarding copyright ownership.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.knoldus.db

import org.knoldus.models.{User, UserType}
import org.scalatest.flatspec.AnyFlatSpec
import scala.concurrent.duration._

import java.util.UUID
import scala.concurrent.Await

class UserTableTest extends AnyFlatSpec {
  val userTable = new UserTable

  /*creating user with UUID*/
  val user: User = User(Some(UUID.randomUUID()),"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

  /*add method test cases*/
  "add" should "add the user" in {
    val id = Await.result(userTable.add(user),5 seconds)
    assert(Some(id).nonEmpty)
    Await.result(userTable.deleteById(id),5 seconds)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "not return the user when user id does not exists" in {
    assertThrows[RuntimeException](Await.result(userTable.getById(Some(UUID.randomUUID())), 5 seconds))
  }

  it should "return the user when user id exists" in {
    val id = Await.result(userTable.add(user),5 seconds)
    val result = Await.result(userTable.getById(id),5 seconds)
    assert(result.nonEmpty)
    Await.result(userTable.deleteById(id),5 seconds)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is be empty" in {
    val result = Await.result(userTable.getAll, 5 seconds)
    assert(result.isEmpty)
  }

  it should "be valid as ListBuffer should not be empty" in {
    val id = Await.result(userTable.add(user),5 seconds)
    val result = Await.result(userTable.getAll,5 seconds)
    assert(result.nonEmpty)
    Await.result(userTable.deleteById(id),5 seconds)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not update when user id does not exists" in {
    val result = Await.result(userTable.update(Some(UUID.randomUUID()),user),5 seconds)
    assert(!result)
  }

  it should "update when user id exists" in {
    val id = Await.result(userTable.add(user),5 seconds)
    val updatedUser: User = User(id,"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

    val result = Await.result(userTable.update(id,updatedUser), 5 seconds)
    assert(result)
    Await.result(userTable.deleteById(id),5 seconds)
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete when user id does not exists" in {
    val result = Await.result(userTable.deleteById(Some(UUID.randomUUID())), 5 seconds)
    assert(!result)
  }

  it should "delete the user when user id exists" in {
    val id = Await.result(userTable.add(user), 5 seconds)
    val result = Await.result(userTable.deleteById(id), 5 seconds)
    assert(result)
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteAll method test cases*/
  "deleteAll" should "not delete when no user exists" in {
    val result = Await.result(userTable.deleteAll(), 5 seconds)
    assert(!result)
  }

  it should "delete all users when users exists" in {
    Await.result(userTable.add(user), 5 seconds)
    val result = Await.result(userTable.deleteAll(), 5 seconds)
    assert(result)
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

}
