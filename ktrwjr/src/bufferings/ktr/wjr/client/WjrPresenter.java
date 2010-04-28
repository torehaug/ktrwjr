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

import java.util.List;

import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class WjrPresenter implements WjrDisplayHandler {

  protected KtrWjrServiceAsync rpcService;

  protected WjrDisplay view;

  protected WjrStore store;

  protected boolean running = false;

  protected boolean cancelRequested = false;

  public WjrPresenter(KtrWjrServiceAsync rpcService, WjrDisplay view) {
    this.rpcService = rpcService;
    this.view = view;
  }

  public void go(HasWidgets container, Element loadingElem) {
    view.go(this, container, loadingElem);
    rpcService.loadStore(new AsyncCallback<WjrStore>() {

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

  @Override
  public void onLoadStore() {
    rpcService.loadStore(new AsyncCallback<WjrStore>() {

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

  @Override
  public void onClearButtonClick() {
    store.clearAllResultsAndSummaries();
    view.repaintAllTreeItems(store);
  }

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

  @Override
  public void onCancelButtonClick() {
    if (!running) {
      GWT.log("No tests are running.");
      return;
    }
    cancelRequested = true;
  }

  protected void runWjrMethodItem(final List<WjrMethodItem> checkedMethods,
      final int currentIndex) {

    rpcService.runTest(
      checkedMethods.get(currentIndex),
      new AsyncCallback<WjrMethodItem>() {
        @Override
        public void onFailure(Throwable caught) {
          GWT.log("Run WjrMethodItem failed.", caught);

          WjrMethodItem stored = checkedMethods.get(currentIndex);
          stored.setState(State.ERROR);
          stored.setTrace(getTrace(caught));

          store.getClassItem(stored.getClassName()).updateSummary(
            store);
          store.updateSummary();

          view.repaintTreeItemAncestors(store, stored);

          int nextIndex = currentIndex + 1;
          if (checkedMethods.size() <= nextIndex) {
            onRunSuccess();
            return;
          }

          if (cancelRequested) {
            onRunCancel();
            return;
          }

          runWjrMethodItem(checkedMethods, nextIndex);
        }

        @Override
        public void onSuccess(WjrMethodItem result) {
          GWT.log("Run WjrMethodItem succeeded.");

          WjrMethodItem stored =
            store.getMethodItem(result.getClassAndMethodName());
          copyMethodItemAttributes(result, stored);

          store.getClassItem(stored.getClassName()).updateSummary(
            store);
          store.updateSummary();
          view.repaintTreeItemAncestors(store, stored);

          int nextIndex = currentIndex + 1;
          if (checkedMethods.size() <= nextIndex) {
            onRunSuccess();
            return;
          }

          if (cancelRequested) {
            onRunCancel();
            return;
          }

          runWjrMethodItem(checkedMethods, nextIndex);
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
          for (WjrMethodItem item : checkedMethods) {
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
}
