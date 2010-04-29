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
import junit.runner.Version;
import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.server.logic.WjrAppEngineRecorder;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.logic.WjrStoreLoader;
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
   * The loader class which loads WjrStore from classes.
   */
  protected WjrStoreLoader storeLoader = new WjrStoreLoader();

  /**
   * The method runner class which runs the tests.
   */
  protected WjrMethodRunner methodRunner = new WjrMethodRunner();

  /**
   * The app engine time and log record class.
   */
  protected WjrAppEngineRecorder appEngineRecorder = new WjrAppEngineRecorder();

  /**
   * {@inheritDoc}
   */
  public WjrStore loadStore() {
    checkState(isJUnit4Available(), "JUnit4 not found.");
    return storeLoader.loadWjrStore(CLASSES_DIRECTORY);
  }

  /**
   * {@inheritDoc}
   */
  public WjrMethodItem runTest(WjrMethodItem methodItem) {
    checkState(isJUnit4Available(), "JUnit4 not found.");
    checkNotNull(methodItem, "The methodItem parameter is null.");

    try {
      appEngineRecorder.startRecording(KtrWjrConfig.getTimezone());
      methodItem = methodRunner.runWjrMethod(methodItem);
    } finally {
      if (appEngineRecorder.isRecording()) {
        appEngineRecorder.stopRecording();
      }
    }

    methodItem.setLog(appEngineRecorder.getRecordedLog());
    methodItem.setCpuTime(appEngineRecorder.getRecordedCpuTime());
    methodItem.setApiTime(appEngineRecorder.getRecordedApiTime());
    return methodItem;
  }

  /**
   * Check if the JUnit4 is available.
   * 
   * @return true if the JUnit4 is available, false if not
   */
  protected boolean isJUnit4Available() {
    return Version.id().startsWith("4");
  }
}
