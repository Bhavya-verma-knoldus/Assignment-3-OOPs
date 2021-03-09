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

package org.knoldus.bootstrap

import org.knoldus.db.{UserDb, DAO}
import org.knoldus.models.{User, UserType}
import org.knoldus.operations.UserOperations

object Main extends App{

  val userDb: DAO[User] = new UserDb
  val userOperations = new UserOperations(userDb)

  //Creating object of user as admin and customer to add them to the ListBuffer
  val admin = User(userName = "Bhavya",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))
  val customer = User(userName = "Rahul",userType = UserType.Customer,password = "qwerty9876",age = 28,emailId = "rahul@gmail.com",mobileNo = 9988774455L,address = Some("Rohini"))

  //Adding admin and customer to the ListBuffer
  val adminId = userOperations.add(admin)
  val customerId = userOperations.add(customer)

  print("Listing all users" + "\n")
  for(index <- userOperations.getAll.indices){
    print("User " + (index + 1) + "\n")
    print("(UserName: " + userOperations.getAll(index).userName + ", ")
    print("UserType: " + userOperations.getAll(index).userType + ", ")
    print("Age : " + userOperations.getAll(index).age + ", ")
    print("Email ID: " + userOperations.getAll(index).emailId + ", ")
    print("Mobile No: " + userOperations.getAll(index).mobileNo + ", ")
    print("Address: " + userOperations.getAll(index).address.get + ")\n")
  }

  val updatedUser = User(userName = "Bhanu",userType = UserType.Admin,password = "bhavya1234",age = 24,emailId = "bhavya@gmail.com",mobileNo = 9999666658L,address = Some("Shahdara"))

  print("\nUpdate method called: " + userOperations.update(adminId,updatedUser) + "\n")

  print("Listing all users" + "\n")
  for(index <- userOperations.getAll.indices){
    print("User " + (index + 1) + "\n")
    print("(UserName: " + userOperations.getAll(index).userName + ", ")
    print("UserType: " + userOperations.getAll(index).userType + ", ")
    print("Age : " + userOperations.getAll(index).age + ", ")
    print("Email ID: " + userOperations.getAll(index).emailId + ", ")
    print("Mobile No: " + userOperations.getAll(index).mobileNo + ", ")
    print("Address: " + userOperations.getAll(index).address.get + ")\n")
  }

  print("\nDelete method called: " + userOperations.deleteById(customerId) + "\n")

  print(userOperations.getById(customerId) + "\n")
}
