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

import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.server.util.WjrUtils;

/**
 * Configuration property class.
 * 
 * Loads the ktrwjr.properties in the resource. The only property is "timezone"
 * which is used for converting time of logs.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrConfig {

  protected static final String KEY_TIMEZONE = "tz";

  protected static final String DEFAULT_TIMEZONE = "PST";

  /**
   * Gets the timezone property for logs.
   * 
   * If the timezone parameter is not set, the
   * {@link KtrWjrConfig#DEFAULT_TIMEZONE} is used.
   * 
   * @param parameterMap
   *          GET parameter map.
   * @return The timezone property for logs.
   */
  public static String getTimezone(Map<String, List<String>> parameterMap) {
    if (parameterMap == null) {
      return DEFAULT_TIMEZONE;
    }

    List<String> tz = parameterMap.get(KEY_TIMEZONE);
    if (tz == null || tz.size() == 0) {
      return DEFAULT_TIMEZONE;
    }

    return WjrUtils.toString(tz.get(0), DEFAULT_TIMEZONE);
  }

}
