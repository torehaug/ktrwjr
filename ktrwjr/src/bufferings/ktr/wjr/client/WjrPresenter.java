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
package bufferings.ktr.wjr.client;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.client.runner.SequentialRunner;
import bufferings.ktr.wjr.client.runner.WjrRunner;
import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;
import bufferings.ktr.wjr.server.util.WjrServerUtils;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.util.WjrParamKey;
import bufferings.ktr.wjr.shared.util.WjrSharedUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

/**
 * The presenter of Kotori Web JUnit Runner.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrPresenter implements WjrDisplayHandler {

  /**
   * The KtrWjr rpc service(async).
   */
  protected KtrWjrServiceAsync rpcService;

  /**
   * The loading view.
   */
  protected WjrLoadingDisplay loadingView;

  /**
   * The view.
   */
  protected WjrDisplay view;

  /**
   * The user configuration.
   */
  protected WjrConfig config;

  /**
   * The test store.
   */
  protected WjrStore store;

  /**
   * The GET parameters for user configuration.
   */
  protected Map<String, List<String>> parameterMap;

  /**
   * The method runner.
   */
  protected WjrRunner runner;

  /**
   * Constructs the presenter.
   * 
   * @param rpcService
   *          The rpc service.
   * @param view
   *          The view.
   */
  public WjrPresenter(KtrWjrServiceAsync rpcService,
      WjrLoadingDisplay loadingView, WjrDisplay view) {
    this.rpcService = rpcService;
    this.loadingView = loadingView;
    this.view = view;
  }

  /**
   * Starts the application.
   * 
   * @param container
   *          The container of the view.
   */
  public void go(final HasWidgets container) {
    loadingView.go(container);
    view.go(WjrPresenter.this, container);

    rpcService.loadConfig(
      getConfigIdFromQueryString(),
      new AsyncCallback<WjrConfig>() {

        public void onFailure(Throwable caught) {
          config = new WjrConfig();
          setParamTzToConfig(config);
          loadingView.notifyLoaded();
          view.notifyLoadingConfigFailed(config, caught);
        }

        public void onSuccess(WjrConfig result) {
          checkNotNull(result, "The configuration is null.");
          config = result;
          setParamTzToConfig(config);
          loadingView.notifyLoaded();
          view.notifyLoadingConfigSucceeded(config);
        }

        private void setParamTzToConfig(WjrConfig config) {
          String tz = getTzFromQueryString();
          if (!WjrSharedUtils.isNullOrEmptyString(tz)) {
            config.setLogHookTimezone(tz);
          }
        }
      });
  }

  /**
   * {@inheritDoc}
   */
  public void onLoadStore() {
    rpcService.loadStore(new AsyncCallback<WjrStore>() {

      public void onFailure(Throwable caught) {
        view.setData(new WjrStore());
        view.notifyLoadingStoreFailed(caught);
      }

      public void onSuccess(WjrStore result) {
        store = result;
        view.setData(store);
        view.notifyLoadingStoreSucceeded();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void onClearButtonClick() {
    store.clearAllResultsAndSummaries();
    view.repaintAllTreeItems(store);
  }

  /**
   * {@inheritDoc}
   */
  public void onRunButtonClick() {
    checkState(!isRunnerRunning(), "Tests are running.");

    runner = createRunner();
    runner.run();
  }

  /**
   * {@inheritDoc}
   */
  public void onCancelButtonClick() {
    if (!isRunnerRunning()) {
      GWT.log("No tests are running.");
      return;
    }
    runner.cancelRunning();
  }

  /**
   * The runner is running or not.
   * 
   * @return True if the runner is not null and the runner is running, false if
   *         not.
   */
  protected boolean isRunnerRunning() {
    return runner != null && runner.isRunning();
  }

  /**
   * Gets the test runner.
   * 
   * @return The test runner.
   */
  protected WjrRunner createRunner() {
    return new SequentialRunner(rpcService, view, config, store);
  }

  /**
   * Gets the configId parameter form the query string.
   */
  protected String getConfigIdFromQueryString() {
    List<String> items = getParameterMap().get(WjrParamKey.KEY_CONFIG_ID);
    if (items == null || items.size() == 0) {
      return null;
    }
    return items.get(0);
  }

  /**
   * Gets the tz parameter form the query string.
   */
  protected String getTzFromQueryString() {
    List<String> items =
      getParameterMap().get(WjrParamKey.KEY_LOGHOOK_TIMEZONE);
    if (items == null || items.size() == 0) {
      return null;
    }
    return items.get(0);
  }

  /**
   * Gets the parameter map from the query string.
   * 
   * If the parameter map is already made, that instance is used.
   * 
   * I want to use the method {@link Window.Location#getParameterMap()}, but it
   * returns immutable map and list. The immutable map and list cannot be used
   * in the GWT-RPC. So I create the
   * {@link WjrServerUtils#buildListParamMap(String)}, which is the same logic
   * as {@link Window.Location#buildListParamMap(String)} but returns not
   * immutable map.
   * 
   * @return The properties.
   */
  protected Map<String, List<String>> getParameterMap() {
    if (parameterMap != null) {
      return parameterMap;
    }
    parameterMap = buildListParamMap(Window.Location.getQueryString());
    return parameterMap;
  }

  /**
   * Builds the not immutable map from String to List<String> that we'll return
   * in getParameterMap().
   * 
   * @return a map from the queryString.
   * @see Window.Location#buildListParamMap(String)
   */
  protected Map<String, List<String>> buildListParamMap(String queryString) {
    Map<String, List<String>> out = new HashMap<String, List<String>>();

    if (queryString != null && queryString.length() > 1) {
      String qs = queryString.substring(1);

      for (String kvPair : qs.split("&")) {
        String[] kv = kvPair.split("=", 2);
        if (kv[0].length() == 0) {
          continue;
        }

        List<String> values = out.get(kv[0]);
        if (values == null) {
          values = new ArrayList<String>();
          out.put(kv[0], values);
        }
        values.add(kv.length > 1 ? URL.decodeQueryString(kv[1]) : "");
      }
    }
    return out;
  }
}
