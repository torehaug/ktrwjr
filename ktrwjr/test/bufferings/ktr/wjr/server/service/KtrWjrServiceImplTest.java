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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slim3.tester.ServletTestCase;

import bufferings.ktr.wjr.server.logic.WjrAppEngineRecorder;
import bufferings.ktr.wjr.server.logic.WjrMethodRunner;
import bufferings.ktr.wjr.server.logic.WjrStoreLoader;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class KtrWjrServiceImplTest extends ServletTestCase {

  @Test
  public void loadStore_WillCallStoreLoader() {
    final WjrStore store = new WjrStore();

    KtrWjrServiceImpl service = new KtrWjrServiceImpl() {
      @Override
      protected WjrStoreLoader getStoreLoader() {
        return new WjrStoreLoader() {
          @Override
          public WjrStore loadWjrStore(String searchRootDirPath) {
            assertThat(searchRootDirPath, is("WEB-INF/classes"));
            return store;
          }

          @Override
          protected void checkAndStoreTestClass(WjrStore store, Class<?> clazz) {
          }
        };
      }
    };

    assertThat(service.loadStore(null), is(store));
  }

  @Test(expected = NullPointerException.class)
  public void runTest_WillThrowNPE_WhenMethodItemIsNull() {
    KtrWjrServiceImpl service = new KtrWjrServiceImpl();
    service.runTest(null, null);
  }

  @Test
  public void runTest_WillCallMethodRunnerAndAppEngineRecorder() {
    final WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "fooMethod");
    final StringBuilder called = new StringBuilder();

    KtrWjrServiceImpl service = new KtrWjrServiceImpl() {
      @Override
      protected WjrAppEngineRecorder getAppEngineRecorder() {
        return new WjrAppEngineRecorder() {
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
            assertThat(timeZoneId, is("PST"));
          }

          @Override
          public void stopRecording() {
            called.append("3");
          }

          @Override
          public boolean isRecording() {
            return true;
          }
        };
      }

      @Override
      protected WjrMethodRunner getMethodRunner() {
        return new WjrMethodRunner() {
          public WjrMethodItem runWjrMethod(WjrMethodItem param) {
            called.append("2");
            assertThat(param, is(methodItem));
            return param;
          }
        };
      }
    };

    assertThat(service.runTest(methodItem, null), is(methodItem));
    assertThat(called.toString(), is("123"));
    assertThat(methodItem.getApiTime(), is("APITIME"));
    assertThat(methodItem.getCpuTime(), is("CPUTIME"));
    assertThat(methodItem.getLog(), is("LOG"));
  }

  @Test
  public void runTest_WillCallMethodRunnerAndAppEngineRecorder_WithParameter() {
    final WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "fooMethod");
    final StringBuilder called = new StringBuilder();

    KtrWjrServiceImpl service = new KtrWjrServiceImpl() {
      @Override
      protected WjrAppEngineRecorder getAppEngineRecorder() {
        return new WjrAppEngineRecorder() {
          @Override
          public String getRecordedApiTime() {
            return "";
          }

          @Override
          public String getRecordedCpuTime() {
            return "";
          }

          @Override
          public String getRecordedLog() {
            return "";
          }

          @Override
          public void startRecording(String timeZoneId) {
            assertThat(timeZoneId, is("JST"));
            called.append("1");
          }

          @Override
          public void stopRecording() {
          }

          @Override
          public boolean isRecording() {
            return true;
          }
        };
      }

      @Override
      protected WjrMethodRunner getMethodRunner() {
        return new WjrMethodRunner() {
          public WjrMethodItem runWjrMethod(WjrMethodItem param) {
            return param;
          }
        };
      }
    };

    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("JST");
    map.put("tz", list);

    service.runTest(methodItem, map);
    assertThat(called.toString(), is("1"));
  }
}
