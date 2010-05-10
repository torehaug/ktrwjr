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


/**
 * The logic generator which matches the JUnit version.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnitLogicFactory {

  /**
   * The version checker.
   */
  protected static WjrJUnitVersionChecker versionChecker;

  /**
   * Gets the method runnner which matches the version of the using JUnit.
   * 
   * @return The method runner.
   * @throws IllegalStateException
   *           When both JUnit4 and JUnit3 are not available.
   */
  public static WjrMethodRunner getMethodRunner() {
    WjrJUnitVersionChecker checker = getVersionChecker();

    if (checker.isJUnit4Available()) {
      return new WjrJUnit4MethodRunner();
    } else if (checker.isJUnit3Available()) {
      return new WjrJUnit3MethodRunner();
    } else {
      throw new IllegalStateException(
        "Both JUnit4 and JUnit3 are not available.");
    }
  }

  /**
   * Gets the store loader which matches the version of the using JUnit.
   * 
   * @return The store loader.
   * @throws IllegalStateException
   *           When both JUnit4 and JUnit3 are not available.
   */
  public static WjrStoreLoader getStoreLoader() {
    WjrJUnitVersionChecker checker = getVersionChecker();

    if (checker.isJUnit4Available()) {
      return new WjrJUnit4StoreLoader();
    } else if (checker.isJUnit3Available()) {
      return new WjrJUnit3StoreLoader();
    } else {
      throw new IllegalStateException(
        "Both JUnit4 and JUnit3 are not available.");
    }
  }

  /**
   * Gets the version checker.
   * 
   * @return The version checker.
   */
  protected static WjrJUnitVersionChecker getVersionChecker() {
    if (versionChecker == null) {
      versionChecker = new WjrJUnitVersionChecker();
    }
    return versionChecker;
  }
}
