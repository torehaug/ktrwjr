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
  public void wjrMethodItem_WillThrowNPE_WhenClassNameIsNull() {
    new WjrMethodItem(null, "barMethod");
  }

  @Test(expected = IllegalArgumentException.class)
  public void wjrMethodItem_WillThrowIAE_WhenClassNameIsEmpty() {
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
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");
    assertThat(methodItem.getClassName(), is("bar.foo.Foo"));

    assertThat(methodItem.getClassAndMethodName(), is("bar.foo.Foo#barMethod"));
    assertThat(methodItem.getMethodName(), is("barMethod"));
  }

  @Test
  public void setTrace_WillBeEmpty_WhenNullIsSet() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");

    methodItem.setTrace("abcde");
    assertThat(methodItem.getTrace(), is("abcde"));

    methodItem.setTrace(null);
    assertThat(methodItem.getTrace(), is(""));
  }

  @Test
  public void setLog_WillBeEmpty_WhenNullIsSet() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");

    methodItem.setLog("abcde");
    assertThat(methodItem.getLog(), is("abcde"));

    methodItem.setLog(null);
    assertThat(methodItem.getLog(), is(""));
  }

  @Test
  public void setTime_WillBeEmpty_WhenNullIsSet() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");

    methodItem.setTime("1000");
    assertThat(methodItem.getTime(), is("1000"));

    methodItem.setTime(null);
    assertThat(methodItem.getTime(), is(""));
  }

  @Test
  public void setCpuTime_WillBeEmpty_WhenNullIsSet() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");

    methodItem.setCpuTime("1000");
    assertThat(methodItem.getCpuTime(), is("1000"));

    methodItem.setCpuTime(null);
    assertThat(methodItem.getCpuTime(), is(""));
  }

  @Test
  public void setApuTime_WillBeEmpty_WhenNullIsSet() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");

    methodItem.setApiTime("1000");
    assertThat(methodItem.getApiTime(), is("1000"));

    methodItem.setApiTime(null);
    assertThat(methodItem.getApiTime(), is(""));
  }

  @Test
  public void clearResult_CanClearResult() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");
    methodItem.setState(State.ERROR);
    methodItem.setTrace("sampleTrace");
    methodItem.setLog("sampleLog");
    methodItem.setTime("1111");
    methodItem.setCpuTime("1111");
    methodItem.setApiTime("1111");
    methodItem.setOverQuota(true);

    methodItem.clearResult();

    assertThat(methodItem.getClassName(), is("bar.foo.Foo"));
    assertThat(methodItem.getClassAndMethodName(), is("bar.foo.Foo#barMethod"));
    assertThat(methodItem.getMethodName(), is("barMethod"));

    assertThat(methodItem.getState(), is(State.NOT_YET));
    assertThat(methodItem.getTrace(), is(""));
    assertThat(methodItem.getLog(), is(""));
    assertThat(methodItem.getTime(), is(""));
    assertThat(methodItem.getCpuTime(), is(""));
    assertThat(methodItem.getApiTime(), is(""));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test(expected = NullPointerException.class)
  public void setState_WillThrowNPE_WhenStateIsNull() {
    WjrMethodItem methodItem = new WjrMethodItem("bar.foo.Foo", "barMethod");
    methodItem.setState(null);
  }

  @Test(expected = NullPointerException.class)
  public void copyResult_WillThrowNPE_WhenToIsNull() {
    WjrMethodItem from = new WjrMethodItem("bar.foo.Foo", "barMethod");
    from.copyResult(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void copyResult_WillThrowIAE_WhenClassNameIsDifferent() {
    WjrMethodItem from = new WjrMethodItem("bar.foo.Foo", "barMethod");
    WjrMethodItem to = new WjrMethodItem("bar.foo.Bar", "barMethod");
    from.copyResult(to);
  }

  @Test(expected = IllegalArgumentException.class)
  public void copyResult_WillThrowIAE_WhenMethodNameIsDifferent() {
    WjrMethodItem from = new WjrMethodItem("bar.foo.Foo", "barMethod");
    WjrMethodItem to = new WjrMethodItem("bar.foo.Foo", "fooMethod");
    from.copyResult(to);
  }

  @Test
  public void copyResult_WillCopyResults() {
    WjrMethodItem from = new WjrMethodItem("bar.foo.Foo", "barMethod");
    from.setState(State.FAILURE);
    from.setTime("1111");
    from.setApiTime("2222");
    from.setCpuTime("3333");
    from.setLog("mylog");
    from.setTrace("mytrace");

    WjrMethodItem to = new WjrMethodItem("bar.foo.Foo", "barMethod");
    from.copyResult(to);

    assertThat(from.getState(), is(to.getState()));
    assertThat(from.getTime(), is(to.getTime()));
    assertThat(from.getApiTime(), is(to.getApiTime()));
    assertThat(from.getCpuTime(), is(to.getCpuTime()));
    assertThat(from.getLog(), is(to.getLog()));
    assertThat(from.getTrace(), is(to.getTrace()));
  }

  @Test
  public void copyResult_WillNotCopyRetryInfo() {
    WjrMethodItem from = new WjrMethodItem("bar.foo.Foo", "barMethod");
    from.setOverQuota(true);
    from.setRetryCount(5);
    from.setMaxRetryCount(10);
    from.setWaitingSeconds(15);

    WjrMethodItem to = new WjrMethodItem("bar.foo.Foo", "barMethod");
    from.copyResult(to);

    assertThat(to.isOverQuota(), is(false));
    assertThat(to.getRetryCount(), is(0));
    assertThat(to.getMaxRetryCount(), is(0));
    assertThat(to.getWaitingSeconds(), is(0));
  }
}
