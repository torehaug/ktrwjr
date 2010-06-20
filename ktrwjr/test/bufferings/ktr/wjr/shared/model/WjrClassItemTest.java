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
  public void wjrClassItem_WillThrowNPE_WhenClassNameIsNull() {
    new WjrClassItem(null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void wjrClassItem_WillThrowIAE_WhenClassNameIsEmpty() {
    new WjrClassItem("");
  }

  @Test
  public void wjrClassItem_CanConstruct() {
    WjrClassItem classItem = new WjrClassItem("bar.foo.Foo");
    assertThat(classItem.getClassName(), is("bar.foo.Foo"));
  }

  @Test
  public void clearSummary_CanClearSummary() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    classItem.state = State.ERROR;
    classItem.successCount = 10;
    classItem.failureCount = 5;
    classItem.errorCount = 3;
    classItem.runningCount = 3;
    classItem.retryWaitingCount = 2;
    classItem.notYetCount = 2;
    classItem.totalCount = 25;

    classItem.clearSummary();

    assertThat(classItem.getClassName(), is("foo.Foo"));

    assertThat(classItem.getState(), is(State.NOT_YET));
    assertThat(classItem.getSuccessCount(), is(0));
    assertThat(classItem.getFailureCount(), is(0));
    assertThat(classItem.getErrorCount(), is(0));
    assertThat(classItem.getRunningCount(), is(0));
    assertThat(classItem.getRetryWaitingCount(), is(0));
    assertThat(classItem.getNotYetCount(), is(25));
    assertThat(classItem.getTotalCount(), is(25));
  }

  @Test(expected = NullPointerException.class)
  public void updateSummary_WillThrowNPE_WhenWjrStoreIsNull() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    classItem.updateSummary(null);
  }

  @Test
  public void updateSummary_WillBeErrorState_WithError() {
    WjrStore store = new WjrStore();
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem methodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem methodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    WjrMethodItem methodItem6 = new WjrMethodItem("foo.Foo", "barMethod6");
    WjrMethodItem methodItem7 = new WjrMethodItem("foo.Foo", "barMethod7");
    store.addMethodItem(methodItem1);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem4);
    store.addMethodItem(methodItem5);
    store.addMethodItem(methodItem6);
    store.addMethodItem(methodItem7);

    methodItem1.setState(State.SUCCESS);
    methodItem2.setState(State.ERROR);
    methodItem3.setState(State.FAILURE);
    methodItem4.setState(State.SUCCESS);
    methodItem5.setState(State.NOT_YET);
    methodItem6.setState(State.RUNNING);
    methodItem7.setState(State.RETRY_WAITING);

    classItem.updateSummary(store);

    assertThat(classItem.getState(), is(State.ERROR));
    assertThat(classItem.getSuccessCount(), is(2));
    assertThat(classItem.getFailureCount(), is(1));
    assertThat(classItem.getErrorCount(), is(1));
    assertThat(classItem.getRunningCount(), is(1));
    assertThat(classItem.getRetryWaitingCount(), is(1));
    assertThat(classItem.getNotYetCount(), is(1));
    assertThat(classItem.getTotalCount(), is(7));
  }

  @Test
  public void updateSummary_WillBeFailureState_WithFailureAndNoError() {
    WjrStore store = new WjrStore();
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem methodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem methodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    WjrMethodItem methodItem6 = new WjrMethodItem("foo.Foo", "barMethod6");
    WjrMethodItem methodItem7 = new WjrMethodItem("foo.Foo", "barMethod7");
    store.addMethodItem(methodItem1);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem4);
    store.addMethodItem(methodItem5);
    store.addMethodItem(methodItem6);
    store.addMethodItem(methodItem7);

    methodItem1.setState(State.SUCCESS);
    methodItem2.setState(State.FAILURE);
    methodItem3.setState(State.FAILURE);
    methodItem4.setState(State.SUCCESS);
    methodItem5.setState(State.NOT_YET);
    methodItem6.setState(State.RUNNING);
    methodItem7.setState(State.RETRY_WAITING);

    classItem.updateSummary(store);

    assertThat(classItem.getState(), is(State.FAILURE));
    assertThat(classItem.getSuccessCount(), is(2));
    assertThat(classItem.getFailureCount(), is(2));
    assertThat(classItem.getErrorCount(), is(0));
    assertThat(classItem.getRunningCount(), is(1));
    assertThat(classItem.getRetryWaitingCount(), is(1));
    assertThat(classItem.getNotYetCount(), is(1));
    assertThat(classItem.getTotalCount(), is(7));
  }

  @Test
  public void updateSummary_WillBeRunningState_WithRunningRetryWaitingAndNoFailureError() {
    WjrStore store = new WjrStore();
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem methodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem methodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    WjrMethodItem methodItem6 = new WjrMethodItem("foo.Foo", "barMethod6");
    store.addMethodItem(methodItem1);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem4);
    store.addMethodItem(methodItem5);
    store.addMethodItem(methodItem6);

    methodItem1.setState(State.SUCCESS);
    methodItem2.setState(State.NOT_YET);
    methodItem3.setState(State.SUCCESS);
    methodItem4.setState(State.RUNNING);
    methodItem5.setState(State.SUCCESS);
    methodItem6.setState(State.RETRY_WAITING);

    classItem.updateSummary(store);

    assertThat(classItem.getState(), is(State.RUNNING));
    assertThat(classItem.getSuccessCount(), is(3));
    assertThat(classItem.getFailureCount(), is(0));
    assertThat(classItem.getErrorCount(), is(0));
    assertThat(classItem.getRunningCount(), is(1));
    assertThat(classItem.getRetryWaitingCount(), is(1));
    assertThat(classItem.getNotYetCount(), is(1));
    assertThat(classItem.getTotalCount(), is(6));
  }

  @Test
  public void updateSummary_WillBeNotYetState_WithNotYetAndNoFailureErrorRunningRetryWaiting() {
    WjrStore store = new WjrStore();
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem methodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem methodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    store.addMethodItem(methodItem1);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem4);
    store.addMethodItem(methodItem5);

    methodItem1.setState(State.SUCCESS);
    methodItem2.setState(State.NOT_YET);
    methodItem3.setState(State.SUCCESS);
    methodItem4.setState(State.SUCCESS);
    methodItem5.setState(State.SUCCESS);

    classItem.updateSummary(store);

    assertThat(classItem.getState(), is(State.NOT_YET));
    assertThat(classItem.getSuccessCount(), is(4));
    assertThat(classItem.getFailureCount(), is(0));
    assertThat(classItem.getErrorCount(), is(0));
    assertThat(classItem.getRunningCount(), is(0));
    assertThat(classItem.getNotYetCount(), is(1));
    assertThat(classItem.getTotalCount(), is(5));
  }

  @Test
  public void updateSummary_WillBeSuccessState_WithAllSuccess() {
    WjrStore store = new WjrStore();
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo", "barMethod3");
    WjrMethodItem methodItem4 = new WjrMethodItem("foo.Foo", "barMethod4");
    WjrMethodItem methodItem5 = new WjrMethodItem("foo.Foo", "barMethod5");
    store.addMethodItem(methodItem1);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem4);
    store.addMethodItem(methodItem5);

    methodItem1.setState(State.SUCCESS);
    methodItem2.setState(State.SUCCESS);
    methodItem3.setState(State.SUCCESS);
    methodItem4.setState(State.SUCCESS);
    methodItem5.setState(State.SUCCESS);

    classItem.updateSummary(store);

    assertThat(classItem.getState(), is(State.SUCCESS));
    assertThat(classItem.getSuccessCount(), is(5));
    assertThat(classItem.getFailureCount(), is(0));
    assertThat(classItem.getErrorCount(), is(0));
    assertThat(classItem.getRunningCount(), is(0));
    assertThat(classItem.getNotYetCount(), is(0));
    assertThat(classItem.getTotalCount(), is(5));
  }
}
