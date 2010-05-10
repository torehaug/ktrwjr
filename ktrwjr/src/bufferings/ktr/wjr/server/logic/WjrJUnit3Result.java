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

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import junit.framework.TestFailure;
import junit.framework.TestResult;

/**
 * A wrapper of the {@link TestResult}.
 * 
 * This class has time attribute in addition to the {@link TestResult}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnit3Result {

  /**
   * The test result.
   */
  protected TestResult result;

  /**
   * The time taken to run the test.
   */
  protected long runTime;

  /**
   * Constructs the WjrJUnit3Result with TestResult and run time.
   * 
   * @param result
   *          The test result.
   * @param runTime
   *          The run time
   */
  public WjrJUnit3Result(TestResult result, long runTime) {
    this.result = result;
    this.runTime = runTime;
  }

  /**
   * Gets the run count.
   * 
   * @return The run count.
   */
  public int getRunCount() {
    return result.runCount();
  }

  /**
   * Gets the failure count.
   * 
   * @return The failure count.
   */
  public int getFailureCount() {
    return result.failureCount();
  }

  /**
   * Gets the error count.
   * 
   * @return The error count.
   */
  public int getErrorCount() {
    return result.errorCount();
  }

  /**
   * Gets the run time.
   * 
   * @return The run time.
   */
  public long getRunTime() {
    return runTime;
  }

  /**
   * Get the failures.
   * 
   * @return The failures.
   */
  public List<TestFailure> getFailures() {
    List<TestFailure> list = new ArrayList<TestFailure>();
    for (Enumeration<TestFailure> e = result.failures(); e.hasMoreElements();) {
      list.add(e.nextElement());
    }
    return list;
  }

  /**
   * Get the errors.
   * 
   * @return The errors.
   */
  public List<TestFailure> getErrors() {
    List<TestFailure> list = new ArrayList<TestFailure>();
    for (Enumeration<TestFailure> e = result.errors(); e.hasMoreElements();) {
      list.add(e.nextElement());
    }
    return list;
  }
}
