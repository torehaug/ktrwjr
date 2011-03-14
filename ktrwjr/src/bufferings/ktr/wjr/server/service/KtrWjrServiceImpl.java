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

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.logging.Logger;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.server.logic.WjrConfigLoader;
import bufferings.ktr.wjr.server.logic.WjrGAEDevLogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAELogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAEProdLogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAEQuotaRecorder;
import bufferings.ktr.wjr.server.logic.WjrJUnitLogicFactory;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.logic.WjrStoreLoader;
import bufferings.ktr.wjr.server.util.AppEngineUtil;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.util.WjrSharedUtils;

/**
 * KtrWjr service class.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrServiceImpl implements KtrWjrService {

  private static final Logger logger = Logger.getLogger(KtrWjrServiceImpl.class
    .getName());

  protected static final String CLASSES_DIRECTORY = "WEB-INF/classes";

  /**
   * {@inheritDoc}
   */
  public WjrConfig loadConfig(String configId) {
    if (WjrSharedUtils.isNullOrEmptyString(configId)) {
      configId = WjrConfig.DEFAULT_CONFIG_ID;
    }

    WjrConfig config = getConfigLoader().loadWjrConfig(configId);
    logger.info("The configuration is loaded. " + config.toString());
    return config;
  }

  /**
   * {@inheritDoc}
   */
  public WjrStore loadStore() {
    return getStoreLoader().loadWjrStore(CLASSES_DIRECTORY);
  }

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem, boolean cpumsEnabled,
      boolean apimsEnabled, boolean logHookEnabled, String logHookTimezone) {
    checkNotNull(methodItem, "The methodItem parameter is null.");

    WjrGAEQuotaRecorder quotaRecorder = null;
    if (cpumsEnabled || apimsEnabled) {
      quotaRecorder = getGAEQuotaRecorder();
    }

    WjrGAELogRecorder logRecorder = null;
    if (logHookEnabled) {
      logRecorder = getGAELogRecorder();
    }

    WjrMethodRunner methodRunner = getMethodRunner();
    try {
      methodItem.clearResult();

      if (logRecorder != null) {
        if (WjrSharedUtils.isNullOrEmptyString(logHookTimezone)) {
          logHookTimezone = WjrConfig.DEFAULT_LOGHOOK_TIMEZONE;
        }

        logRecorder.startRecording(logHookTimezone);
      }

      if (quotaRecorder != null) {
        quotaRecorder.startRecording();
      }

      methodItem = methodRunner.runWjrMethod(methodItem);
    } finally {
      if (quotaRecorder != null && quotaRecorder.isRecording()) {
        quotaRecorder.stopRecording();
        if (cpumsEnabled) {
          methodItem.setCpuTime(quotaRecorder.getRecordedCpuTime());
        }
        if (apimsEnabled) {
          methodItem.setApiTime(quotaRecorder.getRecordedApiTime());
        }
      }

      if (logRecorder != null && logRecorder.isRecording()) {
        logRecorder.stopRecording();
        methodItem.setLog(logRecorder.getRecordedLog());
      }
    }
    return methodItem;
  }

  /**
   * Gets the configuration loader.
   */
  protected WjrConfigLoader getConfigLoader() {
    return new WjrConfigLoader();
  }

  /**
   * Gets the store loader which loads WjrStore from classes.
   */
  protected WjrStoreLoader getStoreLoader() {
    return WjrJUnitLogicFactory.getStoreLoader();
  }

  /**
   * Gets the method runner which runs the tests.
   */
  protected WjrMethodRunner getMethodRunner() {
    return WjrJUnitLogicFactory.getMethodRunner();
  }

  /**
   * Gets the GAE log recorder.
   */
  protected WjrGAELogRecorder getGAELogRecorder() {
    if (AppEngineUtil.isProduction()) {
      return new WjrGAEProdLogRecorder();
    } else {
      return new WjrGAEDevLogRecorder();
    }
  }

  /**
   * Gets the GAE quota recorder.
   */
  protected WjrGAEQuotaRecorder getGAEQuotaRecorder() {
    return new WjrGAEQuotaRecorder();
  }
}
