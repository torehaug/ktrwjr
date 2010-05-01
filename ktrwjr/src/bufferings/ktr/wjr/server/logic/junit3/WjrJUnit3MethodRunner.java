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
package bufferings.ktr.wjr.server.logic.junit3;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.util.WjrUtils;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

/**
 * The test runner of the {@link WjrMethodItem}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnit3MethodRunner implements WjrMethodRunner {

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runWjrMethod(WjrMethodItem methodItem) {
    checkNotNull(methodItem, "The methodItem parameter is null.");
    try {
      Class<?> clazz = loadClass(methodItem.getClassName());
      Test runner = getRunner(clazz, methodItem.getMethodName());
      WjrJUnit3Result result = runTest(runner);
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
  @SuppressWarnings("unchecked")
  protected Test getRunner(Class<?> clazz, String methodName) {
    if (!TestCase.class.isAssignableFrom(clazz)) {
      throw new IllegalArgumentException(
        "TestCase is not assignable from the class.");
    }

    Class<? extends TestCase> testCaseClass = (Class<? extends TestCase>) clazz;
    return TestSuite.createTest(testCaseClass, methodName);
  }

  /**
   * Runs the test runner.
   * 
   * @param runner
   *          The test runner.
   * @return The test result.
   */
  protected WjrJUnit3Result runTest(Test runner) {
    TestResult result = new TestResult();
    long before = System.currentTimeMillis();
    runner.run(result);
    Long time = System.currentTimeMillis() - before;
    return new WjrJUnit3Result(result, time);
  }

  /**
   * Applys the result to the methodItem.
   * 
   * @param methodItem
   *          The methodItem to be applied the result.
   * @param result
   *          The test result.
   */
  protected WjrMethodItem applyResult(WjrMethodItem methodItem,
      WjrJUnit3Result result) {
    if (result.getFailureCount() > 0) {
      methodItem.setState(State.FAILURE);
      methodItem.setTrace(result.getFailures().get(0).trace());
    } else if (result.getErrorCount() > 0) {
      methodItem.setState(State.ERROR);
      methodItem.setTrace(result.getErrors().get(0).trace());
    } else {
      methodItem.setState(State.SUCCESS);
      methodItem.setTrace("");
    }

    methodItem.setTime(Long.toString(result.getRunTime()));
    return methodItem;
  }

}
