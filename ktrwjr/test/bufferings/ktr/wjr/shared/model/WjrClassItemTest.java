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

public class WjrClassItemTest {

  @Test(expected = NullPointerException.class)
  public void wjrClassItem_WillThrowNPE_WhenClassCanonicalNameIsNull() {
    new WjrClassItem(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wjrClassItem_WillThrowIAE_WhenClassCanonicalNameIsEmpty() {
    new WjrClassItem("");
  }

  @Test
  public void wjrClassItem_CanConstruct() {
    WjrClassItem wjrClassItem = new WjrClassItem("Foo");
    assertThat(wjrClassItem.getClassCanonicalName(), is("Foo"));
    assertThat(wjrClassItem.getClassSimpleName(), is("Foo"));

    WjrClassItem wjrClassItem2 = new WjrClassItem("foo.Foo");
    assertThat(wjrClassItem2.getClassCanonicalName(), is("foo.Foo"));
    assertThat(wjrClassItem2.getClassSimpleName(), is("Foo"));
  }

  @Test
  public void clearSummary_CanClearSummary() {
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrClassItem.state = State.ERROR;
    wjrClassItem.successCount = 10;
    wjrClassItem.failureCount = 5;
    wjrClassItem.errorCount = 3;
    wjrClassItem.notYetCount = 2;
    wjrClassItem.totalCount = 20;

    wjrClassItem.clearSummary();

    assertThat(wjrClassItem.getClassCanonicalName(), is("foo.Foo"));
    assertThat(wjrClassItem.getClassSimpleName(), is("Foo"));

    assertThat(wjrClassItem.state, is(State.NOT_YET));
    assertThat(wjrClassItem.successCount, is(0));
    assertThat(wjrClassItem.failureCount, is(0));
    assertThat(wjrClassItem.errorCount, is(0));
    assertThat(wjrClassItem.notYetCount, is(20));
    assertThat(wjrClassItem.totalCount, is(20));
  }

  @Test(expected = NullPointerException.class)
  public void updateSummary_WillThrowNPE_WhenWjrStoreIsNull() {
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrClassItem.updateSummary(null);
  }

  @Test
  public void updateSummary_WillBeErrorState_WhenErrorMethodExist() {
    WjrStore wjrStore = new WjrStore();
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrStore.addClassItem(wjrClassItem);

    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem wjrMethodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem wjrMethodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    wjrStore.addMethodItem(wjrMethodItem1);
    wjrStore.addMethodItem(wjrMethodItem2);
    wjrStore.addMethodItem(wjrMethodItem3);
    wjrStore.addMethodItem(wjrMethodItem4);
    wjrStore.addMethodItem(wjrMethodItem5);

    wjrMethodItem1.setState(State.SUCCESS);
    wjrMethodItem2.setState(State.ERROR);
    wjrMethodItem3.setState(State.FAILURE);
    wjrMethodItem4.setState(State.SUCCESS);
    wjrMethodItem5.setState(State.FAILURE);

    wjrClassItem.updateSummary(wjrStore);

    assertThat(wjrClassItem.state, is(State.ERROR));
    assertThat(wjrClassItem.successCount, is(2));
    assertThat(wjrClassItem.failureCount, is(2));
    assertThat(wjrClassItem.errorCount, is(1));
    assertThat(wjrClassItem.notYetCount, is(0));
    assertThat(wjrClassItem.totalCount, is(5));
  }

  @Test
  public void updateSummary_WillBeFailureState_WhenFailureMethodExistAndErrorNotExist() {
    WjrStore wjrStore = new WjrStore();
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrStore.addClassItem(wjrClassItem);

    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem wjrMethodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem wjrMethodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    wjrStore.addMethodItem(wjrMethodItem1);
    wjrStore.addMethodItem(wjrMethodItem2);
    wjrStore.addMethodItem(wjrMethodItem3);
    wjrStore.addMethodItem(wjrMethodItem4);
    wjrStore.addMethodItem(wjrMethodItem5);

    wjrMethodItem1.setState(State.SUCCESS);
    wjrMethodItem2.setState(State.NOT_YET);
    wjrMethodItem3.setState(State.FAILURE);
    wjrMethodItem4.setState(State.SUCCESS);
    wjrMethodItem5.setState(State.FAILURE);

    wjrClassItem.updateSummary(wjrStore);

    assertThat(wjrClassItem.state, is(State.FAILURE));
    assertThat(wjrClassItem.successCount, is(2));
    assertThat(wjrClassItem.failureCount, is(2));
    assertThat(wjrClassItem.errorCount, is(0));
    assertThat(wjrClassItem.notYetCount, is(1));
    assertThat(wjrClassItem.totalCount, is(5));
  }

  @Test
  public void updateSummary_WillBeNotYetState_WhenNotYetMethodExistAndFailureAndErrorNotExist() {
    WjrStore wjrStore = new WjrStore();
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrStore.addClassItem(wjrClassItem);

    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem wjrMethodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem wjrMethodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    wjrStore.addMethodItem(wjrMethodItem1);
    wjrStore.addMethodItem(wjrMethodItem2);
    wjrStore.addMethodItem(wjrMethodItem3);
    wjrStore.addMethodItem(wjrMethodItem4);
    wjrStore.addMethodItem(wjrMethodItem5);

    wjrMethodItem1.setState(State.SUCCESS);
    wjrMethodItem2.setState(State.NOT_YET);
    wjrMethodItem3.setState(State.SUCCESS);
    wjrMethodItem4.setState(State.SUCCESS);
    wjrMethodItem5.setState(State.SUCCESS);

    wjrClassItem.updateSummary(wjrStore);

    assertThat(wjrClassItem.state, is(State.NOT_YET));
    assertThat(wjrClassItem.successCount, is(4));
    assertThat(wjrClassItem.failureCount, is(0));
    assertThat(wjrClassItem.errorCount, is(0));
    assertThat(wjrClassItem.notYetCount, is(1));
    assertThat(wjrClassItem.totalCount, is(5));
  }

  @Test
  public void updateSummary_WillBeSuccessState_WhenAllMethodSuccess() {
    WjrStore wjrStore = new WjrStore();
    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
    wjrStore.addClassItem(wjrClassItem);

    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem wjrMethodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem wjrMethodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    wjrStore.addMethodItem(wjrMethodItem1);
    wjrStore.addMethodItem(wjrMethodItem2);
    wjrStore.addMethodItem(wjrMethodItem3);
    wjrStore.addMethodItem(wjrMethodItem4);
    wjrStore.addMethodItem(wjrMethodItem5);

    wjrMethodItem1.setState(State.SUCCESS);
    wjrMethodItem2.setState(State.SUCCESS);
    wjrMethodItem3.setState(State.SUCCESS);
    wjrMethodItem4.setState(State.SUCCESS);
    wjrMethodItem5.setState(State.SUCCESS);

    wjrClassItem.updateSummary(wjrStore);

    assertThat(wjrClassItem.state, is(State.SUCCESS));
    assertThat(wjrClassItem.successCount, is(5));
    assertThat(wjrClassItem.failureCount, is(0));
    assertThat(wjrClassItem.errorCount, is(0));
    assertThat(wjrClassItem.notYetCount, is(0));
    assertThat(wjrClassItem.totalCount, is(5));
  }
}
