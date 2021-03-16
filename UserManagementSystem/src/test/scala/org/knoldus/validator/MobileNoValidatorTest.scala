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

class MobileNoValidatorTest extends AnyFlatSpec {

  val mobileNoValidator = new MobileNoValidator

  "mobileNo" should "be invalid as it contains less than 10 digits" in {
    assert(!mobileNoValidator.mobileNoIsValid(986578))
  }

  it should "be invalid as it contains more than 10 digits" in {
    assert(!mobileNoValidator.mobileNoIsValid(98745632178455L))
  }

  it should "be invalid as it does not start with 0,91,7,8,9" in {
    assert(!mobileNoValidator.mobileNoIsValid(5236478451L))
  }

  it should "be valid" in {
    assert(mobileNoValidator.mobileNoIsValid(9999877845L))
  }
}
