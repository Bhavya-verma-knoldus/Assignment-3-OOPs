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

package org.knoldus.validator

import org.scalatest.flatspec.AnyFlatSpec

class EmailValidatorTest extends AnyFlatSpec {

  val emailValidator = new EmailValidator

  "email" should "be invalid as it does not contains recipient name" in {
    assert(!emailValidator.emailIdIsValid("@gmail.com"))
  }

  it should "be invalid as it does not contains @ symbol" in {
    assert(!emailValidator.emailIdIsValid("bhavyagmail.com"))
  }

  it should "be invalid as it does not contains domain name" in {
    assert(!emailValidator.emailIdIsValid("bhavya@.com"))
  }

  it should "be invalid as it does not contains .(com,net,org)" in {
    assert(!emailValidator.emailIdIsValid("bhavya@gmail.in"))
  }

  it should "be valid" in {
    assert(emailValidator.emailIdIsValid("bhavya@gmail.com"))
  }
}
