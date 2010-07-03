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
public class WjrParamParser {

  protected static final String KEY_TIMEZONE = "tz";

  protected static final String KEY_CONFIG_ID = "config";

  protected static final String DEFAULT_TIMEZONE = "PST";

  protected static final String DEFAULT_CONFIG_ID = "default";

  /**
   * Gets the configuration ID property.
   * 
   * If the configuration ID parameter is not set, the
   * {@link WjrParamParser#DEFAULT_CONFIG_ID} is used.
   * 
   * @param parameterMap
   *          GET parameter map.
   * @return The configuration ID property for logs.
   */
  public String getConfigId(Map<String, List<String>> parameterMap) {
    if (parameterMap == null) {
      return DEFAULT_CONFIG_ID;
    }

    List<String> config = parameterMap.get(KEY_CONFIG_ID);
    if (config == null || config.size() == 0) {
      return DEFAULT_CONFIG_ID;
    }

    return WjrUtils.toString(config.get(0), DEFAULT_CONFIG_ID);
  }

  /**
   * Gets the timeZoneId property for logs.
   * 
   * If the timeZoneId parameter is not set, the
   * {@link WjrParamParser#DEFAULT_TIMEZONE} is used.
   * 
   * @param parameterMap
   *          GET parameter map.
   * @return The timezone property for logs.
   */
  public String getTimeZoneId(Map<String, List<String>> parameterMap) {
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
