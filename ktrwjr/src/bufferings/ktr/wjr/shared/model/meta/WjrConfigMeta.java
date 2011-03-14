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
package bufferings.ktr.wjr.shared.model.meta;

/**
 * A meta of WjrConfig.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrConfigMeta {

  private static final WjrConfigMeta meta = new WjrConfigMeta();

  public static WjrConfigMeta meta() {
    return meta;
  }

  public final String configId = "configId";

  public final String configName = "configuration";

  public final String cpumsEnabled = "cpumsEnabled";

  public final String apimsEnabled = "apimsEnabled";

  public final String logHookEnabled = "logHookEnabled";

  public final String logHookTimezone = "logHookTimezone";

  public final String retryOverQuotaEnabled = "retryOverQuotaEnabled";

  public final String retryOverQuotaInterval = "retryOverQuotaInterval";

  public final String retryOverQuotaMaxCount = "retryOverQuotaMaxCount";

  private WjrConfigMeta() {
  }
}
