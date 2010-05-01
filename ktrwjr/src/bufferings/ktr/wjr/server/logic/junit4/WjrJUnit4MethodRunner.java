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
package bufferings.ktr.wjr.server.logic.junit4;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.util.WjrUtils;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

/**
 * The test runner of the {@link WjrMethodItem}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnit4MethodRunner implements WjrMethodRunner {

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runWjrMethod(WjrMethodItem methodItem) {
    checkNotNull(methodItem, "The methodItem parameter is null.");
    try {
      Class<?> clazz = loadClass(methodItem.getClassName());
      Runner runner = getRunner(clazz, methodItem.getMethodName());
      Result result = runTest(runner);
      return applyResult(methodItem, result);
    } catch (Exception e) {
      methodItem.setState(State.ERROR);
      methodItem.setTrace(getTrace(e));
      return methodItem;
    }
  }

  /**
   * Gets the trace string from the exception.
   * 
   * If the parameter is null, returns empty string.
   * 
   * @param e
   *          The exception.
   * @return The trace string from the exception.
   */
  protected String getTrace(Exception e) {
    return WjrUtils.getTraceStringFromException(e);
  }

  /**
   * Loads the class from the class name.
   * 
   * @param className
   *          The class name.
   * @return The loaded class.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws RuntimeException
   *           When the class is not found.
   */
  protected Class<?> loadClass(String className) {
    return WjrUtils.loadClass(className);
  }

  /**
   * Gets the test runner of the given test method.
   * 
   * @param clazz
   *          The class of the test method.
   * @param methodName
   *          The method name to run.
   * @return The test runner of the given test method.
   */
  protected Runner getRunner(Class<?> clazz, String methodName) {
    return Request.method(clazz, methodName).getRunner();
  }

  /**
   * Runs the test runner.
   * 
   * @param runner
   *          The test runner.
   * @return The test result.
   */
  protected Result runTest(Runner runner) {
    Result result = new Result();

    RunNotifier notifier = new RunNotifier();
    notifier.addFirstListener(result.createListener());

    notifier.fireTestRunStarted(runner.getDescription());
    runner.run(notifier);
    notifier.fireTestRunFinished(result);

    return result;
  }

  /**
   * Applys the result to the methodItem.
   * 
   * @param methodItem
   *          The methodItem to be applied the result.
   * @param result
   *          The test result.
   * @return The applied methodItem, which is the same instance as the input.
   */
  protected WjrMethodItem applyResult(WjrMethodItem methodItem, Result result) {
    if (result.getRunCount() == 0) {
      // The handling of the not exist method.

      // In the case of JUnit4 test case with JUnit4 runner,
      // the result will be error.

      // In the case of JUnit3 test case with JUnit4 runner,
      // the result will not run, so I regard the not run as
      // the not exist method here.

      // In the case of JUnit3 test case with JUnit3 runner,
      // the result will be failure.

      methodItem.setState(State.ERROR);
      methodItem.setTrace("No tests found matching Method "
        + methodItem.getMethodName()
        + "("
        + methodItem.getClassName()
        + ").");
    } else if (result.getFailureCount() > 0) {
      if (result.getFailures().get(0).getException() instanceof AssertionError) {
        methodItem.setState(State.FAILURE);
      } else {
        methodItem.setState(State.ERROR);
      }
      methodItem.setTrace(result.getFailures().get(0).getTrace());

    } else {
      methodItem.setState(State.SUCCESS);
      methodItem.setTrace("");
    }

    methodItem.setTime(Long.toString(result.getRunTime()));
    return methodItem;
  }

}
