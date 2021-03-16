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

    /*user will be added only when email id and mobile no. is valid*/
    if(emailValidator.emailIdIsValid(user.emailId) && mobileNoValidator.mobileNoIsValid(user.mobileNo)) {
      userDb.add(user)
    }
    else {
      throw new IllegalArgumentException("email id or mobile no is not valid")
    }
  }

  def getById(id: UUID): List[User] = {
    userDb.getById(id)
  }

  def getAll: List[User] = userDb.getAll

  def update(id:UUID,updatedUser: User): Boolean = {

    /*user will be updated only when email id and mobile no. is valid*/
    if(emailValidator.emailIdIsValid(updatedUser.emailId) && mobileNoValidator.mobileNoIsValid(updatedUser.mobileNo)) {
      userDb.update(id,updatedUser)
    }
    else{
      throw new IllegalArgumentException("email id or mobile no is not valid")
    }
  }

  def deleteById(id: UUID): Boolean = {
    userDb.deleteById(id)
  }

  def deleteAll(): Boolean = {
    userDb.deleteAll()
  }
}
