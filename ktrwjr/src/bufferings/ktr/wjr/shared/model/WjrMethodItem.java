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

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

/**
 * The test case class method information.
 * 
 * This class keeps the test method run result information.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrMethodItem extends WjrStoreItem {

  /**
   * The separator between the class name and the method name.
   */
  protected static final String CLASS_METHOD_SEPARATOR = "#";

  /**
   * The class name.
   */
  protected String className;

  /**
   * The method name.
   */
  protected String methodName;

  /**
   * The class name and the method name binded with
   * {@link WjrMethodItem#CLASS_METHOD_SEPARATOR}.
   */
  protected String classAndMethodName;

  /**
   * The trace string of the run test. This cannot be null.
   */
  protected String trace = "";

  /**
   * The log string of the run test. This cannot be null.
   */
  protected String log = "";

  /**
   * The real time the run test took. The unit is [ms]. This cannot be null.
   */
  protected String time = "";

  /**
   * The cpu time the run test took. The unit is [cpu_ms]. This cannot be null.
   */
  protected String cpuTime = "";

  /**
   * The api time the run test took. The unit is [api_cpu_ms]. This cannot be
   * null.
   */
  protected String apiTime = "";

  /**
   * Constructor for only GWT-RPC serialization.
   */
  @SuppressWarnings("unused")
  private WjrMethodItem() {
  }

  /**
   * Constructs with the class name and the method name.
   * 
   * @param className
   *          The class name.
   * @param methodName
   *          The method name.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws IllegalArgumentException
   *           When the className parameter is empty.
   * @throws NullPointerException
   *           When the methodName parameter is null.
   * @throws IllegalArgumentException
   *           When the methodName parameter is empty.
   */
  public WjrMethodItem(String className, String methodName) {
    checkNotNull(className, "The className parameter is null.");
    checkArgument(className.length() > 0, "The className parameter is empty.");
    checkNotNull(methodName, "The methodName parameter is null.");
    checkArgument(methodName.length() > 0, "The methodName parameter is empty.");

    this.className = className;
    this.methodName = methodName;
    this.classAndMethodName = className + CLASS_METHOD_SEPARATOR + methodName;
  }

  /**
   * Gets the class name.
   * 
   * @return The class name.
   */
  public String getClassName() {
    return className;
  }

  /**
   * Gets the class name and the method name binded with
   * {@link WjrMethodItem#CLASS_METHOD_SEPARATOR}
   * 
   * @return The class item name and the method name binded with
   *         {@link WjrMethodItem#CLASS_METHOD_SEPARATOR}
   */
  public String getClassAndMethodName() {
    return classAndMethodName;
  }

  /**
   * Gets the method name.
   * 
   * @return The method name.
   */
  public String getMethodName() {
    return methodName;
  }

  /**
   * Gets the trace string of the run test. This cannot be null.
   * 
   * @return The trace string of the run test.
   */
  public String getTrace() {
    return trace;
  }

  /**
   * Sets the trace string of the run test. If the trace parameter is null, the
   * empty string is set.
   * 
   * @param trace
   *          The trace string of the run test.
   */
  public void setTrace(String trace) {
    this.trace = (trace != null ? trace : "");
  }

  /**
   * Gets the log string of the run test. This cannot be null.
   * 
   * @return The log string of the run test.
   */
  public String getLog() {
    return log;
  }

  /**
   * Sets the log string of the run test. If the log parameter is null, the
   * empty string is set.
   * 
   * @param log
   *          The log string of the run test.
   */
  public void setLog(String log) {
    this.log = (log != null ? log : "");
  }

  /**
   * Gets the real time the run test took. The unit is [ms]. This cannot be
   * null.
   * 
   * @return The real time the run test took.
   */
  public String getTime() {
    return time;
  }

  /**
   * Sets the real time the run test took. The unit is [ms]. If the time
   * parameter is null, the empty string is set.
   * 
   * @param time
   *          The real time the run test took. The unit is [ms].
   */
  public void setTime(String time) {
    this.time = (time != null ? time : "");
  }

  /**
   * Gets the cpu time the run test took. The unit is [cpu_ms]. This cannot be
   * null.
   * 
   * @return The cpu time the run test took.
   */
  public String getCpuTime() {
    return cpuTime;
  }

  /**
   * Sets the cpu time the run test took. The unit is [cpu_ms]. If the cpuTime
   * parameter is null, the empty string is set.
   * 
   * @param cpuTime
   *          The cpu time the run test took. The unit is [cpu_ms].
   */
  public void setCpuTime(String cpuTime) {
    this.cpuTime = (cpuTime != null ? cpuTime : "");
  }

  /**
   * Gets the api time the run test took. The unit is [api_cpu_ms]. This cannot
   * be null.
   * 
   * @return The api time the run test took.
   */
  public String getApiTime() {
    return apiTime;
  }

  /**
   * Sets the api time the run test took. The unit is [api_cpu_ms]. If the
   * apiTime parameter is null, the empty string is set.
   * 
   * @param apiTime
   *          The api time the run test took. The unit is [api_cpu_ms].
   */
  public void setApiTime(String apiTime) {
    this.apiTime = (apiTime != null ? apiTime : "");
  }

  /**
   * Clears the test result.
   */
  public void clearResult() {
    state = State.NOT_YET;
    trace = "";
    log = "";
    time = "";
    cpuTime = "";
    apiTime = "";
  }

}
