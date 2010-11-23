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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class KtrWjrServiceServletTest {

  private KtrWjrServiceServlet servlet = new KtrWjrServiceServlet();

  @Test
  public final void loadStore_WillCallDelegate() {
    final WjrStore store = new WjrStore();
    final Map<String, List<String>> map = new HashMap<String, List<String>>();

    servlet.delegate = new KtrWjrServiceImpl() {

      @Override
      public WjrStore loadStore(Map<String, List<String>> parameterMap) {
        assertThat(parameterMap, is(map));
        return store;
      }

    };

    assertThat(servlet.loadStore(map), is(store));
  }

  @Test
  public final void runTest_WillCallDelegate() {
    final WjrMethodItem in = new WjrMethodItem("foo.Foo", "barMethod1");
    final WjrMethodItem out = new WjrMethodItem("foo.Foo", "barMethod2");
    final Map<String, List<String>> map = new HashMap<String, List<String>>();

    servlet.delegate = new KtrWjrServiceImpl() {

      @Override
      public WjrMethodItem runTest(WjrMethodItem methodItem,
          Map<String, List<String>> parameterMap, boolean cpumsEnabled,
          boolean apimsEnabled, boolean logHookEnabled, String logHookTimezone) {
        assertThat(methodItem, is(in));
        assertThat(parameterMap, is(map));
        return out;
      }
    };

    assertThat(servlet.runTest(in, map, true, true, true, "PST"), is(out));
  }

}
