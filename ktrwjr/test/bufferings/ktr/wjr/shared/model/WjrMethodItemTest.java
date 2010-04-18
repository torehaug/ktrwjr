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
package bufferings.ktr.wjr.shared.model;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

public class WjrMethodItemTest {

  @Test(expected = NullPointerException.class)
  public void wjrMethodItem_WillThrowNPE_WhenClassCanonicalNameIsNull() {
    new WjrMethodItem(null, "barMethod");
  }

  @Test(expected = IllegalArgumentException.class)
  public void wjrMethodItem_WillThrowIAE_WhenClassCanonicalNameIsEmpty() {
    new WjrMethodItem("", "barMethod");
  }

  @Test(expected = NullPointerException.class)
  public void wjrMethodItem_WillThrowNPE_WhenMethodNameIsNull() {
    new WjrMethodItem("foo.Foo", null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wjrMethodItem_WillThrowIAE_WhenMethodNameIsEmpty() {
    new WjrMethodItem("foo.Foo", "");
  }

  @Test
  public void wjrMethodItem_CanConstruct() {
    WjrMethodItem wjrMethodItem = new WjrMethodItem("Foo", "barMethod");
    assertThat(wjrMethodItem.getClassCanonicalName(), is("Foo"));
    assertThat(wjrMethodItem.getClassSimpleName(), is("Foo"));

    assertThat(wjrMethodItem.getMethodCanonicalName(), is("Foo#barMethod"));
    assertThat(wjrMethodItem.getMethodSimpleName(), is("barMethod"));

    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod");
    assertThat(wjrMethodItem2.getClassCanonicalName(), is("foo.Foo"));
    assertThat(wjrMethodItem2.getClassSimpleName(), is("Foo"));

    assertThat(wjrMethodItem2.getMethodCanonicalName(), is("foo.Foo#barMethod"));
    assertThat(wjrMethodItem2.getMethodSimpleName(), is("barMethod"));
  }

  @Test
  public void clearResult_CanClearResult() {
    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
    wjrMethodItem.state = State.ERROR;
    wjrMethodItem.log = "sampleLog";
    wjrMethodItem.time = "1111";
    wjrMethodItem.trace = "sampleTrace";

    wjrMethodItem.clearResult();

    assertThat(wjrMethodItem.getClassCanonicalName(), is("foo.Foo"));
    assertThat(wjrMethodItem.getClassSimpleName(), is("Foo"));
    assertThat(wjrMethodItem.getMethodCanonicalName(), is("foo.Foo#barMethod"));
    assertThat(wjrMethodItem.getMethodSimpleName(), is("barMethod"));

    assertThat(wjrMethodItem.state, is(State.NOT_YET));
    assertThat(wjrMethodItem.log, is(nullValue()));
    assertThat(wjrMethodItem.time, is(nullValue()));
    assertThat(wjrMethodItem.trace, is(nullValue()));
  }

}
