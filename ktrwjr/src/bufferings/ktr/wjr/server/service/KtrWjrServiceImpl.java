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

import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.server.logic.WjrGAEDevLogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAELogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAEProdLogRecorder;
import bufferings.ktr.wjr.server.logic.WjrGAEQuotaRecorder;
import bufferings.ktr.wjr.server.logic.WjrJUnitLogicFactory;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.logic.WjrParamParser;
import bufferings.ktr.wjr.server.logic.WjrStoreLoader;
import bufferings.ktr.wjr.server.util.AppEngineUtil;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

/**
 * KtrWjr service class.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrServiceImpl implements KtrWjrService {

  protected static final String CLASSES_DIRECTORY = "WEB-INF/classes";

  /**
   * {@inheritDoc}
   */
  public WjrStore loadStore(Map<String, List<String>> parameterMap) {
    return getStoreLoader().loadWjrStore(CLASSES_DIRECTORY);
  }

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem,
      Map<String, List<String>> parameterMap) {
    checkNotNull(methodItem, "The methodItem parameter is null.");

    WjrGAEQuotaRecorder quotaRecorder = getGAEQuotaRecorder();
    WjrGAELogRecorder logRecorder = getGAELogRecorder();

    WjrParamParser paramParser = getParamParser();
    WjrMethodRunner methodRunner = getMethodRunner();

    String timeZoneId = paramParser.getTimeZoneId(parameterMap);
    try {
      methodItem.clearResult();
      logRecorder.startRecording(timeZoneId);
      quotaRecorder.startRecording();
      methodItem = methodRunner.runWjrMethod(methodItem);
    } finally {
      if (quotaRecorder.isRecording()) {
        quotaRecorder.stopRecording();
        methodItem.setCpuTime(quotaRecorder.getRecordedCpuTime());
        methodItem.setApiTime(quotaRecorder.getRecordedApiTime());
      }
      if (logRecorder.isRecording()) {
        logRecorder.stopRecording();
        methodItem.setLog(logRecorder.getRecordedLog());
      }
    }
    return methodItem;
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
   * Gets the parameter map parser.
   */
  protected WjrParamParser getParamParser() {
    return new WjrParamParser();
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
