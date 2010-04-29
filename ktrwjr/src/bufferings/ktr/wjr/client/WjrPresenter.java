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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;
import bufferings.ktr.wjr.server.util.WjrUtils;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Element;
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
   * The view.
   */
  protected WjrDisplay view;

  /**
   * The test store.
   */
  protected WjrStore store;

  /**
   * Whether the test is running or not.
   */
  protected boolean running = false;

  /**
   * Whether the cancel running tests is requested or not.
   */
  protected boolean cancelRequested = false;

  /**
   * The GET parameters for user configuration.
   */
  protected Map<String, List<String>> parameterMap;

  /**
   * Constructs the presenter.
   * 
   * @param rpcService
   *          The rpc service.
   * @param view
   *          The view.
   */
  public WjrPresenter(KtrWjrServiceAsync rpcService, WjrDisplay view) {
    this.rpcService = rpcService;
    this.view = view;
  }

  /**
   * Starts the application.
   * 
   * @param container
   *          The container of the view.
   * @param loadingElem
   *          The loading element, which is removed from DOM after loading
   *          finished.
   */
  public void go(HasWidgets container, Element loadingElem) {
    view.go(this, container, loadingElem);
    rpcService.loadStore(getParameterMap(), new AsyncCallback<WjrStore>() {

      @Override
      public void onFailure(Throwable caught) {
        view.notifyLoadingFailed(caught);
      }

      @Override
      public void onSuccess(WjrStore result) {
        store = result;
        view.notifyLoadingSucceeded(store);
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onLoadStore() {
    rpcService.loadStore(getParameterMap(), new AsyncCallback<WjrStore>() {

      @Override
      public void onFailure(Throwable caught) {
        view.setData(new WjrStore());
        view.notifyReloadingFailed(caught);
      }

      @Override
      public void onSuccess(WjrStore result) {
        store = result;
        view.setData(store);
        view.notifyReloadingSucceeded();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onClearButtonClick() {
    store.clearAllResultsAndSummaries();
    view.repaintAllTreeItems(store);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onRunButtonClick() {
    List<WjrMethodItem> checkedMethods = view.getCheckedMethodItems();
    if (checkedMethods.size() == 0) {
      GWT.log("No method items are checked.");
      view.notifyRunningFinished();
      return;
    }

    running = true;
    cancelRequested = false;

    for (WjrMethodItem item : checkedMethods) {
      item.clearResult();
      item.setState(State.RUNNING);
    }
    store.updateAllSummaries();
    view.repaintAllTreeItems(store);

    runWjrMethodItem(checkedMethods, 0);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onCancelButtonClick() {
    if (!running) {
      GWT.log("No tests are running.");
      return;
    }
    cancelRequested = true;
  }

  /**
   * Runs the test method.
   * 
   * @param methodItems
   *          The methods to run.
   * @param currentIndex
   *          The index
   */
  protected void runWjrMethodItem(final List<WjrMethodItem> methodItems,
      final int currentIndex) {

    rpcService.runTest(
      methodItems.get(currentIndex),
      getParameterMap(),
      new AsyncCallback<WjrMethodItem>() {

        @Override
        public void onFailure(Throwable caught) {
          WjrMethodItem stored = methodItems.get(currentIndex);
          GWT.log("Run " + stored.getMethodName() + " failed.", caught);

          stored.setState(State.ERROR);
          stored.setTrace(getTrace(caught));

          store.getClassItem(stored.getClassName()).updateSummary(store);
          store.updateSummary();

          view.repaintTreeItemAncestors(store, stored);

          int nextIndex = currentIndex + 1;
          if (methodItems.size() <= nextIndex) {
            onRunSuccess();
            return;
          }

          if (cancelRequested) {
            onRunCancel();
            return;
          }

          runWjrMethodItem(methodItems, nextIndex);
        }

        @Override
        public void onSuccess(WjrMethodItem result) {
          GWT.log("Run " + result.getMethodName() + " succeeded.");

          WjrMethodItem stored =
            store.getMethodItem(result.getClassAndMethodName());
          copyMethodItemAttributes(result, stored);

          store.getClassItem(stored.getClassName()).updateSummary(store);
          store.updateSummary();
          view.repaintTreeItemAncestors(store, stored);

          int nextIndex = currentIndex + 1;
          if (methodItems.size() <= nextIndex) {
            onRunSuccess();
            return;
          }

          if (cancelRequested) {
            onRunCancel();
            return;
          }

          runWjrMethodItem(methodItems, nextIndex);
        }

        private void copyMethodItemAttributes(WjrMethodItem from,
            WjrMethodItem to) {
          to.setState(from.getState());
          to.setTrace(from.getTrace());
          to.setTime(from.getTime());
          to.setCpuTime(from.getCpuTime());
          to.setApiTime(from.getApiTime());
          to.setLog(from.getLog());
        }

        private String getTrace(Throwable e) {
          return e.toString();
        }

        private void onRunSuccess() {
          view.notifyRunningFinished();
        }

        private void onRunCancel() {
          for (WjrMethodItem item : methodItems) {
            if (item.getState() == State.RUNNING) {
              item.setState(State.NOT_YET);
            }
          }
          store.updateAllSummaries();
          view.repaintAllTreeItems(store);
          view.notifyRunningFinished();
        }
      });
  }

  /**
   * Gets the parameter map from the query string.
   * 
   * If the parameter map is already made, that instance is used.
   * 
   * I want to use the method {@link Window.Location#getParameterMap()}, but it
   * returns immutable map and list. The immutable map and list cannot be used
   * in the GWT-RPC. So I create the {@link WjrUtils#buildListParamMap(String)},
   * which is the same logic as
   * {@link Window.Location#buildListParamMap(String)} but returns not immutable
   * map.
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
        values.add(kv.length > 1 ? URL.decodeComponent(kv[1]) : "");
      }
    }
    return out;
  }
}
