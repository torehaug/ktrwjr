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
import bufferings.ktr.wjr.server.util.WjrUtils;

/**
 * Utility methods.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrUtilsTest {

  @Test(expected = NullPointerException.class)
  public void loadClass_WillThrowNPE_WhenClassNameIsNull() {
    WjrUtils.loadClass(null);
  }

  @Test(expected = ClassNotFoundRuntimeException.class)
  public void loadClass_WillThrowRE_WhenClassNameIsEmpty() {
    WjrUtils.loadClass("");
  }

  @Test(expected = ClassNotFoundRuntimeException.class)
  public void loadClass_WillThrowRE_WhenClassNotExist() {
    WjrUtils.loadClass("notexist.NotExist");
  }

  @Test
  public void loadClass_CanLoadClass() {
    assertThat(
      WjrUtils.loadClass(ForTest.class.getName()).getName(),
      is(ForTest.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInnerNotStaticClass() {
    assertThat(WjrUtils
      .loadClass(ForTest.ForTestInnerNotStatic.class.getName())
      .getName(), is(ForTest.ForTestInnerNotStatic.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInnerStaticClass() {
    assertThat(WjrUtils
      .loadClass(ForTest.ForTestInnerStatic.class.getName())
      .getName(), is(ForTest.ForTestInnerStatic.class.getName()));
  }

  @Test
  public void loadClass_CanLoadInheritClass() {
    assertThat(
      WjrUtils.loadClass(ForTestInherit.class.getName()).getName(),
      is(ForTestInherit.class.getName()));
  }
  
  @Test(expected=NullPointerException.class)
  public void getTimeZone_WillThrowNPE_WithNullId(){
    WjrUtils.getTimeZone(null);
  }

  @Test
  public void getTimeZone_WillGMT_WithInvalidId(){
    assertThat(WjrUtils.getTimeZone("").getID(), is("GMT"));
    assertThat(WjrUtils.getTimeZone("Invalid").getID(), is("GMT"));
  }

  @Test
  public void getTimeZone_CanGetTimeZone_WithValidId(){
    assertThat(WjrUtils.getTimeZone("JST").getID(), is("JST"));
    assertThat(WjrUtils.getTimeZone("PST").getID(), is("PST"));
  }
}
