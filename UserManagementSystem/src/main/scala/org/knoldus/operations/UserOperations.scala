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

import org.knoldus.db.DAO
import org.knoldus.models.User
import org.knoldus.validator.{EmailValidator, MobileNoValidator}

import java.util.UUID

class UserOperations(userDb: DAO[User],emailValidator: EmailValidator,mobileNoValidator: MobileNoValidator) {

  def add(user: User): UUID ={

    if(userValid(user)) {
      val uuid = UUID.randomUUID()
      userDb.add(user.copy(id=Some(uuid)))
    }
    else{
      throw new RuntimeException("Invalid operation")
    }
  }

  def getById(id: UUID): List[User] = {
    userDb.getById(id)
  }

  def getAll: List[User] = userDb.getAll

  def update(id:Option[UUID],updatedUser: User): Boolean = {

    if(userValid(updatedUser)) {
      userDb.update(id,updatedUser.copy(id = id))
    }
    else{
      throw new RuntimeException("Invalid operation")
    }
  }

  def deleteById(id: UUID): Boolean = {
    userDb.deleteById(id)
  }

  def deleteAll(): Boolean = {
    userDb.deleteAll()
  }

  private def userValid(user: User): Boolean = {
    user match {
      case User(None, userName, userType, password, age, emailId, mobileNo, address) =>

        if(emailAndMobileValidator(user)) true else throw new RuntimeException("email id or mobile no is not valid")

      case User(Some(_),_,_,_,_,_,_,_) => false
    }
  }

  private def emailAndMobileValidator(user: User): Boolean = {
    if(emailValidator.emailIdIsValid(user.emailId) && mobileNoValidator.mobileNoIsValid(user.mobileNo)) true else false
  }
}
