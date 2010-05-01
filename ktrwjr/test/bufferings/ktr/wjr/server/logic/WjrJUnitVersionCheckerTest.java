/*
 * Copyright 2010 bufferings[at]gmail.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package bufferings.ktr.wjr.server.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class WjrJUnitVersionCheckerTest {
  @Test
  public void isJUnit4Available_WillReturnTrue_WhenVersionStartWith4() {
    WjrJUnitVersionChecker checker = new WjrJUnitVersionChecker() {
      @Override
      protected String getJUnitVersionId() {
        return "4.xx";
      }
    };
    assertThat(checker.isJUnit4Available(), is(true));
  }

  @Test
  public void isJUnit4Available_WillReturnFalse_WhenVersionNotStartsWith4() {
    WjrJUnitVersionChecker checker = new WjrJUnitVersionChecker() {
      @Override
      protected String getJUnitVersionId() {
        return "3.xx";
      }
    };
    assertThat(checker.isJUnit4Available(), is(false));
  }

  @Test
  public void isJUnit3Available_WillReturnTrue_WhenVersionStartWith4() {
    WjrJUnitVersionChecker checker = new WjrJUnitVersionChecker() {
      @Override
      protected String getJUnitVersionId() {
        return "3.xx";
      }
    };
    assertThat(checker.isJUnit3Available(), is(true));
  }

  @Test
  public void isJUnit3Available_WillReturnFalse_WhenVersionNotStartsWith4() {
    WjrJUnitVersionChecker checker = new WjrJUnitVersionChecker() {
      @Override
      protected String getJUnitVersionId() {
        return "4.xx";
      }
    };
    assertThat(checker.isJUnit3Available(), is(false));
  }
}
