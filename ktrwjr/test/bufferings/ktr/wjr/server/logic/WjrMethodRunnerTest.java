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

import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.server.fortest.ForTestInherit;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

public class WjrMethodRunnerTest {

  private WjrMethodRunner methodRunner = new WjrMethodRunner();

  @Test
  public void getTrace_WillReturnEmptyString_WhenExceptionIsNull() {
    assertThat(methodRunner.getTrace(null), is(""));
  }

  @Test
  public void getTrace_CanGetTrace() {
    assertThat(methodRunner.getTrace(new Exception()), is(not(nullValue())));
  }

  @Test
  public void getRunner_WontBeError_WhenMethodNotExist() {
    assertThat(
      methodRunner.getRunner(ForTest.class, "notExistMethod"),
      is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunner() {
    assertThat(
      methodRunner.getRunner(ForTest.class, "succeedMethod"),
      is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunnerOfInnerStaticClass() {
    assertThat(methodRunner.getRunner(
      ForTest.ForTestInnerStatic.class,
      "succeedMethod"), is(not(nullValue())));
  }

  @Test
  public void getRunner_CanGetRunnerOfInheritClass() {
    assertThat(
      methodRunner.getRunner(ForTestInherit.class, "succeedMethod"),
      is(not(nullValue())));
  }

  @Test
  public void runTest_WillFailure_WhenMethodNotExist() {
    Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTest.class,
        "notExistMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(1));
    assertThat(result.getIgnoreCount(), is(0));
  }

  @Test
  public void runTest_CanRunSuccessTest() {
    Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTest.class,
        "successMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getIgnoreCount(), is(0));
  }

  @Test
  public void runTest_CanRunFailureTest() {
    Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTest.class,
        "failureMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(1));
    assertThat(result.getIgnoreCount(), is(0));
    assertThat(
      result.getFailures().get(0).getException() instanceof AssertionError,
      is(true));
  }

  @Test
  public void runTest_CanRunErrorTest() {
    Result result =
      methodRunner
        .runTest(methodRunner.getRunner(ForTest.class, "errorMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(1));
    assertThat(result.getIgnoreCount(), is(0));
    assertThat(
      result.getFailures().get(0).getException() instanceof AssertionError,
      is(false));
  }

  @Test
  public void runTest_CanRunSuccessTestOfInnerStaticClass() {
    Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTest.ForTestInnerStatic.class,
        "successMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getIgnoreCount(), is(0));
  }

  @Test
  public void runTest_CanRunSuccessTestOfInheritClass() {
    Result result =
      methodRunner.runTest(methodRunner.getRunner(
        ForTestInherit.class,
        "successMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getIgnoreCount(), is(0));
  }

  @Test
  public void applyResult_WillBeErrorState_WithException() {
    Result result = new Result() {
      @Override
      public int getFailureCount() {
        return 1;
      }

      @Override
      public List<Failure> getFailures() {
        return Arrays
          .asList(new Failure[] { new Failure(null, new Exception()) });
      }

      @Override
      public long getRunTime() {
        return 1234;
      }
    };

    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.class.getName(), "successMethod");
    methodRunner.applyResult(methodItem, result);

    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
    assertThat(methodItem.getTime(), is("1234"));
  }

  @Test
  public void applyResult_WillBeFailureState_WithAssertionError() {
    Result result = new Result() {
      @Override
      public int getFailureCount() {
        return 1;
      }

      @Override
      public List<Failure> getFailures() {
        return Arrays.asList(new Failure[] { new Failure(
          null,
          new AssertionError()) });
      }

      @Override
      public long getRunTime() {
        return 1234;
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
  public void applyResult_WillBeSuccessState_WithoutFailure() {
    Result result = new Result() {
      @Override
      public int getFailureCount() {
        return 0;
      }

      @Override
      public long getRunTime() {
        return 1234;
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
      new WjrMethodItem(ForTest.class.getName(), "successMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
  }

  @Test
  public void runWjrMethod_CanRunFailureMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.class.getName(), "failureMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.FAILURE));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
  }

  @Test
  public void runWjrMethod_CanRunErrorMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.class.getName(), "errorMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not(nullValue())));
  }

  @Test
  public void runWjrMethod_CanRunInnerStaticClassMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTest.ForTestInnerStatic.class.getName(), "successMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
  }

  @Test
  public void runWjrMethod_CanRunInheritClassMethod() {
    WjrMethodItem methodItem =
      new WjrMethodItem(ForTestInherit.class.getName(), "successMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.SUCCESS));
    assertThat(methodItem.getTrace(), is(""));
  }

  @Test
  public void runWjrMethod_WillSetErrorToItem_WhenExceptionOccur() {
    WjrMethodItem methodItem =
      new WjrMethodItem("NotExistClass", "successMethod");
    methodRunner.runWjrMethod(methodItem);
    assertThat(methodItem.getState(), is(State.ERROR));
    assertThat(methodItem.getTrace(), is(not("")));
  }
}
