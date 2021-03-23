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

import java.util.UUID

class UserDbTest extends AnyFlatSpec {

  val userDb = new UserDb

  /*creating user with UUID*/
  val user: User = User(Some(UUID.randomUUID()),"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

  /*add method test cases*/

  "add" should "add the user" in {
    val id = userDb.add(user)
    assert(Some(id).nonEmpty)
    userDb.deleteById(id)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "not return the user when user id does not exists" in {
    assertThrows[RuntimeException](userDb.getById(UUID.randomUUID()))
  }

  it should "return the user when user id exists" in {
    val id = userDb.add(user)
    assert(userDb.getById(id).nonEmpty)
    userDb.deleteById(id)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is be empty" in {
    assert(userDb.getAll.isEmpty)
  }

  it should "be valid as ListBuffer should not be empty" in {
    val id = userDb.add(user)
    assert(userDb.getAll.nonEmpty)
    userDb.deleteById(id)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not update when user id does not exists" in {
    assert(!userDb.update(Some(UUID.randomUUID()),user))
  }

  it should "update when user id exists" in {
    val id = userDb.add(user)
    val updatedUser: User = User(Some(id),"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

    assert(userDb.update(Some(id),updatedUser))
    userDb.deleteById(id)
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete when user id does not exists" in {
    assert(!userDb.deleteById(UUID.randomUUID()))
  }

  it should "delete the user when user id exists" in {
    val id = userDb.add(user)
    assert(userDb.deleteById(id))
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteAll method test cases*/
  "deleteAll" should "not delete when no user exists" in {
    assert(!userDb.deleteAll())
  }

  it should "delete all users when users exists" in {
    userDb.add(user)
    assert(userDb.deleteAll())
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/
}
