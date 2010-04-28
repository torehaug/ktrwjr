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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import bufferings.ktr.wjr.server.logic.WjrAppEngineRecorder;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.logic.WjrStoreLoader;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class KtrWjrServiceImplTest extends ServletTestCase {

  @Test(expected = IllegalStateException.class)
  public void loadStore_WillThrowISE_WhenJUnit4IsNotAvailable() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl() {

      @Override
      protected boolean isJUnit4Available() {
        return false;
      }

    };
    service.loadStore();
  }

  @Test
  public void loadStore_WillCallStoreLoader() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl();

    final WjrStore store = new WjrStore();
    service.storeLoader = new WjrStoreLoader() {

      @Override
      public WjrStore loadWjrStore(String searchRootDirPath) {
        assertThat(searchRootDirPath, is("WEB-INF/classes"));
        return store;
      }

    };

    assertThat(service.loadStore(), is(store));
  }

  @Test(expected = IllegalStateException.class)
  public void runTest_WillThrowISE_WhenJUnit4IsNotAvailable() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl() {

      @Override
      protected boolean isJUnit4Available() {
        return false;
      }

    };
    service.runTest(new WjrMethodItem("foo.Foo", "forMethod"));
  }

  @Test(expected = NullPointerException.class)
  public void runTest_WillThrowNPE_WhenMethodItemIsNull() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl();
    service.runTest(null);
  }

  @Test
  public void runTest_WillCallMethodRunnerAndAppEngineRecorder() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl();
    final WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "fooMethod");
    final StringBuilder called = new StringBuilder();
    service.methodRunner = new WjrMethodRunner() {

      @Override
      public WjrMethodItem runWjrMethod(WjrMethodItem param) {
        called.append("2");
        assertThat(param, is(methodItem));
        return param;
      }

    };
    service.appEngineRecorder = new WjrAppEngineRecorder() {

      @Override
      public String getRecordedApiTime() {
        return "APITIME";
      }

      @Override
      public String getRecordedCpuTime() {
        return "CPUTIME";
      }

      @Override
      public String getRecordedLog() {
        return "LOG";
      }

      @Override
      public void startRecording(String timeZoneId) {
        called.append("1");
        assertThat(timeZoneId, is("JST"));
        super.startRecording(timeZoneId);
      }

      @Override
      public void stopRecording() {
        called.append("3");
        super.stopRecording();
      }

    };

    assertThat(service.runTest(methodItem), is(methodItem));
    assertThat(called.toString(), is("123"));
    assertThat(methodItem.getApiTime(), is("APITIME"));
    assertThat(methodItem.getCpuTime(), is("CPUTIME"));
    assertThat(methodItem.getLog(), is("LOG"));
  }

  @Test
  public void isJUnit4Available_IsTrue() throws Exception {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl();
    assertThat(service.isJUnit4Available(), is(true));
  }

}
