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

import org.knoldus.models.User

import java.util.UUID
import scala.collection.mutable.ListBuffer
import scala.math.Ordered.orderingToOrdered
import scala.util.{Failure, Success, Try}

class UserDb extends DAO[User]{

  private val listBuffer = new ListBuffer[User]()

  override def add(user: User): UUID = {
    Try { listBuffer.append(user) } match {
      case Success(_) => listBuffer.last.id.get
      case Failure(_) => throw new RuntimeException("Invalid Operation")
    }
  }

  override def getById(id: UUID): List[User] = {
    val list = filterListById(Some(id))
    if(list != Nil) list else throw new RuntimeException("User does not exist")
  }

  override def getAll: List[User] = {
    listBuffer.toList
  }

  override def update(id: Option[UUID], updatedUser: User): Boolean = {
    val index = findIndexById(id)
    if(index != -1) { listBuffer.update(index,updatedUser); true } else false
  }

  override def deleteById(id: UUID): Boolean = {
    val index = findIndexById(Some(id))
    if(index != -1) { listBuffer.remove(index); true } else false
  }

  override def deleteAll(): Boolean = {
    if(listBuffer.nonEmpty) { listBuffer.remove(0,listBuffer.length); true } else false
  }

  private def filterListById(id: Option[UUID]): List[User] = {
    listBuffer.filter(listBuffer => listBuffer.id.compareTo(id) == 0).toList
  }

  private def findIndexById(id: Option[UUID]): Int = {
    val list = filterListById(id)
    if(list != Nil) listBuffer.indexOf(list.head) else -1
  }
}
