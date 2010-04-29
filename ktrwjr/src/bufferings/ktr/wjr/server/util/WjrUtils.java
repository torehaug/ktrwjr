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
package bufferings.ktr.wjr.server.util;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.TimeZone;

/**
 * Utility methods.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrUtils {

  /**
   * Loads the class from the class name.
   * 
   * @param className
   *          The class name.
   * @return The loaded class.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws ClassNotFoundRuntimeException
   *           When the class is not found.
   */
  public static Class<?> loadClass(String className) {
    checkNotNull(className, "The className parameter is null.");
    try {
      return Thread
        .currentThread()
        .getContextClassLoader()
        .loadClass(className);
    } catch (ClassNotFoundException e1) {
      throw new ClassNotFoundRuntimeException("Cannot load class("
        + className
        + ").", e1);
    }
  }

  /**
   * Convert the object to string.
   * 
   * @param o
   *          The object to convert to string.
   * @param def
   *          The default value if the object is null.
   * @return The converted string.
   */
  public static String toString(Object o, String def) {
    return o == null ? def : o.toString();
  }

  /**
   * Gets the time zone form id.
   * 
   * @param timeZoneId
   *          The time zone id.
   * @return The time zone.
   * @throws NullPointerException
   *           When the timeZoneId is null.
   */
  public static TimeZone getTimeZone(String timeZoneId) {
    return TimeZone.getTimeZone(timeZoneId);
  }

  private WjrUtils() {
  };
}
