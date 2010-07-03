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

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * The configuration.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrConfig implements IsSerializable {

  /**
   * The configuration id.
   */
  protected String configId;

  /**
   * The configuration name.
   */
  protected String configName;

  /**
   * Whether the cpu_ms watching is enabled.
   */
  protected boolean cpumsEnabled = true;

  /**
   * Whether the api_cpu_ms watching is enabled.
   */
  protected boolean apimsEnabled = true;

  /**
   * Whether the log hooking is enabled.
   */
  protected boolean logHookEnabled = true;

  /**
   * The log hooking timezone.
   */
  protected String logHookTimezone = "PST";

  /**
   * Whether the retrying over quota is enabled.
   */
  protected boolean retryOverQuotaEnabled = false;

  /**
   * The interval seconds before retrying the test when the over quota exception
   * occures.
   */
  protected int retryOverQuotaInterval = 0;

  /**
   * The max retrying count when the over quota exception occures.
   */
  protected int retryOverQuotaMaxCount = 1;

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
   * @param configId
   *          The configuration id.
   */
  public void setConfigId(String configId) {
    this.configId = configId;
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
   * @param configName
   *          The configuration name.
   */
  public void setConfigName(String configName) {
    this.configName = configName;
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
   * @param cpumsEnabled
   *          Whether the cpu_ms watching is enabled.
   */
  public void setCpumsEnabled(boolean cpumsEnabled) {
    this.cpumsEnabled = cpumsEnabled;
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
   * @param apimsEnabled
   *          Whether the api_cpu_ms watching is enabled.
   */
  public void setApimsEnabled(boolean apimsEnabled) {
    this.apimsEnabled = apimsEnabled;
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
  public void setLogHookEnabled(boolean logHookEnabled) {
    this.logHookEnabled = logHookEnabled;
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
   * @param logHookTimezone
   *          The log hooking timezone.
   */
  public void setLogHookTimezone(String logHookTimezone) {
    this.logHookTimezone = logHookTimezone;
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
   * @param retryOverQuotaEnabled
   *          Whether the retrying over quota is enabled.
   */
  public void setRetryOverQuotaEnabled(boolean retryOverQuotaEnabled) {
    this.retryOverQuotaEnabled = retryOverQuotaEnabled;
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
   * @param retryOverQuotaInterval
   *          The interval seconds before retrying the test when the over quota
   *          exception
   */
  public void setRetryOverQuotaInterval(int retryOverQuotaInterval) {
    this.retryOverQuotaInterval = retryOverQuotaInterval;
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
   * @param retryOverQuotaMaxCount
   *          The max retrying count when the over quota exception occures.
   */
  public void setRetryOverQuotaMaxCount(int retryOverQuotaMaxCount) {
    this.retryOverQuotaMaxCount = retryOverQuotaMaxCount;
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
