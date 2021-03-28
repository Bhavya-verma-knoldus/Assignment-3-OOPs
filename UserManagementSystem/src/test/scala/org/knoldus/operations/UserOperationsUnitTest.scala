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

import org.knoldus.models.{User, UserType}
import org.knoldus.repo.UserRepo
import org.knoldus.validator.{EmailValidator, MobileNoValidator}
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID
import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

class UserOperationsUnitTest extends AnyFlatSpec {

  val mockedEmailValidator: EmailValidator = mock[EmailValidator]
  val mockedMobileNoValidator: MobileNoValidator = mock[MobileNoValidator]
  val mockedUserRepo: UserRepo = mock[UserRepo]

  val userOperations = new UserOperations(mockedUserRepo,mockedEmailValidator,mockedMobileNoValidator)

  val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

  /*add method test cases*/
  "add" should "not add as mobile no and email id both are invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as mobile no is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as email id is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "not add as email id and mobile no are valid but user object is sent with uuid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    val userWithId: User = User(Some(UUID.randomUUID()),"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.add(userWithId),5 seconds))
  }

  it should "not add as email id and mobile no are valid but userRepo throws exception" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    intercept[RuntimeException]{
      when(mockedUserRepo.add(user)) thenThrow(throw new RuntimeException("Invalid operation"))
    }

    assertThrows[RuntimeException](Await.result(userOperations.add(user),5 seconds))
  }

  it should "add the user" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true
    when(mockedUserRepo.add(user)) thenReturn Future(Option(UUID.randomUUID()))

    val result = userOperations.add(user)
    assert(Some(result).nonEmpty)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "throw an exception when user id does not exists" in {

    intercept[RuntimeException]{
      when(mockedUserRepo.getById(user.id)) thenThrow(throw new RuntimeException)
    }

    assertThrows[RuntimeException](Await.result(userOperations.getById(user.id),5 seconds))
  }

  it should "return the user when user id exists" in {
    val uuid = UUID.randomUUID()
    when(mockedUserRepo.getById(Some(uuid))) thenReturn Future(List(user))

    val result = Await.result(userOperations.getById(Some(uuid)),5 seconds)
    assert(result.nonEmpty)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is empty" in {

    when(mockedUserRepo.getAll) thenReturn Future(List())

    val result = Await.result(userOperations.getAll,5 seconds)
    assert(result.isEmpty)
  }

  it should "return list of users when ListBuffer is not empty" in {
    when(mockedUserRepo.getAll) thenReturn Future(List(user))

    val result = Await.result(userOperations.getAll,5 seconds)
    assert(result.nonEmpty)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not update as mobile no and email id both are invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as mobile no is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as email id is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(UUID.randomUUID()),user),5 seconds))
  }

  it should "not update as email id and mobile no are valid but user object is sent with UUID" in {

    val uuid = UUID.randomUUID()
    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    val userWithId: User = User(Some(UUID.randomUUID()),"Bhavya",UserType.Admin,"bhavya1234",24,"bhavya@gmail.com",9999666658L,Some("Shahdara"))

    assertThrows[RuntimeException](Await.result(userOperations.update(Some(uuid),userWithId),5 seconds))
  }

  it should "not update as email id and mobile no are valid but update returns false" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    when(mockedUserRepo.update(user.id,user)) thenReturn Future(false)

    val result = Await.result(userOperations.update(user.id,user), 5 seconds)
    assert(!result)
  }

  it should "update the user" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true
    when(mockedUserRepo.update(user.id,user)) thenReturn Future(true)

    val result = Await.result(userOperations.update(user.id,user), 5 seconds)
    assert(result)
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete the user when user id is not valid" in {

    val uuid = UUID.randomUUID()
    when(mockedUserRepo.deleteById(Some(uuid))) thenReturn Future(false)

    val result = Await.result(userOperations.deleteById(Some(uuid)),5 seconds)
    assert(!result)
  }

  it should "delete the user when user id is valid" in {
    val uuid = UUID.randomUUID()
    when(mockedUserRepo.deleteById(Some(uuid))) thenReturn Future(true)

    val result = Await.result(userOperations.deleteById(Some(uuid)),5 seconds)
    assert(result)
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteAll method test cases*/
  "deleteAll" should "not delete all users as ListBuffer is empty" in {

    when(mockedUserRepo.deleteAll()) thenReturn Future(false)

    val result = Await.result(userOperations.deleteAll(),5 seconds)
    assert(!result)
  }

  it should "delete all users as ListBuffer is not empty" in {
    when(mockedUserRepo.deleteAll()) thenReturn Future(true)

    val result = Await.result(userOperations.deleteAll(),5 seconds)
    assert(result)
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/
}
