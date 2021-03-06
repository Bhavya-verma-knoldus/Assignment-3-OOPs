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

package org.knoldus.operations

import org.knoldus.db.UserTable
import org.knoldus.repo.UserRepo
import org.knoldus.models.{User, UserType}
import org.knoldus.validator.{EmailValidator, MobileNoValidator}
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID
import scala.concurrent.Await
import scala.concurrent.duration._

class UserOperationsIntegrationTest extends AnyFlatSpec {

  val emailValidator = new EmailValidator
  val mobileValidator = new MobileNoValidator
  val userTable = new UserTable
  val userRepo = new UserRepo(userTable)
  val userOperations = new UserOperations(userRepo,emailValidator,mobileValidator)

  /*add method test cases*/
  "add" should "not add as mobile no and email id both are invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail",mobileNo = 999966,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as mobile no is invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 999966,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as email id is invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail",mobileNo = 9999666658L,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as email id and mobile no are valid but user object is sent with UUID" in {

    val user: User = User(id = Some(UUID.randomUUID()) ,userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "add the user" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

    val id = Await.result(userOperations.add(user),5 seconds)
    assert(Some(id).nonEmpty)
    Await.result(userOperations.deleteById(id),5 seconds)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "throw an exception when user id does not exists" in {

    assertThrows[RuntimeException](Await.result(userOperations.getById(Some(UUID.randomUUID())),5 seconds))
  }

  it should "return the user when user id exists" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
    val id = Await.result(userOperations.add(user),5 seconds)

    val result = Await.result(userOperations.getById(id),5 seconds)
    assert(result.nonEmpty)
    Await.result(userOperations.deleteById(id),5 seconds)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is empty" in {
    val result = Await.result(userOperations.getAll,5 seconds)
    assert(result.isEmpty)
  }

  it should "return list of users when ListBuffer is not empty" in {
    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
    val id = Await.result(userOperations.add(user),5 seconds)

    val result = Await.result(userOperations.getAll,5 seconds)
    assert(result.nonEmpty)
    Await.result(userOperations.deleteById(id),5 seconds)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not update as mobile no and email id both are invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail",mobileNo = 999966,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as mobile no is invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 999966,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as email id is invalid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail",mobileNo = 9999666658L,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as email id and mobile no are valid but update throws exception" in {

    val user: User = User(id = Some(UUID.randomUUID()) ,userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.update(user.id,user),5 seconds))
  }

  it should "update the user" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
    val id = Await.result(userOperations.add(user),5 seconds)

    val updatedUser: User = User(userName = "Bhanu",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

    val result = Await.result(userOperations.update(id,updatedUser),5 seconds)
    assert(result)
    Await.result(userOperations.deleteById(id),5 seconds)
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete the user when user id is not valid" in {
    val result = Await.result(userOperations.deleteById(Some(UUID.randomUUID())),5 seconds)
    assert(!result)
  }

  it should "delete the user when user id is valid" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
    val id = Await.result(userOperations.add(user),5 seconds)

    val result = Await.result(userOperations.deleteById(id),5 seconds)
    assert(result)
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteAll method test cases*/
  "deleteAll" should "not delete all users as ListBuffer is empty" in {

    val result = Await.result(userOperations.deleteAll(),5 seconds)
    assert(!result)
  }

  it should "delete all users as ListBuffer is not empty" in {

    val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
    Await.result(userOperations.add(user),5 seconds)

    val result = Await.result(userOperations.deleteAll(),5 seconds)
    assert(result)
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/
}
