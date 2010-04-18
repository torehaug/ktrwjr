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
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class KtrWjrServiceImpl implements KtrWjrService {

  private static final String CLASSES_DIRECTORY = "WEB-INF/classes";

  protected WjrStoreLoader wjrStoreLoader = new WjrStoreLoader();

  protected WjrMethodRunner wjrMethodRunner = new WjrMethodRunner();

  protected WjrAppEngineRecorder wjrGAEInfo = new WjrAppEngineRecorder();

  public WjrStore loadStore() {
    checkState(isJUnit4Available(), "JUnit4 not found.");
    checkNotNull(wjrStoreLoader, "The wjrStoreLoader field is null.");
    return wjrStoreLoader.loadWjrStore(CLASSES_DIRECTORY);
  }

  public WjrMethodItem runTest(WjrMethodItem methodItem) {
    checkState(isJUnit4Available(), "JUnit4 not found.");
    checkNotNull(wjrMethodRunner, "The wjrMethodRunner field is null.");
    checkNotNull(methodItem, "The methodItem parameter is null.");

    wjrGAEInfo.startRecording(WjrConfig.getTimezone());
    try {
      methodItem = wjrMethodRunner.runWjrMethod(methodItem);
    } finally {
      wjrGAEInfo.stopRecording();
    }

    methodItem.setLog(wjrGAEInfo.getRecordedLog());
    methodItem.setCpuTime(wjrGAEInfo.getRecordedCpuTime());
    methodItem.setApiTime(wjrGAEInfo.getRecordedApiTime());
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
