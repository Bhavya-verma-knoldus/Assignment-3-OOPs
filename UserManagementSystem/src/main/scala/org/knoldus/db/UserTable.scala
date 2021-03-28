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
import scala.concurrent.Future
import scala.math.Ordered.orderingToOrdered
import scala.util.{Failure, Success, Try}
import scala.concurrent.ExecutionContext.Implicits.global

class UserTable {

  private val listBuffer = ListBuffer.empty[User]

  def add(user: User): Future[Option[UUID]] = Future{
    Try { listBuffer.append(user) } match {
      case Success(_) => listBuffer.last.id
      case Failure(_) => throw new RuntimeException("Invalid Operation")
    }
  }

  def getById(id: Option[UUID]): Future[List[User]] = Future{
    val list = filterListById(id)
    if(list != Nil) list else throw new RuntimeException("User does not exist")
  }

  def getAll: Future[List[User]] = Future{
    listBuffer.toList
  }

  def update(id: Option[UUID], updatedUser: User): Future[Boolean] = Future{
    val index = findIndexById(id)
    if(index != -1) { listBuffer.update(index,updatedUser); true } else false
  }

  def deleteById(id: Option[UUID]): Future[Boolean] = Future{
    val index = findIndexById(id)
    if(index != -1) { listBuffer.remove(index); true } else false
  }

  def deleteAll(): Future[Boolean] = Future{
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
