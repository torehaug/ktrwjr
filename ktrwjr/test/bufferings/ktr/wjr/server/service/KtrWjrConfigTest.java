package bufferings.ktr.wjr.server.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class KtrWjrConfigTest {

  @Test
  public void getTimezone_WillReturnPST_WithNullMap() {
    assertThat(KtrWjrConfig.getTimezone(null), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithNoTimezoneMap() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    assertThat(KtrWjrConfig.getTimezone(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsNullList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    map.put("tz", null);

    assertThat(KtrWjrConfig.getTimezone(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsEmptyList() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    map.put("tz", list);

    assertThat(KtrWjrConfig.getTimezone(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnPST_WithTimezoneIsNull() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add(null);
    map.put("tz", list);

    assertThat(KtrWjrConfig.getTimezone(map), is("PST"));
  }

  @Test
  public void getTimezone_WillReturnEmpty_WithTimezoneIsEmpty() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("");
    map.put("tz", list);

    assertThat(KtrWjrConfig.getTimezone(map), is(""));
  }

  @Test
  public void getTimezone_WillReturnJST_WithTimezoneIsJST() {
    Map<String, List<String>> map = new HashMap<String, List<String>>();
    List<String> list = new ArrayList<String>();
    list.add("JST");
    list.add("NotUsed");
    map.put("tz", list);

    assertThat(KtrWjrConfig.getTimezone(map), is("JST"));
  }
}
