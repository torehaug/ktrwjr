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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.TimeZone;

/**
 * Server utility methods.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrServerUtils {

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
   * Convert the string to boolean.
   * 
   * If str is "true" then returns true, if str is "false" then returns false,
   * other case returns def.
   * 
   * @param str
   *          The string to convert to boolean.
   * @param def
   *          The default value if the string cannot be coverted.
   * @return The converted boolean.
   */
  public static boolean convertToBoolean(String str, boolean def) {
    if (str == null) {
      return def;
    }

    if (str.equals("true")) {
      return true;
    }

    if (str.equals("false")) {
      return false;
    }

    return def;
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

  /**
   * Gets the trace string from the exception.
   * 
   * If the parameter is null, returns empty string.
   * 
   * @param e
   *          The exception.
   * @return The trace string from the exception.
   */
  public static String getTraceStringFromException(Exception e) {
    if (e == null) {
      return "";
    }
    StringWriter stringWriter = new StringWriter();
    PrintWriter writer = new PrintWriter(stringWriter);
    e.printStackTrace(writer);
    return stringWriter.getBuffer().toString();
  }

  private WjrServerUtils() {
  }
}
