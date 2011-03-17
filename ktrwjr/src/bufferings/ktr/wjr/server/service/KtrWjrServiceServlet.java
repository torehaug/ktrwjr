/*
 * Copyright 2010-2011 bufferings[at]gmail.com
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

import static bufferings.ktr.wjr.shared.util.WjrParamKey.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.server.util.WjrServerUtils;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.meta.WjrClassItemMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrConfigMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrMethodItemMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrStoreMeta;
import bufferings.ktr.wjr.shared.util.WjrParamKey;

import com.google.gson.stream.JsonWriter;

/**
 * KtrWjr service for json.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrServiceServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  public static final String CONTENT_TYPE_JSON = "application/json";

  /**
   * delegation
   */
  protected KtrWjrServiceImpl delegate = new KtrWjrServiceImpl();

  /**
   * {@inheritDoc}
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    resp.setContentType(CONTENT_TYPE_JSON);

    JsonWriter writer = new JsonWriter(resp.getWriter());
    String method = req.getParameter(WjrParamKey.KEY_METHOD);
    if (METHOD_LOAD_CONFIG.equals(method)) {
      writeConfig(writer, loadConfig(req));
    } else if (METHOD_LOAD_STORE.equals(method)) {
      writeStore(writer, loadStore());
    } else if (METHOD_RUN_TEST.equals(method)) {
      writeMethodItem(writer, runTest(req));
    } else {
      throw new ServletException("Invalid parameter.");
    }
  }

  /**
   * @see KtrWjrService#loadConfig(String)
   */
  WjrConfig loadConfig(HttpServletRequest req) {
    String configId = req.getParameter(WjrParamKey.KEY_CONFIG_ID);
    return delegate.loadConfig(configId);
  }

  /**
   * @see KtrWjrService#loadStore()
   */
  WjrStore loadStore() {
    return delegate.loadStore();
  }

  /**
   * @see KtrWjrService#runTest(WjrMethodItem, boolean, boolean, boolean,
   *      String)
   */
  WjrMethodItem runTest(HttpServletRequest req) {
    WjrMethodItem methodItem =
      new WjrMethodItem(
        req.getParameter(KEY_RUN_CLASS_NAME),
        req.getParameter(KEY_RUN_METHOD_NAME));
    boolean cpumsEnabled =
      WjrServerUtils.convertToBoolean(req.getParameter(KEY_CPUMS_ENABLED), true);
    boolean apimsEnabled =
      WjrServerUtils.convertToBoolean(req.getParameter(KEY_APIMS_ENABLED), true);
    boolean logHookEnabled =
      WjrServerUtils.convertToBoolean(req.getParameter(KEY_LOGHOOK_ENABLED), true);
    String logHookTimezone = req.getParameter(KEY_LOGHOOK_TIMEZONE);

    return delegate.runTest(
      methodItem,
      cpumsEnabled,
      apimsEnabled,
      logHookEnabled,
      logHookTimezone);
  }

  /**
   * Write config json.
   * 
   * @param writer
   *          the writer
   * @param config
   *          the config.
   * @throws IOException
   *           when fail to write
   */
  void writeConfig(JsonWriter writer, WjrConfig config) throws IOException {
    WjrConfigMeta m = WjrConfigMeta.meta();

    writer
      .beginObject()
      .name(m.configId)
      .value(config.getConfigId())
      .name(m.configName)
      .value(config.getConfigName())
      .name(m.cpumsEnabled)
      .value(Boolean.toString(config.isCpumsEnabled()))
      .name(m.apimsEnabled)
      .value(Boolean.toString(config.isApimsEnabled()))
      .name(m.logHookEnabled)
      .value(Boolean.toString(config.isLogHookEnabled()))
      .name(m.logHookTimezone)
      .value(config.getLogHookTimezone())
      .name(m.retryOverQuotaEnabled)
      .value(Boolean.toString(config.isRetryOverQuotaEnabled()))
      .name(m.retryOverQuotaInterval)
      .value(Integer.toString(config.getRetryOverQuotaInterval()))
      .name(m.retryOverQuotaMaxCount)
      .value(Integer.toString(config.getRetryOverQuotaMaxCount()))
      .endObject();
  }

  /**
   * Write classItem json.
   * 
   * @param writer
   *          the writer
   * @param classItem
   *          the classItem.
   * @throws IOException
   *           when fail to write
   */
  void writeClassItem(JsonWriter writer, WjrClassItem classItem)
      throws IOException {
    WjrClassItemMeta m = WjrClassItemMeta.meta();

    writer
      .beginObject()
      .name(m.className)
      .value(classItem.getClassName())
      .endObject();
  }

  /**
   * Write methodItem json.
   * 
   * @param writer
   *          the writer
   * @param methodItem
   *          the methodItem.
   * @throws IOException
   *           when fail to write
   */
  void writeMethodItem(JsonWriter writer, WjrMethodItem methodItem)
      throws IOException {
    WjrMethodItemMeta m = WjrMethodItemMeta.meta();

    writer
      .beginObject()
      .name(m.className)
      .value(methodItem.getClassName())
      .name(m.methodName)
      .value(methodItem.getMethodName())
      .name(m.trace)
      .value(methodItem.getTrace())
      .name(m.log)
      .value(methodItem.getLog())
      .name(m.time)
      .value(methodItem.getTime())
      .name(m.cpuTime)
      .value(methodItem.getCpuTime())
      .name(m.apiTime)
      .value(methodItem.getApiTime())
      .name(m.isOverQuota)
      .value(methodItem.isOverQuota())
      .name(m.retryCount)
      .value(methodItem.getRetryCount())
      .name(m.maxRetryCount)
      .value(methodItem.getMaxRetryCount())
      .name(m.waitingSeconds)
      .value(methodItem.getWaitingSeconds())
      .name(m.state)
      .value(methodItem.getState().name())
      .endObject();
  }

  /**
   * Write store json.
   * 
   * @param writer
   *          the writer
   * @param store
   *          the store.
   * @throws IOException
   *           when fail to write
   */
  void writeStore(JsonWriter writer, WjrStore store) throws IOException {
    WjrStoreMeta m = WjrStoreMeta.meta();

    writer.beginObject().name(m.classItems).beginArray();
    for (WjrClassItem classItem : store.getClassItems()) {
      writeClassItem(writer, classItem);
    }
    writer.endArray();

    writer.name(m.methodItems).beginArray();
    for (WjrMethodItem methodItem : store.getMethodItems()) {
      writeMethodItem(writer, methodItem);
    }
    writer.endArray().endObject();
  }
}
