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
package bufferings.ktr.wjr.server.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import org.junit.Test;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

import bufferings.ktr.wjr.server.service.fortest.ForTest;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

public class WjrMethodRunnerTest {

  private WjrMethodRunner wjrMethodRunner = new WjrMethodRunner();

  @Test
  public void runWjrMethod_CanRunWjrMethod() {
    WjrMethodItem wjrMethodItem =
      new WjrMethodItem(ForTest.class.getCanonicalName(), "normalMethod");
    wjrMethodRunner.runWjrMethod(wjrMethodItem);
    assertThat(wjrMethodItem.getState(), is(State.SUCCESS));
    assertThat(wjrMethodItem.getTrace(), is(nullValue()));
  }

  @Test
  public void getTrace_CanGetTrace() {
    assertThat(wjrMethodRunner.getTrace(new Exception()), is(not(nullValue())));
  }

  @Test
  public void loadClass_CanLoadClass() {
    assertThat(
      wjrMethodRunner.loadClass(ForTest.class.getCanonicalName()),
      is(not(nullValue())));
  }

  @Test(expected = RuntimeException.class)
  public void loadClass_WillThrowRTE_WhenCannotLoadClass() {
    wjrMethodRunner.loadClass(ForTest.class.getCanonicalName() + "XXX");
  }

  @Test
  public void getRunner_CanGetRunner() {
    assertThat(
      wjrMethodRunner.getRunner(ForTest.class, "normalMethod"),
      is(not(nullValue())));
  }

  @Test
  public void runTest_CanRunTest() {
    Result result =
      wjrMethodRunner.runTest(wjrMethodRunner.getRunner(
        ForTest.class,
        "normalMethod"));
    assertThat(result.getRunCount(), is(1));
    assertThat(result.getFailureCount(), is(0));
    assertThat(result.getIgnoreCount(), is(0));
  }

  @Test
  public void applyResult_WillBeErrorState_WithException() {
    Logger logger = Logger.getLogger("test");
    logger.info("ちょっとテスト");
    
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

    WjrMethodItem wjrMethodItem =
      new WjrMethodItem(ForTest.class.getCanonicalName(), "normalMethod");
    wjrMethodRunner.applyResult(wjrMethodItem, result);

    assertThat(wjrMethodItem.getState(), is(State.ERROR));
    assertThat(wjrMethodItem.getTrace(), is(not(nullValue())));
    assertThat(wjrMethodItem.getTime(), is("1.234"));
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

    WjrMethodItem wjrMethodItem =
      new WjrMethodItem(ForTest.class.getCanonicalName(), "normalMethod");
    wjrMethodRunner.applyResult(wjrMethodItem, result);

    assertThat(wjrMethodItem.getState(), is(State.FAILURE));
    assertThat(wjrMethodItem.getTrace(), is(not(nullValue())));
    assertThat(wjrMethodItem.getTime(), is("1.234"));
  }

  @Test
  public void applyResult_WillBeSucceedState_WithoutFailure() {
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

    WjrMethodItem wjrMethodItem =
      new WjrMethodItem(ForTest.class.getCanonicalName(), "normalMethod");
    wjrMethodRunner.applyResult(wjrMethodItem, result);

    assertThat(wjrMethodItem.getState(), is(State.SUCCESS));
    assertThat(wjrMethodItem.getTrace(), is(nullValue()));
    assertThat(wjrMethodItem.getTime(), is("1.234"));
  }
}
