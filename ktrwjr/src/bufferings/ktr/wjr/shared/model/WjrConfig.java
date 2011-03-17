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


/**
 * The configration.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrConfig {

  /**
   * The default value of configId.
   */
  public static final String DEFAULT_CONFIG_ID = "default";

  /**
   * The default value of configName.
   */
  public static final String DEFAULT_CONFIG_NAME = "No Name";

  /**
   * The default value of cpumsEnabled.
   */
  public static final boolean DEFAULT_CPUMS_ENABLED = true;

  /**
   * The default value of apimsEnabled.
   */
  public static final boolean DEFAULT_APIMS_ENABLED = true;

  /**
   * The default value of logHookEnabled.
   */
  public static final boolean DEFAULT_LOGHOOK_ENABLED = true;

  /**
   * The default value of logHookTimezone.
   */
  public static final String DEFAULT_LOGHOOK_TIMEZONE = "PST";

  /**
   * The default value of retryOverQuotaEnabled.
   */
  public static final boolean DEFAULT_RETRYOVERQUOTA_ENABLED = false;

  /**
   * The default value of runParallel.
   */
  public static final boolean DEFAULT_RUNPARALLEL = false;

  /**
   * The default value of retryOverQuotaInterval.
   */
  public static final int DEFAULT_RETRYOVERQUOTA_INTERVAL = 0;

  /**
   * The default value of retryOverQuotaMaxCount.
   */
  public static final int DEFAULT_RETRYOVERQUOTA_MAXCOUNT = 0;

  /**
   * The min value of retryOverQuotaInterval.
   */
  public static final int RETRYOVERQUOTA_INTERVAL_MIN = 0;

  /**
   * The max value of retryOverQuotaInterval.
   */
  public static final int RETRYOVERQUOTA_INTERVAL_MAX = 1000000;

  /**
   * The min value of retryOverQuotaMaxCount.
   */
  public static final int RETRYOVERQUOTA_MAXCOUNT_MIN = 0;

  /**
   * The max value of retryOverQuotaMaxCount.
   */
  public static final int RETRYOVERQUOTA_MAXCOUNT_MAX = 100;

  /**
   * The configuration id.
   */
  protected String configId = DEFAULT_CONFIG_ID;

  /**
   * The configuration name.
   */
  protected String configName = DEFAULT_CONFIG_NAME;

  /**
   * Whether the cpu_ms watching is enabled.
   */
  protected boolean cpumsEnabled = DEFAULT_CPUMS_ENABLED;

  /**
   * Whether the api_cpu_ms watching is enabled.
   */
  protected boolean apimsEnabled = DEFAULT_APIMS_ENABLED;

  /**
   * Whether the log hooking is enabled.
   */
  protected boolean logHookEnabled = DEFAULT_LOGHOOK_ENABLED;

  /**
   * The log hooking timezone.
   */
  protected String logHookTimezone = DEFAULT_LOGHOOK_TIMEZONE;

  /**
   * Whether the retrying over quota is enabled.
   */
  protected boolean retryOverQuotaEnabled = DEFAULT_RETRYOVERQUOTA_ENABLED;

  /**
   * The interval seconds before retrying the test when the over quota exception
   * occures.
   */
  protected int retryOverQuotaInterval = DEFAULT_RETRYOVERQUOTA_INTERVAL;

  /**
   * The max retrying count when the over quota exception occures.
   */
  protected int retryOverQuotaMaxCount = DEFAULT_RETRYOVERQUOTA_MAXCOUNT;

  /**
   * Gets the configuration id.
   * 
   * @return The configuration id.
   */
  public String getConfigId() {
    return configId;
  }

  /**
   * Sets the configuration id.
   * 
   * @param value
   *          The configuration id.
   */
  public void setConfigId(String value) {
    configId = parseString(value, DEFAULT_CONFIG_ID);
  }

  /**
   * Gets the configuration name.
   * 
   * @return The configuration name.
   */
  public String getConfigName() {
    return configName;
  }

  /**
   * Sets the configuration name.
   * 
   * @param value
   *          The configuration name.
   */
  public void setConfigName(String value) {
    configName = parseString(value, DEFAULT_CONFIG_ID);
  }

  /**
   * Gets whether the cpu_ms watching is enabled.
   * 
   * @return Whether the cpu_ms watching is enabled.
   */
  public boolean isCpumsEnabled() {
    return cpumsEnabled;
  }

  /**
   * Sets whether the cpu_ms watching is enabled.
   * 
   * @param value
   *          Whether the cpu_ms watching is enabled.
   */
  public void setCpumsEnabled(String value) {
    cpumsEnabled = parseBoolean(value, DEFAULT_CPUMS_ENABLED);
  }

  /**
   * Gets whether the api_cpu_ms watching is enabled.
   * 
   * @return Whether the api_cpu_ms watching is enabled.
   */
  public boolean isApimsEnabled() {
    return apimsEnabled;
  }

  /**
   * Sets whether the api_cpu_ms watching is enabled.
   * 
   * @param value
   *          Whether the api_cpu_ms watching is enabled.
   */
  public void setApimsEnabled(String value) {
    apimsEnabled = parseBoolean(value, DEFAULT_APIMS_ENABLED);
  }

  /**
   * Gets whether the log hooking is enabled.
   * 
   * @return Whether the log hooking is enabled.
   */
  public boolean isLogHookEnabled() {
    return logHookEnabled;
  }

  /**
   * Sets whether the log hooking is enabled.
   * 
   * @param logHookEnabled
   *          Whether the log hooking is enabled.
   */
  public void setLogHookEnabled(String value) {
    logHookEnabled = parseBoolean(value, DEFAULT_LOGHOOK_ENABLED);
  }

  /**
   * Gets the log hooking timezone.
   * 
   * @return The log hooking timezone.
   */
  public String getLogHookTimezone() {
    return logHookTimezone;
  }

  /**
   * Sets the log hooking timezone.
   * 
   * @param value
   *          The log hooking timezone.
   */
  public void setLogHookTimezone(String value) {
    logHookTimezone = parseString(value, DEFAULT_LOGHOOK_TIMEZONE);
  }

  /**
   * Gets whether the retrying over quota is enabled.
   * 
   * @return Whether the retrying over quota is enabled.
   */
  public boolean isRetryOverQuotaEnabled() {
    return retryOverQuotaEnabled;
  }

  /**
   * Sets whether the retrying over quota is enabled.
   * 
   * @param value
   *          Whether the retrying over quota is enabled.
   */
  public void setRetryOverQuotaEnabled(String value) {
    retryOverQuotaEnabled = parseBoolean(value, DEFAULT_RETRYOVERQUOTA_ENABLED);
  }

  /**
   * Gets the interval seconds before retrying the test when the over quota
   * exception
   * 
   * @return The interval seconds before retrying the test when the over quota
   *         exception
   */
  public int getRetryOverQuotaInterval() {
    return retryOverQuotaInterval;
  }

  /**
   * Sets the interval seconds before retrying the test when the over quota
   * exception
   * 
   * @param value
   *          The interval seconds before retrying the test when the over quota
   *          exception
   */
  public void setRetryOverQuotaInterval(String value) {
    int temp = parseInt(value, DEFAULT_RETRYOVERQUOTA_INTERVAL);
    retryOverQuotaInterval =
      checkRange(temp, RETRYOVERQUOTA_INTERVAL_MIN, RETRYOVERQUOTA_INTERVAL_MAX);
  }

  /**
   * Gets the max retrying count when the over quota exception occures.
   * 
   * @return The max retrying count when the over quota exception occures.
   */
  public int getRetryOverQuotaMaxCount() {
    return retryOverQuotaMaxCount;
  }

  /**
   * Sets the max retrying count when the over quota exception occures.
   * 
   * @param value
   *          The max retrying count when the over quota exception occures.
   */
  public void setRetryOverQuotaMaxCount(String value) {
    int temp = parseInt(value, DEFAULT_RETRYOVERQUOTA_MAXCOUNT);
    retryOverQuotaMaxCount =
      checkRange(temp, RETRYOVERQUOTA_MAXCOUNT_MIN, RETRYOVERQUOTA_MAXCOUNT_MAX);
  }

  /**
   * Parses the string value.
   * 
   * If the value is null or empty, returns the defaultValue, otherwise returns
   * the value.
   * 
   * @param value
   *          The value to parse.
   * @param defaultValue
   *          The default value.
   * @return If the value is null or empty, returns the defaultValue, otherwise
   *         returns the value.
   */
  protected String parseString(String value, String defaultValue) {
    if (value == null || value.length() == 0) {
      return defaultValue;
    }
    return value;
  }

  /**
   * Parses the boolean value.
   * 
   * If the value equals to "true", returns true. If the value equals to
   * "false", returns false. Otherwise, returns the default value.
   * 
   * @param value
   *          The value to parse.
   * @param defaultValue
   *          The default value.
   * @return If the value equals to "true", returns true. If the value equals to
   *         "false", returns false. Otherwise, returns the default value.
   */
  protected boolean parseBoolean(String value, boolean defaultValue) {
    if ("true".equals(value)) {
      return true;
    } else if ("false".equals(value)) {
      return false;
    } else {
      return defaultValue;
    }
  }

  /**
   * Parses the int value.
   * 
   * If the value can be parsed to int, returns the int value. Otherwise,
   * returns the default value.
   * 
   * @param value
   *          The value to parse.
   * @param defaultValue
   *          The default value.
   * @return If the value can be parsed to int, returns the int value.
   *         Otherwise, returns the default value.
   */
  protected int parseInt(String value, int defaultValue) {
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return defaultValue;
    }
  }

  /**
   * Checks the value range.
   * 
   * If the value is less than min, returns the min. If the value is greater
   * than max, returns the max. Otherwise, returns the value.
   * 
   * @param value
   *          The value to check.
   * @param min
   *          The minimum value.
   * @param max
   *          The maximum value.
   * @return If the value is less than min, returns the min. If the value is
   *         greater than max, returns the max. Otherwise, returns the value.
   */
  protected int checkRange(int value, int min, int max) {
    if (value < min) {
      return min;
    } else if (value > max) {
      return max;
    } else {
      return value;
    }
  }

  /**
   * {@inheritDoc}
   */
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("WjrConfig[");
    sb.append("configId=");
    sb.append(configId);
    sb.append(",configName=");
    sb.append(configName);
    sb.append(",cpumsEnabled=");
    sb.append(cpumsEnabled);
    sb.append(",apimsEnabled=");
    sb.append(apimsEnabled);
    sb.append(",logHookEnabled=");
    sb.append(logHookEnabled);
    sb.append(",logHookTimezone=");
    sb.append(logHookTimezone);
    sb.append(",retryOverQuotaEnabled=");
    sb.append(retryOverQuotaEnabled);
    sb.append(",retryOverQuotaInterval=");
    sb.append(retryOverQuotaInterval);
    sb.append(",retryOverQuotaMaxCount=");
    sb.append(retryOverQuotaMaxCount);
    sb.append("]");
    return sb.toString();
  }

}
