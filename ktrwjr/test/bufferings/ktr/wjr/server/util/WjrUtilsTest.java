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
package bufferings.ktr.wjr.server.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.server.fortest.ForTestInherit;
import bufferings.ktr.wjr.shared.util.WjrSharedUtils;

/**
 * Utility methods.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrUtilsTest {

  @Test
  public void convertToBoolean_WillReturnTrue_WhenStrIsTrue() {
    assertThat(WjrServerUtils.convertToBoolean("true", true), is(true));
    assertThat(WjrServerUtils.convertToBoolean("true", false), is(true));
  }

  @Test
  public void convertToBoolean_WillReturnFalse_WhenStrIsFalse() {
    assertThat(WjrServerUtils.convertToBoolean("false", true), is(false));
    assertThat(WjrServerUtils.convertToBoolean("false", false), is(false));
  }

  @Test
  public void convertToBoolean_WillReturnDef_WhenStrIsOthers() {
    assertThat(WjrServerUtils.convertToBoolean(null, true), is(true));
    assertThat(WjrServerUtils.convertToBoolean(null, false), is(false));
    assertThat(WjrServerUtils.convertToBoolean("", true), is(true));
    assertThat(WjrServerUtils.convertToBoolean("", false), is(false));
    assertThat(WjrServerUtils.convertToBoolean("True", true), is(true));
    assertThat(WjrServerUtils.convertToBoolean("True", false), is(false));
    assertThat(WjrServerUtils.convertToBoolean("False", true), is(true));
    assertThat(WjrServerUtils.convertToBoolean("False", false), is(false));
  }

  @Test(expected = NullPointerException.class)
  public void loadClass_WillThrowNPE_WhenClassNameIsNull() {
    WjrServerUtils.loadClass(null);
  }

  @Test(expected = ClassNotFoundRuntimeException.class)
  public void loadClass_WillThrowRE_WhenClassNameIsEmpty() {
    WjrServerUtils.loadClass("");
  }

  @Test(expected = ClassNotFoundRuntimeException.class)
  public void loadClass_WillThrowRE_WhenClassNotExist() {
    WjrServerUtils.loadClass("notexist.NotExist");
  }

  @Test
  public void loadClass_CanLoadClass() {
    assertThat(
      WjrServerUtils.loadClass(ForTest.class.getName()).getName(),
      is(ForTest.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInnerNotStaticClass() {
    assertThat(
      WjrServerUtils
        .loadClass(ForTest.ForTestInnerNotStatic.class.getName())
        .getName(),
      is(ForTest.ForTestInnerNotStatic.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInnerStaticClass() {
    assertThat(
      WjrServerUtils
        .loadClass(ForTest.ForTestInnerStatic.class.getName())
        .getName(),
      is(ForTest.ForTestInnerStatic.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInheritClass() {
    assertThat(WjrServerUtils
      .loadClass(ForTestInherit.class.getName())
      .getName(), is(ForTestInherit.class.getName()));
  }

  @Test(expected = NullPointerException.class)
  public void getTimeZone_WillThrowNPE_WithNullId() {
    WjrServerUtils.getTimeZone(null);
  }

  @Test
  public void getTimeZone_WillGMT_WithInvalidId() {
    assertThat(WjrServerUtils.getTimeZone("").getID(), is("GMT"));
    assertThat(WjrServerUtils.getTimeZone("Invalid").getID(), is("GMT"));
  }

  @Test
  public void getTimeZone_CanGetTimeZone_WithValidId() {
    assertThat(WjrServerUtils.getTimeZone("JST").getID(), is("JST"));
    assertThat(WjrServerUtils.getTimeZone("PST").getID(), is("PST"));
  }

  @Test
  public void getTrace_WillReturnEmptyString_WhenExceptionIsNull() {
    assertThat(WjrServerUtils.getTraceStringFromException(null), is(""));
  }

  @Test
  public void getTrace_CanGetTrace() {
    assertThat(
      WjrServerUtils.getTraceStringFromException(new Exception()),
      is(not(nullValue())));
  }

  @Test
  public void isNullOrEmptyString_WillReturnTrue_WhenNull() {
    assertThat(WjrSharedUtils.isNullOrEmptyString(null), is(true));
  }

  @Test
  public void isNullOrEmptyString_WillReturnTrue_WhenEmpty() {
    assertThat(WjrSharedUtils.isNullOrEmptyString(""), is(true));
  }

  @Test
  public void isNullOrEmptyString_WillReturnFalse_WhenNotEmpty() {
    assertThat(WjrSharedUtils.isNullOrEmptyString("a"), is(false));
  }
}
