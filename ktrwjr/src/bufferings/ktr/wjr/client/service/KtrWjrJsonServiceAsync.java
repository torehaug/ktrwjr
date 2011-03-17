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
package bufferings.ktr.wjr.client.service;

import static bufferings.ktr.wjr.shared.util.WjrParamKey.*;
import static com.google.gwt.http.client.RequestBuilder.*;

import java.util.ArrayList;
import java.util.List;

import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;
import bufferings.ktr.wjr.shared.model.meta.WjrClassItemMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrConfigMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrMethodItemMeta;
import bufferings.ktr.wjr.shared.model.meta.WjrStoreMeta;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.http.client.URL;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The service using Json.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrJsonServiceAsync implements KtrWjrServiceAsync {

  /**
   * Name and Value Pair
   * 
   * @author bufferings[at]gmail.com
   */
  static class Pair {

    /**
     * The name.
     */
    public final String name;

    /**
     * The value.
     */
    public final String value;

    /**
     * Init with params.
     * 
     * @param name
     *          the name.
     * @param value
     *          the value.
     */
    public Pair(String name, String value) {
      this.name = name;
      this.value = value;
    }
  }

  /**
   * The url of json servlet.
   */
  static final String JSON_SERVLET_URL = "/ktrwjr/ktrwjr/ktrwjr.s3gwt";

  /**
   * The header of a content type.
   */
  static final String CONTENT_TYPE_HEADER = "Content-Type";

  /**
   * The x-www-form-urlencoded of a content type.
   */
  static final String CONTENT_TYPE_X_WWW_FORM_URLENCODED =
    "application/x-www-form-urlencoded; charset=utf-8";

  /**
   * The separator of parameter.
   */
  static final String PARAMETER_SEPARATOR = "&";

  /**
   * The separator of name and value.
   */
  static final String NAME_VALUE_SEPARATOR = "=";

  /**
   * {@inheritDoc}
   */
  public void loadConfig(String configId,
      final AsyncCallback<WjrConfig> callback) {
    final List<Pair> params = new ArrayList<Pair>();
    params.add(new Pair(KEY_METHOD, METHOD_LOAD_CONFIG));
    params.add(new Pair(KEY_CONFIG_ID, configId));
    try {
      sendRequest(params, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          callback.onFailure(exception);
        }

        public void onResponseReceived(Request request, Response response) {
          try {
            callback.onSuccess(createWjrConfigFromJson(response.getText()));
          } catch (Exception e) {
            callback.onFailure(e);
          }
        }
      });
    } catch (RequestException e) {
      callback.onFailure(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void loadStore(final AsyncCallback<WjrStore> callback) {
    final List<Pair> params = new ArrayList<Pair>();
    params.add(new Pair(KEY_METHOD, METHOD_LOAD_STORE));
    try {
      sendRequest(params, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          callback.onFailure(exception);
        }

        public void onResponseReceived(Request request, Response response) {
          try {
            callback.onSuccess(createWjrStoreFromJson(response.getText()));
          } catch (Exception e) {
            callback.onFailure(e);
          }
        }
      });
    } catch (RequestException e) {
      callback.onFailure(e);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void runTest(WjrMethodItem methodItem, boolean cpumsEnabled,
      boolean apimsEnabled, boolean logHookEnabled, String logHookTimezone,
      final AsyncCallback<WjrMethodItem> callback) {
    final List<Pair> params = new ArrayList<Pair>();
    params.add(new Pair(KEY_METHOD, METHOD_RUN_TEST));
    params.add(new Pair(KEY_RUN_CLASS_NAME, methodItem.getClassName()));
    params.add(new Pair(KEY_RUN_METHOD_NAME, methodItem.getMethodName()));
    params.add(new Pair(KEY_CPUMS_ENABLED, Boolean.toString(cpumsEnabled)));
    params.add(new Pair(KEY_APIMS_ENABLED, Boolean.toString(apimsEnabled)));
    params.add(new Pair(KEY_LOGHOOK_ENABLED, Boolean.toString(logHookEnabled)));
    params.add(new Pair(KEY_LOGHOOK_TIMEZONE, logHookTimezone));
    try {
      sendRequest(params, new RequestCallback() {
        public void onError(Request request, Throwable exception) {
          callback.onFailure(exception);
        }

        public void onResponseReceived(Request request, Response response) {
          try {
            callback.onSuccess(createWjrMethodItemFromJson(response.getText()));
          } catch (Exception e) {
            callback.onFailure(e);
          }
        }
      });
    } catch (RequestException e) {
      callback.onFailure(e);
    }
  }

  /**
   * Sends a request to the ktrwjr servlet.
   * 
   * @param params
   *          the parameters.
   * @param callback
   *          the callback.
   * @throws RequestException
   *           thrown when the error occured.
   */
  void sendRequest(List<Pair> params, RequestCallback callback)
      throws RequestException {
    RequestBuilder builder = new RequestBuilder(POST, JSON_SERVLET_URL);
    builder.setHeader(CONTENT_TYPE_HEADER, CONTENT_TYPE_X_WWW_FORM_URLENCODED);
    builder.sendRequest(format(params), callback);
  }

  /**
   * Returns a String that is suitable for use as an
   * <code>application/x-www-form-urlencoded</code> list of parameters in an
   * HTTP PUT or HTTP POST.
   * 
   * @param parameters
   *          The parameters to include.
   * @param encoding
   *          The encoding to use.
   */
  String format(List<Pair> parameters) {
    final StringBuilder result = new StringBuilder();
    for (final Pair parameter : parameters) {
      String encodedName = URL.encode(parameter.name);
      String encodedValue =
        parameter.value != null ? URL.encode(parameter.value) : "";
      if (result.length() > 0) {
        result.append(PARAMETER_SEPARATOR);
      }
      result.append(encodedName);
      result.append(NAME_VALUE_SEPARATOR);
      result.append(encodedValue);
    }
    return result.toString();
  }

  /**
   * Creates a WjrConfig from Json string.
   * 
   * @param jsonString
   *          the Json string.
   * @return the created WjrConfig instance.
   */
  WjrConfig createWjrConfigFromJson(String jsonString) {
    WjrConfigMeta m = WjrConfigMeta.meta();
    WjrConfig wjrConfig = new WjrConfig();

    JSONObject j = JSONParser.parseStrict(jsonString).isObject();
    wjrConfig.setConfigId(j.get(m.configId).isString().stringValue());
    wjrConfig.setConfigName(j.get(m.configName).isString().stringValue());
    wjrConfig.setCpumsEnabled(j.get(m.cpumsEnabled).isString().stringValue());
    wjrConfig.setApimsEnabled(j.get(m.apimsEnabled).isString().stringValue());
    wjrConfig.setLogHookEnabled(j
      .get(m.logHookEnabled)
      .isString()
      .stringValue());
    wjrConfig.setLogHookTimezone(j
      .get(m.logHookTimezone)
      .isString()
      .stringValue());
    wjrConfig.setRetryOverQuotaEnabled(j
      .get(m.retryOverQuotaEnabled)
      .isString()
      .stringValue());
    wjrConfig.setRetryOverQuotaInterval(j
      .get(m.retryOverQuotaInterval)
      .isString()
      .stringValue());
    wjrConfig.setRetryOverQuotaMaxCount(j
      .get(m.retryOverQuotaMaxCount)
      .isString()
      .stringValue());

    return wjrConfig;
  }

  /**
   * Creates a WjrStore from Json string.
   * 
   * @param jsonString
   *          the Json string.
   * @return the created WjrStore instance.
   */
  WjrStore createWjrStoreFromJson(String jsonString) {
    WjrStoreMeta m = WjrStoreMeta.meta();
    WjrStore wjrStore = new WjrStore();

    JSONObject j = JSONParser.parseStrict(jsonString).isObject();

    JSONArray classItems = j.get(m.classItems).isArray();
    int classItemsSize = classItems.size();
    for (int i = 0; i < classItemsSize; i++) {
      wjrStore.addClassItem(createWjrClassItemFromJson(classItems
        .get(i)
        .toString()));
    }

    JSONArray methodItems = j.get(m.methodItems).isArray();
    int methodItemsSize = methodItems.size();
    for (int i = 0; i < methodItemsSize; i++) {
      wjrStore.addMethodItem(createWjrMethodItemFromJson(methodItems
        .get(i)
        .toString()));
    }

    return wjrStore;
  }

  /**
   * Creates a WjrClassItem from Json string.
   * 
   * @param jsonString
   *          the Json string.
   * @return the created WjrClassItem instance.
   */
  WjrClassItem createWjrClassItemFromJson(String jsonString) {
    WjrClassItemMeta m = WjrClassItemMeta.meta();
    JSONValue j = JSONParser.parseStrict(jsonString);
    return new WjrClassItem(j
      .isObject()
      .get(m.className)
      .isString()
      .stringValue());
  }

  /**
   * Creates a WjrMethodItem from Json string.
   * 
   * @param jsonString
   *          the Json string.
   * @return the created WjrMethodItem instance.
   */
  WjrMethodItem createWjrMethodItemFromJson(String jsonString) {
    WjrMethodItemMeta m = WjrMethodItemMeta.meta();
    JSONObject j = JSONParser.parseStrict(jsonString).isObject();

    String className = j.get(m.className).isString().stringValue();
    String methodName = j.get(m.methodName).isString().stringValue();
    WjrMethodItem wjrMethodItem = new WjrMethodItem(className, methodName);

    wjrMethodItem.setTrace(j.get(m.trace).isString().stringValue());
    wjrMethodItem.setLog(j.get(m.log).isString().stringValue());
    wjrMethodItem.setTime(j.get(m.time).isString().stringValue());
    wjrMethodItem.setCpuTime(j.get(m.cpuTime).isString().stringValue());
    wjrMethodItem.setApiTime(j.get(m.apiTime).isString().stringValue());
    wjrMethodItem.setOverQuota(j.get(m.isOverQuota).isBoolean().booleanValue());
    wjrMethodItem.setRetryCount(new Integer(j
      .get(m.retryCount)
      .isNumber()
      .toString()));
    wjrMethodItem.setMaxRetryCount(new Integer(j
      .get(m.maxRetryCount)
      .isNumber()
      .toString()));
    wjrMethodItem.setWaitingSeconds(new Integer(j
      .get(m.waitingSeconds)
      .isNumber()
      .toString()));
    wjrMethodItem.setState(State.valueOf(j
      .get(m.state)
      .isString()
      .stringValue()));

    return wjrMethodItem;
  }

}
