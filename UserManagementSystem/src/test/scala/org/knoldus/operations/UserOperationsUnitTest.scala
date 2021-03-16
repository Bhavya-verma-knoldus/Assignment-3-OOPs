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

import org.knoldus.db.UserDb
import org.knoldus.models.{User, UserType}
import org.knoldus.validator.{EmailValidator, MobileNoValidator}
import org.mockito.MockitoSugar.{mock, when}
import org.scalatest.flatspec.AnyFlatSpec

import java.util.UUID

class UserOperationsUnitTest extends AnyFlatSpec {

  /*mocking all the dependent classes*/
  val mockedEmailValidator: EmailValidator = mock[EmailValidator]
  val mockedMobileNoValidator: MobileNoValidator = mock[MobileNoValidator]
  val mockedUserDb: UserDb = mock[UserDb]

  val userOperations = new UserOperations(mockedUserDb,mockedEmailValidator,mockedMobileNoValidator)

  val user: User = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

  /*add method test cases*/
  "add" should "not add as mobile no and email id both are invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[IllegalArgumentException](userOperations.add(user))
  }

  it should "not add as mobile no is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[IllegalArgumentException](userOperations.add(user))
  }

  it should "not add as email id is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    assertThrows[IllegalArgumentException](userOperations.add(user))
  }

  it should "not add as email id and mobile no are valid but userDb throws exception" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    intercept[RuntimeException]{
      when(mockedUserDb.add(user)) thenThrow(throw new RuntimeException("Invalid operation"))
    }

    assertThrows[RuntimeException](userOperations.add(user))
  }

  it should "add the user" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true
    when(mockedUserDb.add(user)) thenReturn UUID.randomUUID()

    val result = userOperations.add(user)
    assert(Some(result).nonEmpty)
  }
  /*add method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getById method test cases*/
  "getById" should "throw an exception when user id does not exists" in {

    intercept[NoSuchElementException]{
      when(mockedUserDb.getById(user.id.get)) thenThrow(throw new NoSuchElementException)
    }

    assertThrows[NoSuchElementException](userOperations.getById(user.id.get))
  }

  it should "return the user when user id exists" in {
    val uuid = UUID.randomUUID()
    when(mockedUserDb.getById(uuid)) thenReturn List(user)

    assert(userOperations.getById(uuid).nonEmpty)
  }
  /*getById method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*getAll method test cases*/
  "getAll" should "return empty list when ListBuffer is empty" in {

    when(mockedUserDb.getAll) thenReturn List()

    assert(userOperations.getAll.isEmpty)
  }

  it should "return list of users when ListBuffer is not empty" in {
    when(mockedUserDb.getAll) thenReturn List(user)

    assert(userOperations.getAll.nonEmpty)
  }
  /*getAll method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*update method test cases*/
  "update" should "not update as mobile no and email id both are invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[IllegalArgumentException](userOperations.update(UUID.randomUUID(),user))
  }

  it should "not update as mobile no is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn false

    assertThrows[IllegalArgumentException](userOperations.update(UUID.randomUUID(),user))
  }

  it should "not update as email id is invalid" in {

    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn false
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    assertThrows[IllegalArgumentException](userOperations.update(UUID.randomUUID(),user))
  }

  it should "not update as email id and mobile no are valid but update returns false" in {

    val uuid = UUID.randomUUID()
    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true

    when(mockedUserDb.update(uuid,user)) thenReturn false

    assert(!userOperations.update(uuid,user))
  }

  it should "update the user" in {

    val uuid = UUID.randomUUID()
    when(mockedEmailValidator.emailIdIsValid(user.emailId)) thenReturn true
    when(mockedMobileNoValidator.mobileNoIsValid(user.mobileNo)) thenReturn true
    when(mockedUserDb.update(uuid,user)) thenReturn true

    assert(userOperations.update(uuid,user))
  }
  /*update method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteByID method test cases*/
  "deleteByID" should "not delete the user when user id is not valid" in {

    val uuid = UUID.randomUUID()
    when(mockedUserDb.deleteById(uuid)) thenReturn false

    assert(!userOperations.deleteById(uuid))
  }

  it should "delete the user when user id is valid" in {
    val uuid = UUID.randomUUID()
    when(mockedUserDb.deleteById(uuid)) thenReturn true

    assert(userOperations.deleteById(uuid))
  }
  /*deleteByID method test cases ended*/
  /*---------------------------------------------------------------------------*/

  /*deleteAll method test cases*/
  "deleteAll" should "not delete all users as ListBuffer is empty" in {

    when(mockedUserDb.deleteAll()) thenReturn false

    assert(!userOperations.deleteAll())
  }

  it should "delete all users as ListBuffer is not empty" in {
    when(mockedUserDb.deleteAll()) thenReturn true

    assert(userOperations.deleteAll())
  }
  /*deleteAll method test cases ended*/
  /*---------------------------------------------------------------------------*/
}
