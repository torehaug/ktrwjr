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
package bufferings.ktr.wjr.shared.util;

/**
 * Configuration property class.
 * 
 * Loads the ktrwjr.properties in the resource. The only property is "timezone"
 * which is used for converting time of logs.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrParamKey {

  /**
   * The key of the method param.
   */
  public static final String KEY_METHOD = "m";

  /**
   * The key of the configuration id.
   */
  public static final String KEY_CONFIG_ID = "config";

  /**
   * The key of the run class name param.
   */
  public static final String KEY_RUN_CLASS_NAME = "class";

  /**
   * The key of the run method name param.
   */
  public static final String KEY_RUN_METHOD_NAME = "method";

  /**
   * The key of the cpums enabled.
   */
  public static final String KEY_CPUMS_ENABLED = "cpums";

  /**
   * The key of the apims enabled.
   */
  public static final String KEY_APIMS_ENABLED = "apims";

  /**
   * The key of the loghook enabled.
   */
  public static final String KEY_LOGHOOK_ENABLED = "log";

  /**
   * The key of the loghook timezone.
   */
  public static final String KEY_LOGHOOK_TIMEZONE = "tz";

  /**
   * The param value of loadConfig method.
   */
  public static final String METHOD_LOAD_CONFIG = "load_config";

  /**
   * The param value of loadStore method.
   */
  public static final String METHOD_LOAD_STORE = "load_store";

  /**
   * The param value of runTest method.
   */
  public static final String METHOD_RUN_TEST = "run_test";

}
