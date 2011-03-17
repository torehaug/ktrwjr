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
package bufferings.ktr.wjr.client.service;

import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

/**
 * The KtrWjr service.
 * 
 * @author bufferings[at]gmail.com
 */
public interface KtrWjrService {

  /**
   * Loads the WjrConfig.
   * 
   * @param configId
   *          The config ID.
   * @return The loaded config.
   */
  public WjrConfig loadConfig(String configId);

  /**
   * Loads the WjrStore.
   * 
   * @return The loaded store.
   */
  public WjrStore loadStore();

  /**
   * Runs the test.
   * 
   * @param methodItem
   *          The methodItem to run.
   * @param cpumsEnabled
   *          Whether to get cpu_ms or not.
   * @param apimsEnabled
   *          Whether to get api_ms or not.
   * @param logHookEnabled
   *          Whether to get log or not.
   * @param logHookTimezone
   *          The log timezone.
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem, boolean cpumsEnabled,
      boolean apimsEnabled, boolean logHookEnabled, String logHookTimezone);
}
