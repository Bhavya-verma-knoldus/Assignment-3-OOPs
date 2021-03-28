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

package org.knoldus.repo

import org.knoldus.db.UserTable
import org.knoldus.models.{User, UserType}
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class UserRepoUnitTest extends AnyFlatSpec {

  val mockedUserTable: UserTable = mock[UserTable]
  val userRepo = new UserRepo(mockedUserTable)

  val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

  /*add method test cases*/
  "add" should "not add as userTable throws Exception" in {

    intercept[RuntimeException]{
      when(Await.result(mockedUserTable.add(user),5 seconds)) thenThrow(throw new RuntimeException("Invalid operation"))
    }

    assertThrows[RuntimeException](Await.result(userRepo.add(user),5 seconds))
  }

  it should "add the user" in {
    when(mockedUserTable.add(user)) thenReturn Future(Option(UUID.randomUUID()))

    val result = Await.result(userRepo.add(user),5 seconds)
    assert(result.nonEmpty)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "throw an exception when user id does not exists" in {

    intercept[RuntimeException]{
      when(Await.result(mockedUserTable.getById(user.id),5 seconds)) thenThrow(throw new NoSuchElementException)
    }

    assertThrows[RuntimeException](Await.result(userRepo.getById(user.id),5 seconds))
  }

  it should "return the user when user id exists" in {
    val uuid = UUID.randomUUID()
    when(mockedUserTable.getById(Some(uuid))) thenReturn Future(List(user))

    val result = Await.result(userRepo.getById(Some(uuid)),5 seconds)
    assert(result.nonEmpty)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is empty" in {

    when(mockedUserTable.getAll) thenReturn Future(List())

    val result = Await.result(userRepo.getAll,5 seconds)
    assert(result.isEmpty)
  }

  it should "return list of users when ListBuffer is not empty" in {
    when(mockedUserTable.getAll) thenReturn Future(List(user))

    val result = Await.result(userRepo.getAll,5 seconds)
    assert(result.nonEmpty)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not add as userTable returns false" in {
    when(mockedUserTable.update(user.id,user)) thenReturn Future(false)

    val result = Await.result(userRepo.update(user.id,user),5 seconds)
    assert(!result)
  }

  it should "add the user" in {
    when(mockedUserTable.update(user.id,user)) thenReturn Future(true)

    val result = Await.result(userRepo.update(user.id,user),5 seconds)
    assert(result)
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete the user when user id is not valid" in {

    val uuid = UUID.randomUUID()
    when(mockedUserTable.deleteById(Some(uuid))) thenReturn Future(false)

    val result = Await.result(userRepo.deleteById(Some(uuid)),5 seconds)
    assert(!result)
  }

  it should "delete the user when user id is valid" in {
    val uuid = UUID.randomUUID()
    when(mockedUserTable.deleteById(Some(uuid))) thenReturn Future(true)

    val result = Await.result(userRepo.deleteById(Some(uuid)),5 seconds)
    assert(result)
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/
  /*deleteAll method test cases*/
  "deleteAll" should "not delete all users as ListBuffer is empty" in {

    when(mockedUserTable.deleteAll()) thenReturn Future(false)

    val result = Await.result(userRepo.deleteAll(),5 seconds)
    assert(!result)
  }

  it should "delete all users as ListBuffer is not empty" in {
    when(mockedUserTable.deleteAll()) thenReturn Future(true)

    val result = Await.result(userRepo.deleteAll(),5 seconds)
    assert(result)
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/
}
