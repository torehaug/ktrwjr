/*
 * Copyright 2010-2011 bufferings[at]gmail.com
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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class WjrConfigTest {

  private WjrConfig config = new WjrConfig();

  @Test
  public void configId() {
    config.setConfigId(null);
    assertThat(config.getConfigId(), is("default"));
    config.setConfigId("");
    assertThat(config.getConfigId(), is("default"));
    config.setConfigId("sample");
    assertThat(config.getConfigId(), is("sample"));
  }

  @Test
  public void configName() {
    config.setConfigName(null);
    assertThat(config.getConfigName(), is("default"));
    config.setConfigName("");
    assertThat(config.getConfigName(), is("default"));
    config.setConfigName("sample");
    assertThat(config.getConfigName(), is("sample"));
  }

  @Test
  public void cpumsEnabled() {
    config.setCpumsEnabled(null);
    assertThat(config.isCpumsEnabled(), is(true));
    config.setCpumsEnabled("");
    assertThat(config.isCpumsEnabled(), is(true));
    config.setCpumsEnabled("unknown");
    assertThat(config.isCpumsEnabled(), is(true));
    config.setCpumsEnabled("true");
    assertThat(config.isCpumsEnabled(), is(true));
    config.setCpumsEnabled("false");
    assertThat(config.isCpumsEnabled(), is(false));
  }

  @Test
  public void apimsEnabled() {
    config.setApimsEnabled(null);
    assertThat(config.isApimsEnabled(), is(true));
    config.setApimsEnabled("");
    assertThat(config.isApimsEnabled(), is(true));
    config.setApimsEnabled("unknown");
    assertThat(config.isApimsEnabled(), is(true));
    config.setApimsEnabled("true");
    assertThat(config.isApimsEnabled(), is(true));
    config.setApimsEnabled("false");
    assertThat(config.isApimsEnabled(), is(false));
  }

  @Test
  public void logHookEnabled() {
    config.setLogHookEnabled(null);
    assertThat(config.isLogHookEnabled(), is(true));
    config.setLogHookEnabled("");
    assertThat(config.isLogHookEnabled(), is(true));
    config.setLogHookEnabled("unknown");
    assertThat(config.isLogHookEnabled(), is(true));
    config.setLogHookEnabled("true");
    assertThat(config.isLogHookEnabled(), is(true));
    config.setLogHookEnabled("false");
    assertThat(config.isLogHookEnabled(), is(false));
  }

  @Test
  public void logHookTimezone() {
    config.setLogHookTimezone(null);
    assertThat(config.getLogHookTimezone(), is("PST"));
    config.setLogHookTimezone("");
    assertThat(config.getLogHookTimezone(), is("PST"));
    config.setLogHookTimezone("JST");
    assertThat(config.getLogHookTimezone(), is("JST"));
  }

  @Test
  public void retryOver() {
    config.setRetryOverQuotaEnabled(null);
    assertThat(config.isRetryOverQuotaEnabled(), is(false));
    config.setRetryOverQuotaEnabled("");
    assertThat(config.isRetryOverQuotaEnabled(), is(false));
    config.setRetryOverQuotaEnabled("unknown");
    assertThat(config.isRetryOverQuotaEnabled(), is(false));
    config.setRetryOverQuotaEnabled("true");
    assertThat(config.isRetryOverQuotaEnabled(), is(true));
    config.setRetryOverQuotaEnabled("false");
    assertThat(config.isRetryOverQuotaEnabled(), is(false));
  }

  @Test
  public void retryOverQuotaInterval() {
    config.setRetryOverQuotaInterval(null);
    assertThat(config.getRetryOverQuotaInterval(), is(0));
    config.setRetryOverQuotaInterval("");
    assertThat(config.getRetryOverQuotaInterval(), is(0));
    config.setRetryOverQuotaInterval("unknown");
    assertThat(config.getRetryOverQuotaInterval(), is(0));
    config.setRetryOverQuotaInterval("1");
    assertThat(config.getRetryOverQuotaInterval(), is(1));
    config.setRetryOverQuotaInterval("-1");
    assertThat(config.getRetryOverQuotaInterval(), is(0));
    config.setRetryOverQuotaInterval("1000001");
    assertThat(config.getRetryOverQuotaInterval(), is(1000000));
  }

  @Test
  public void retryOverQuotaMaxCount() {
    config.setRetryOverQuotaMaxCount(null);
    assertThat(config.getRetryOverQuotaMaxCount(), is(0));
    config.setRetryOverQuotaMaxCount("");
    assertThat(config.getRetryOverQuotaMaxCount(), is(0));
    config.setRetryOverQuotaMaxCount("unknown");
    assertThat(config.getRetryOverQuotaMaxCount(), is(0));
    config.setRetryOverQuotaMaxCount("1");
    assertThat(config.getRetryOverQuotaMaxCount(), is(1));
    config.setRetryOverQuotaMaxCount("-1");
    assertThat(config.getRetryOverQuotaMaxCount(), is(0));
    config.setRetryOverQuotaMaxCount("101");
    assertThat(config.getRetryOverQuotaMaxCount(), is(100));
  }

}
