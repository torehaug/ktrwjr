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

import java.util.Arrays;
import java.util.List;

import junit.framework.AssertionFailedError;
import junit.framework.TestFailure;

import org.junit.Test;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3Inherit;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

/**
 * The test runner of the {@link WjrMethodItem}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnit3MethodRunnerTest {

  private WjrJUnit3MethodRunner methodRunner = new WjrJUnit3MethodRunner();

  @Test
  public void getRunner_WontBeError_WhenMethodNotExist() {
    assertThat(
      methodRunner.getRunner(ForTestJUnit3.class, "notExistMethod"),
      is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunner() {
    assertThat(
      methodRunner.getRunner(ForTestJUnit3.class, "testSucceedMethod"),
      is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunnerOfInnerStaticClass() {
    assertThat(methodRunner.getRunner(
      ForTestJUnit3.ForTestJUnit3InnerStatic.class,
      "testSucceedMethod"), is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunnerOfInheritClass() {
    assertThat(methodRunner.getRunner(
      ForTestJUnit3Inherit.class,
      "testSucceedMethod"), is(not(nullValue())));
  }

  @Test
  public void runTest_WillBeFailure_WhenMethodNotExist() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3.class,
        "notExistMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(1));
    assertThat(result.getErrorCount(), is(0));
    assertThat(result.getFailures().size(), is(1));
    assertThat(result.getErrors().size(), is(0));
  }

  @Test
  public void runTest_CanRunSuccessTest() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3.class,
        "testSuccessMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getErrorCount(), is(0));
    assertThat(result.getFailures().size(), is(0));
    assertThat(result.getErrors().size(), is(0));
  }

  @Test
  public void runTest_CanRunFailureTest() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3.class,
        "testFailureMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(1));
    assertThat(result.getErrorCount(), is(0));
    assertThat(result.getFailures().size(), is(1));
    assertThat(
      result.getFailures().get(0).thrownException() instanceof AssertionFailedError,
      is(true));
    assertThat(result.getErrors().size(), is(0));
  }

  @Test
  public void runTest_CanRunErrorTest() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3.class,
        "testErrorMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getErrorCount(), is(1));
    assertThat(result.getFailures().size(), is(0));
    assertThat(result.getErrors().size(), is(1));
  }

  @Test
  public void runTest_CanRunSuccessTestOfInnerStaticClass() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3.ForTestJUnit3InnerStatic.class,
        "testSuccessMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getErrorCount(), is(0));
  }

  @Test
  public void runTest_CanRunSuccessTestOfInheritClass() {
    WjrJUnit3Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestJUnit3Inherit.class,
        "testSuccessMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getErrorCount(), is(0));
  }

  @Test
  public void applyResult_WillBeErrorState_WithError() {
    WjrJUnit3Result result = new WjrJUnit3Result(null, 1234) {
      @Override
      public int getRunCount() {
        return 1;
      }

      @Override
      public int getFailureCount() {
        return 0;
      }

      @Override
      public int getErrorCount() {
        return 1;
      }

      @Override
      public List<TestFailure> getErrors() {
        return Arrays.asList(new TestFailure[] { new TestFailure(
          null,
          new Exception()) });
      }
    };

    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestJUnit3.class.getName(), "dummy");
    methodRunner.applyResult(methodItem, result);

    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.getTime(), is("1234"));
  }

  @Test
  public void applyResult_WillBeFailureState_WithFailure() {
    WjrJUnit3Result result = new WjrJUnit3Result(null, 1234) {
      @Override
      public int getRunCount() {
        return 1;
      }

      @Override
      public int getFailureCount() {
        return 1;
      }

      @Override
      public int getErrorCount() {
        return 0;
      }

      @Override
      public List<TestFailure> getFailures() {
        return Arrays.asList(new TestFailure[] { new TestFailure(
          null,
          new AssertionFailedError()) });
      }
    };

    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.class.getName(), "successMethod");
    methodRunner.applyResult(methodItem, result);

    assertThat(methodItem.getState(), is(State.FAILURE));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.getTime(), is("1234"));
  }

  @Test
  public void applyResult_WillBeSuccessState_WithoutFailureAndError() {
    WjrJUnit3Result result = new WjrJUnit3Result(null, 1234) {
      @Override
      public int getRunCount() {
        return 1;
      }

      @Override
      public int getFailureCount() {
        return 0;
      }

      @Override
      public int getErrorCount() {
        return 0;
      }
    };

    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.class.getName(), "successMethod");
    methodRunner.applyResult(methodItem, result);

    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
    assertThat(methodItem.getTime(), is("1234"));
  }

  @Test(expected = NullPointerException.class)
  public void runWjrMethod_WillThrowNPE_WhenWjrMethodItemIsNull() {
    methodRunner.runWjrMethod(null);
  }

  @Test
  public void runWjrMethod_CanRunSucceessMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestJUnit3.class.getName(), "testSuccessMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_CanRunFailureMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestJUnit3.class.getName(), "testFailureMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.FAILURE));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_CanRunErrorMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestJUnit3.class.getName(), "testErrorMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_CanRunInnerStaticClassMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(
        ForTestJUnit3.ForTestJUnit3InnerStatic.class.getName(),
        "testSuccessMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_CanRunInheritClassMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(
        ForTestJUnit3Inherit.class.getName(),
        "testSuccessMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_WillError_WithNotExistMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestJUnit3.class.getName(), "notExistMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.FAILURE));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_WillSetErrorToItem_WhenExceptionOccur() {
    WjrMethodItem methodItem =
      new WjrMethodItem("NotExistClass", "successMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not("")));
    assertThat(methodItem.isOverQuota(), is(false));
  }

  @Test
  public void runWjrMethod_WillSetQuotaOver_WhenOverQuotaExceptionOccur() {
    WjrMethodItem methodItem =
      new WjrMethodItem(
        ForTestJUnit3.class.getName(),
        "testOverQuotaExceptionMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not("")));
    assertThat(methodItem.isOverQuota(), is(true));
  }
}
