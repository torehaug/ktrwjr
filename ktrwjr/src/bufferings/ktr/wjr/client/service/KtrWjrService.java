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

import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The KtrWjr service.
 * 
 * @author bufferings[at]gmail.com
 */
@RemoteServiceRelativePath("ktrwjr.s3gwt")
public interface KtrWjrService extends RemoteService {

  /**
   * Loads the WjrConfig.
   * 
   * @param parameterMap
   *          GET parameter map for user configuration.
   * @return The loaded config.
   */
  public WjrConfig loadConfig(Map<String, List<String>> parameterMap);

  /**
   * Loads the WjrStore.
   * 
   * @param parameterMap
   *          GET parameter map for user configuration.
   * @return The loaded store.
   */
  public WjrStore loadStore(Map<String, List<String>> parameterMap);

  /**
   * Runs the test.
   * 
   * @param methodItem
   *          The methodItem to run.
   * @param parameterMap
   *          GET parameter map for user configuration.
   * @param cpumsEnabled
   *          Whether to get cpu_ms or not.
   * @param apimsEnabled
   *          Whether to get api_ms or not.
   * @param logHookEnabled
   *          Whether to get log or not.
   * @param logHookTimezone
   *          The log timezone. If the parameterMap has the "tz", the value of
   *          the parameterMap will be used preferentially.
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem,
      Map<String, List<String>> parameterMap, boolean cpumsEnabled,
      boolean apimsEnabled, boolean logHookEnabled, String logHookTimezone);
}
