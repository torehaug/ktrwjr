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

import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.runner.Request;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;

import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

public class WjrMethodRunner {

  public WjrMethodItem runWjrMethod(WjrMethodItem methodItem) {
    try {
      Class<?> clazz = loadClass(methodItem.getClassCanonicalName());
      Runner runner = getRunner(clazz, methodItem.getMethodSimpleName());
      Result result = runTest(runner);
      return applyResult(methodItem, result);
    } catch (Exception e) {
      methodItem.setState(State.ERROR);
      methodItem.setTrace(getTrace(e));
      return methodItem;
    }
  }

  protected String getTrace(Exception e) {
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    e.printStackTrace(writer);
    StringBuffer buffer = stringWriter.getBuffer();
    return buffer.toString();
  }

  protected Class<?> loadClass(String classCanonicalName) {
    try {
      return Thread.currentThread().getContextClassLoader().loadClass(
        classCanonicalName);
    } catch (ClassNotFoundException e1) {
      throw new RuntimeException("Cannot load class("
        + classCanonicalName
        + ").", e1);
    }
  }

  protected Runner getRunner(Class<?> clazz, String methodName) {
    return Request.method(clazz, methodName).getRunner();
  }

  protected Result runTest(Runner runner) {
    Result result = new Result();

    RunNotifier notifier = new RunNotifier();
    notifier.addFirstListener(result.createListener());

    notifier.fireTestRunStarted(runner.getDescription());
    runner.run(notifier);
    notifier.fireTestRunFinished(result);

    return result;
  }

  protected WjrMethodItem applyResult(WjrMethodItem methodItem, Result result) {
    if (result.getFailureCount() > 0) {
      if (result.getFailures().get(0).getException() instanceof AssertionError) {
        methodItem.setState(State.FAILURE);
      } else {
        methodItem.setState(State.ERROR);
      }
      methodItem.setTrace(result.getFailures().get(0).getTrace());
    } else {
      methodItem.setState(State.SUCCESS);
      methodItem.setTrace(null);
    }

    methodItem.setTime(Long.toString(result.getRunTime()));
    return methodItem;
  }

}
