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
package bufferings.ktr.wjr.client.runner;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bufferings.ktr.wjr.client.WjrDisplay;
import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;

/**
 * The abstract runner.
 * 
 * This class defines the basic common fields and methods.
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class AbstractRunner implements WjrRunner {

  /**
   * The KtrWjr rpc service(async).
   */
  protected KtrWjrServiceAsync rpcService;

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
   * The retry timers. The key is the classAndMethodName.
   */
  protected Map<String, Timer> retryTimerMap = new HashMap<String, Timer>();

  /**
   * Whether the test is running or not.
   */
  protected boolean running = false;

  /**
   * Whether the cancel running tests is requested or not.
   */
  protected boolean cancelRequested = false;

  /**
   * Constructs the runner.
   * 
   * This class receives information from presenter, and use it. This class is a
   * part of presenter.
   * 
   * @param rpcService
   *          The rpc service.
   * @param view
   *          The view.
   * @param config
   *          The configuration.
   * @param store
   *          The test store.
   */
  public AbstractRunner(KtrWjrServiceAsync rpcService, WjrDisplay view,
      WjrConfig config, WjrStore store) {
    checkNotNull(rpcService, "The rpcService is null.");
    checkNotNull(view, "The view is null.");
    checkNotNull(config, "The config is null.");
    checkNotNull(store, "The store is null.");

    this.rpcService = rpcService;
    this.view = view;
    this.config = config;
    this.store = store;
  }

  /**
   * {@inheritDoc}
   */
  public boolean isRunning() {
    return running;
  }

  /**
   * {@inheritDoc}
   */
  public void cancelRunning() {
    cancelRequested = true;
    for (Timer retryTimer : retryTimerMap.values()) {
      retryTimer.cancel();
    }
    retryTimerMap.clear();
  }

  /**
   * {@inheritDoc}
   */
  public void run() {
    checkState(!running, "Tests are running.");

    List<WjrMethodItem> methodItemsToRun = view.getCheckedMethodItems();
    if (methodItemsToRun.size() == 0) {
      GWT.log("No method items are checked.");
      view.notifyRunningFinished();
      return;
    }

    running = true;
    cancelRequested = false;

    prepareItems(methodItemsToRun);
    runBare(methodItemsToRun);
  }

  /**
   * Changes the state of items, and repaint tree.
   * 
   * @param methodItemsToRun
   *          The method items to run.
   */
  protected void prepareItems(List<WjrMethodItem> methodItemsToRun) {
    int maxRetryCount = config.getRetryOverQuotaMaxCount();
    int waitingSeconds = config.getRetryOverQuotaInterval();
    for (WjrMethodItem item : methodItemsToRun) {
      item.clearResult();
      item.setState(State.RUNNING);
      item.setMaxRetryCount(maxRetryCount);
      item.setWaitingSeconds(waitingSeconds);
    }
    store.updateAllSummaries();
    view.repaintAllTreeItems(store);
  }

  /**
   * Gets the trace string from Throwable.
   * 
   * @param e
   *          Throwable.
   * @return The trace string.
   */
  protected String getTrace(Throwable e) {
    return e.toString();
  }

  /**
   * Postprocessing of running tests.
   * 
   * @param methodItemsToRun
   *          The method items to run.
   */
  protected void onFinishRunning(List<WjrMethodItem> methodItemsToRun) {
    for (WjrMethodItem item : methodItemsToRun) {
      if (item.getState() == State.RUNNING) {
        item.setState(State.NOT_YET);
      } else if (item.getState() == State.RETRY_WAITING) {
        item.setState(State.ERROR);
      }
    }
    store.updateAllSummaries();
    view.repaintAllTreeItems(store);

    running = false;
    cancelRequested = false;
    view.notifyRunningFinished();
  }

  /**
   * Runs the tests.
   * 
   * @param methodItemsToRun
   *          The method items to run.
   */
  protected abstract void runBare(List<WjrMethodItem> methodItemsToRun);
}
