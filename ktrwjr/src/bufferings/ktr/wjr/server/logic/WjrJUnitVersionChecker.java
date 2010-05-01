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

import junit.runner.Version;

/**
 * The JUnit version checker.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnitVersionChecker {

  /**
   * Keeps whether JUnit version 4 is available or not.
   */
  protected Boolean junit4Available = null;

  /**
   * Keeps whether JUnit version 3 is available or not.
   */
  protected Boolean junit3Available = null;

  /**
   * Gets whether JUnit version 4 is available or not.
   * 
   * @return Whether JUnit version 4 is available or not.
   */
  public boolean isJUnit4Available() {
    if (junit4Available == null) {
      String id = getJUnitVersionId();
      junit4Available = id.startsWith("4");
    }
    return junit4Available;
  }

  /**
   * Gets whether JUnit version 3 is available or not.
   * 
   * @return Whether JUnit version 3 is available or not.
   */
  public boolean isJUnit3Available() {
    if (junit3Available == null) {
      String id = getJUnitVersionId();
      junit3Available = id.startsWith("3");
    }
    return junit3Available;
  }

  /**
   * Gets the JUnit version ID.
   * 
   * @return The JUnit version ID.
   */
  protected String getJUnitVersionId() {
    return Version.id();
  }
}