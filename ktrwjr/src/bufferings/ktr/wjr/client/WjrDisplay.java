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

import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * KtrWjr view interface.
 * 
 * @author bufferings[at]gmail.com
 */
public interface WjrDisplay {

  /**
   * Starts the view.
   * 
   * @param handler
   *          The view event handler.
   * @param container
   *          The view container.
   */
  public void go(WjrDisplayHandler handler, HasWidgets container);

  /**
   * Sets the store data.
   * 
   * @param store
   *          The test store.
   */
  public void setData(WjrStore store);

  /**
   * Gets the checked method items.
   * 
   * @return The checked method items.
   */
  public List<WjrMethodItem> getCheckedMethodItems();

  /**
   * Update the summary of the all tree items and repaints them.
   * 
   * @param store
   *          The test store.
   */
  public void repaintAllTreeItems(WjrStore store);

  /**
   * Update the summary of the ancestor tree items of the method item and
   * repaints them.
   * 
   * When the method is run and the result is updated, you only have to do is
   * updating its ancestors, not all summary items.
   * 
   * @param store
   *          The test store.
   * @param methodItem
   *          The method item.
   */
  public void repaintTreeItemAncestors(WjrStore store, WjrMethodItem methodItem);

  /**
   * Notifies loading the configuration succeeded to the view.
   * 
   * @param config
   *          The user configuration.
   */
  public void notifyLoadingConfigSucceeded(WjrConfig config);

  /**
   * Notifies loading the configuration failed to the view.
   * 
   * @param config
   *          The user configuration.
   * @param caught
   *          The cause.
   */
  public void notifyLoadingConfigFailed(WjrConfig config, Throwable caught);

  /**
   * Notifies loading the test store succeeded to the view.
   */
  public void notifyLoadingStoreSucceeded();

  /**
   * Notifies loading the test store failed to the view.
   * 
   * @param caught
   *          The cause.
   */
  public void notifyLoadingStoreFailed(Throwable caught);

  /**
   * Notifies running the test is filnished.
   * 
   * When the running test is failed, the failed info is set to methodItem, so
   * the failed notifier is not prepared.
   */
  public void notifyRunningFinished();
}