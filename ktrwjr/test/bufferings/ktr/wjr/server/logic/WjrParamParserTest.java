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
package bufferings.ktr.wjr.server.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class WjrParamParserTest {

  private WjrParamParser paramParser = new WjrParamParser();

  @Test
  public void getTimezone_WillReturnPST_WithNullMap() {
    assertThat(paramParser.getTimeZoneId(null), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithNoTimezoneMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    assertThat(paramParser.getTimeZoneId(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsNullList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("tz", null);

    assertThat(paramParser.getTimeZoneId(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsEmptyList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    map.put("tz", list);

    assertThat(paramParser.getTimeZoneId(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsNull() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add(null);
    map.put("tz", list);

    assertThat(paramParser.getTimeZoneId(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnEmpty_WithTimezoneIsEmpty() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("");
    map.put("tz", list);

    assertThat(paramParser.getTimeZoneId(map), is(""));
  }

  @Test
  public void getTimezone_WillReturnJST_WithTimezoneIsJST() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("JST");
    list.add("NotUsed");
    map.put("tz", list);

    assertThat(paramParser.getTimeZoneId(map), is("JST"));
  }

  @Test
  public void getConfigId_WillReturnPST_WithNullMap() {
    assertThat(paramParser.getTimeZoneId(null), is("PST"));
  }

  @Test
  public void getConfigId_WillReturnDefault_WithNoConfigIdMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    assertThat(paramParser.getConfigId(map), is("default"));
  }

  @Test
  public void getConfigId_WillReturnDefault_WithConfigIdIsNullList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("config", null);

    assertThat(paramParser.getConfigId(map), is("default"));
  }

  @Test
  public void getConfigId_WillReturnDefault_WithConfigIdIsEmptyList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    map.put("config", list);

    assertThat(paramParser.getConfigId(map), is("default"));
  }

  @Test
  public void getConfigId_WillReturnDefault_WithConfigIdIsNull() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add(null);
    map.put("config", list);

    assertThat(paramParser.getConfigId(map), is("default"));
  }

  @Test
  public void getConfigId_WillReturnEmpty_WithConfigIdIsEmpty() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("");
    map.put("config", list);

    assertThat(paramParser.getConfigId(map), is(""));
  }

  @Test
  public void getConfigId_WillReturnConfigId() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("sample");
    list.add("NotUsed");
    map.put("config", list);

    assertThat(paramParser.getConfigId(map), is("sample"));
  }
}
