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

import org.knoldus.repo.DAO
import org.knoldus.models.User
import org.knoldus.validator.{EmailValidator, MobileNoValidator}

import java.util.UUID
import scala.concurrent.Future

class UserOperations(userRepo: DAO[User],emailValidator: EmailValidator,mobileNoValidator: MobileNoValidator) {

  def add(user: User): Future[Option[UUID]] ={

    if(userValid(user)) {
      val uuid = UUID.randomUUID()
      userRepo.add(user.copy(id=Some(uuid)))
    }
    else{
      throw new RuntimeException("Invalid operation")
    }
  }

  def getById(id: Option[UUID]): Future[List[User]] = {
    userRepo.getById(id)
  }

  def getAll: Future[List[User]] = userRepo.getAll

  def update(id:Option[UUID],updatedUser: User): Future[Boolean] = {

    if(userValid(updatedUser)) {
      userRepo.update(id,updatedUser.copy(id = id))
    }
    else{
      throw new RuntimeException("Invalid operation")
    }
  }

  def deleteById(id: Option[UUID]): Future[Boolean] = {
    userRepo.deleteById(id)
  }

  def deleteAll(): Future[Boolean] = {
    userRepo.deleteAll()
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
