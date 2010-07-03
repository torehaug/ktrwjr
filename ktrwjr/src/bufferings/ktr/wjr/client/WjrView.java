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

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;
import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import bufferings.ktr.wjr.client.ui.WjrButtonPanel;
import bufferings.ktr.wjr.client.ui.WjrDialogPanel;
import bufferings.ktr.wjr.client.ui.WjrPopupPanel;
import bufferings.ktr.wjr.client.ui.WjrResultPanel;
import bufferings.ktr.wjr.client.ui.WjrTracePanel;
import bufferings.ktr.wjr.client.ui.WjrTreePanel;
import bufferings.ktr.wjr.client.ui.widget.WjrTree;
import bufferings.ktr.wjr.client.ui.widget.WjrTreeItem;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * The view of the Kotori Web JUnit Runner.
 * 
 * This class controls all the view components of KtrWjr.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrView extends Composite implements WjrDisplay,
    SelectionHandler<WjrTreeItem>, ValueChangeHandler<WjrTreeItem> {

  private static final String LOADING_POPUP_TEXT = "Loading...";

  private static final String RUNNING_POPUP_TEXT = "Running...";

  private static final String CANCELING_POPUP_TEXT = "Canceling...";

  private static WjrViewUiBinder uiBinder = GWT.create(WjrViewUiBinder.class);

  interface WjrViewUiBinder extends UiBinder<Widget, WjrView> {
  }

  /**
   * The view event handler.
   */
  protected WjrDisplayHandler handler;

  /**
   * The container widget of this view.
   */
  protected HasWidgets container;

  /**
   * The result panel which shows the result summary of the tests.
   */
  @UiField
  protected WjrResultPanel resultPanel;

  /**
   * The tree panel which shows the test cases and some icon buttons.
   */
  @UiField
  protected WjrTreePanel treePanel;

  /**
   * The button panel of the run and the cancel button.
   */
  @UiField
  protected WjrButtonPanel buttonPanel;

  /**
   * The trace panel which shows the trace and the log.
   */
  @UiField
  protected WjrTracePanel tracePanel;

  /**
   * The popup panel which is shown while the process is running.
   */
  protected WjrPopupPanel popup;

  /**
   * The dialog panel which shows the error message.
   */
  protected WjrDialogPanel dialog;

  /**
   * The tree items to associate with the store items.
   */
  protected List<WjrTreeItem> treeItems = new ArrayList<WjrTreeItem>();

  /**
   * The store items to associate with the tree items. The store items is read
   * only.
   */
  protected List<WjrStoreItem> storeItems = new ArrayList<WjrStoreItem>();

  /**
   * Whether loading the store or not.
   */
  protected boolean loading = false;

  /**
   * Whether running the tests or not.
   */
  protected boolean running = false;

  /**
   * Whether canceled running the tests or not.
   */
  protected boolean canceled = false;

  /**
   * The configuration.
   */
  protected WjrConfig config = new WjrConfig();

  /**
   * UiFactory method which creates WjrButtonPanel and handles its events.
   * 
   * @return The instance of the WjrButtonPanel.
   */
  @UiFactory
  protected WjrButtonPanel createWjrButtonPanel() {
    return new WjrButtonPanel(new WjrButtonPanel.Handler() {

      public void onButtonClicked(ClickEvent evt) {
        if (!running) {
          startRunning();
        } else {
          cancelRunning();
        }
      }
    });
  }

  /**
   * UiFactory method which creates WjrTreePanel and handles its events.
   * 
   * @return The instance of the WjrTreePanel.
   */
  @UiFactory
  protected WjrTreePanel createWjrTreePanel() {
    return new WjrTreePanel(new WjrTreePanel.Handler() {
      public void onCheckAllButtonClicked(ClickEvent event) {
        updateRunButtonDisabled();
      }

      public void onClearButtonClicked(ClickEvent event) {
        handler.onClearButtonClick();
      }

      public void onReloadButtonClicked(ClickEvent event) {
        startReloading();
      }

      public void onUncheckAllButtonClicked(ClickEvent event) {
        updateRunButtonDisabled();
      }
    });
  }

  /**
   * {@inheritDoc}
   */
  public void go(WjrDisplayHandler handler, HasWidgets container) {
    checkNotNull(handler, "The handler parameter is null.");
    checkNotNull(container, "The container parameter is null.");
    this.handler = handler;
    this.container = container;

    dialog = new WjrDialogPanel("Kotori Web JUnit Runner");
    popup = new WjrPopupPanel();
  }

  /**
   * {@inheritDoc}
   */
  public void notifyLoadingConfigSucceeded(WjrConfig config) {
    finishLoadingConfig(config);
    startReloading();
  }

  /**
   * {@inheritDoc}
   */
  public void notifyLoadingConfigFailed(WjrConfig config, Throwable caught) {
    dialog.show("Cannot load the tests.", caught);
    finishLoadingConfig(config);
  }

  /**
   * Performs the post-processing of loading configuration.
   * 
   * Shows the KtrWjr panels.
   * 
   * @param config
   *          The user configuration.
   */
  private void finishLoadingConfig(WjrConfig config) {
    initWidget(uiBinder.createAndBindUi(this));

    applyConfig(config);
    setData(new WjrStore());
    updateRunButtonDisabled();
    updateTreeButtonsDisabled();

    container.add(this);
  }

  /**
   * Sets the configuration.
   * 
   * @param newConfig
   *          The configuration
   */
  private void applyConfig(WjrConfig newConfig) {
    config = newConfig;
    tracePanel.setTraceTabVisible(config.isLogHookEnabled());
  }

  /**
   * {@inheritDoc}
   */
  public void notifyLoadingStoreSucceeded() {
    finishReloading();
  }

  /**
   * {@inheritDoc}
   */
  public void notifyLoadingStoreFailed(Throwable caught) {
    dialog.show("Cannot load the tests.", caught);
    finishReloading();
  }

  /**
   * {@inheritDoc}
   */
  public void notifyRunningFinished() {
    if (running) {
      running = false;
      canceled = false;
      buttonPanel.changeToRunButton();
      popup.hide();
      updateRunButtonDisabled();
      updateTreeButtonsDisabled();
    }
  }

  /**
   * {@inheritDoc}
   */
  public void setData(WjrStore store) {
    treeItems.clear();
    storeItems.clear();

    updateTracePanel(null);
    updateResultPanel(store);

    WjrTree tree = treePanel.getTree();
    tree.removeItems();

    List<WjrClassItem> classStoreItems = store.getClassItems();
    for (WjrClassItem classStoreItem : classStoreItems) {
      WjrTreeItem classTreeItem = createTreeItem(classStoreItem);
      storeItems.add(classStoreItem);
      treeItems.add(classTreeItem);

      List<WjrMethodItem> methodStoreItems =
        store.getMethodItems(classStoreItem.getClassName());
      storeItems.addAll(methodStoreItems);
      for (WjrMethodItem wjrMethodItem : methodStoreItems) {
        WjrTreeItem methodTreeItem = createTreeItem(wjrMethodItem);
        treeItems.add(methodTreeItem);
        classTreeItem.addItem(methodTreeItem);
      }

      tree.addItem(classTreeItem);
    }
  }

  /**
   * {@inheritDoc}
   */
  public List<WjrMethodItem> getCheckedMethodItems() {
    List<WjrMethodItem> ret = new ArrayList<WjrMethodItem>();
    for (int i = 0, n = treeItems.size(); i < n; i++) {
      if (treeItems.get(i).isChecked()) {
        WjrStoreItem storeItem = storeItems.get(i);
        if (storeItem instanceof WjrMethodItem) {
          ret.add((WjrMethodItem) storeItem);
        }
      }
    }
    return ret;
  }

  /**
   * {@inheritDoc}
   */
  public void repaintAllTreeItems(WjrStore store) {
    updateResultPanel(store);
    for (int i = 0, n = treeItems.size(); i < n; i++) {
      repaintTreeItem(treeItems.get(i), storeItems.get(i));
    }
  }

  /**
   * {@inheritDoc}
   */
  public void repaintTreeItemAncestors(WjrStore store, WjrMethodItem methodItem) {
    updateResultPanel(store);

    WjrTreeItem treeItem = treeItems.get(storeItems.indexOf(methodItem));
    repaintTreeItem(treeItem, methodItem);

    WjrTreeItem parentTreeItem = treeItem.getParentItem();
    repaintTreeItem(parentTreeItem, storeItems.get(treeItems
      .indexOf(parentTreeItem)));
  }

  /**
   * {@inheritDoc}
   */
  public void onSelection(SelectionEvent<WjrTreeItem> event) {
    WjrTreeItem treeItem = event.getSelectedItem();
    if (!treeItem.isSelected()) {
      treeItem.setSelectedStyle("");
      updateTracePanel(null);
    } else {
      WjrStoreItem storeItem =
        (WjrStoreItem) storeItems.get(treeItems.indexOf(treeItem));
      treeItem.setSelectedStyle(getTreeItemSelectedStyle(storeItem));
      updateTracePanel(storeItem);
    }
  }

  /**
   * {@inheritDoc}
   */
  public void onValueChange(ValueChangeEvent<WjrTreeItem> event) {
    updateRunButtonDisabled();
  }

  /**
   * Starts reloading the test store.
   */
  private void startReloading() {
    if (!loading) {
      loading = true;
      setData(new WjrStore());
      updateRunButtonDisabled();
      updateTreeButtonsDisabled();

      popup.setText(LOADING_POPUP_TEXT);
      popup.show();
      handler.onLoadStore();
    }
  }

  /**
   * Performs the post-processing of reloading.
   */
  private void finishReloading() {
    if (loading) {
      loading = false;
      updateRunButtonDisabled();
      updateTreeButtonsDisabled();
      popup.hide();
    }
  }

  /**
   * Starts running the test methods.
   */
  private void startRunning() {
    if (!running) {
      running = true;
      canceled = false;
      buttonPanel.changeToCancelButton();
      updateTreeButtonsDisabled();
      popup.setText(RUNNING_POPUP_TEXT);
      popup.show();
      handler.onRunButtonClick();
    }
  }

  /**
   * Cancel running the test methods.
   */
  private void cancelRunning() {
    if (running && !canceled) {
      canceled = true;
      updateRunButtonDisabled();
      popup.setText(CANCELING_POPUP_TEXT);
      handler.onCancelButtonClick();
    }
  }

  /**
   * Updates the tree panel buttons' disabled.
   */
  private void updateTreeButtonsDisabled() {
    if (treeItems.size() == 0) {
      treePanel.setCheckAllButtonDisabled(true);
      treePanel.setUncheckAllButtonDisabled(true);
      treePanel.setExpandAllButtonDisabled(true);
      treePanel.setCollapseAllButtonDisabled(true);
      treePanel.setClearButtonDisabled(true);
      if (loading) {
        treePanel.setReloadButtonDisabled(true);
      } else {
        treePanel.setReloadButtonDisabled(false);
      }
    } else {
      treePanel.setCheckAllButtonDisabled(false);
      treePanel.setUncheckAllButtonDisabled(false);
      treePanel.setExpandAllButtonDisabled(false);
      treePanel.setCollapseAllButtonDisabled(false);
      if (running) {
        treePanel.setClearButtonDisabled(true);
        treePanel.setReloadButtonDisabled(true);
      } else {
        treePanel.setClearButtonDisabled(false);
        treePanel.setReloadButtonDisabled(false);
      }
    }
  }

  /**
   * Create the tree item from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item.
   */
  private WjrTreeItem createTreeItem(WjrStoreItem storeItem) {
    WjrTreeItem treeItem = new WjrTreeItem();
    treeItem.addSelectionHandler(this);
    treeItem.addValueChangeHandler(this);
    repaintTreeItem(treeItem, storeItem);
    return treeItem;
  }

  /**
   * Updates the result panel from the test store.
   * 
   * @param store
   *          The test store.
   */
  private void updateResultPanel(WjrStore store) {
    int runningsCount = store.getRunningCount() + store.getRetryWaitingCount();
    int runsCount =
      store.getTotalCount() - store.getNotYetCount() - runningsCount;
    resultPanel.updateResults(
      runningsCount,
      runsCount,
      store.getTotalCount(),
      store.getErrorCount(),
      store.getFailureCount());
  }

  /**
   * Updates the trace panel with the selected tree item.
   * 
   * @param selectedItem
   *          The selected tree item. If no item is selected, set null.
   */
  private void updateTracePanel(WjrStoreItem selectedItem) {
    tracePanel.setTrace(getTreeItemTrace(selectedItem));
    if (config.isLogHookEnabled()) {
      tracePanel.setLog(getTreeItemLog(selectedItem));
    }
  }

  /**
   * Repaints the tree item by the store item.
   * 
   * @param treeItem
   *          The tree item.
   * @param storeItem
   *          The test store item.
   */
  private void repaintTreeItem(WjrTreeItem treeItem, WjrStoreItem storeItem) {
    treeItem.setText(getTreeItemText(storeItem));
    treeItem.setIcon(getTreeItemIcon(storeItem));
    if (treeItem.isSelected()) {
      treeItem.setSelectedStyle(getTreeItemSelectedStyle(storeItem));
      updateTracePanel(storeItem);
    }
  }

  /**
   * Updates the run (or cancel) button disabled.
   */
  private void updateRunButtonDisabled() {
    if (loading) {
      buttonPanel.setButtonDisabled(true);
      return;
    }

    if (running) {
      buttonPanel.setButtonDisabled(canceled);
      return;
    }

    boolean checked = false;
    for (WjrTreeItem treeItem : treeItems) {
      if (treeItem.isChecked()) {
        checked = true;
        break;
      }
    }
    buttonPanel.setButtonDisabled(!checked);
  }

  /**
   * Gets the tree item icon from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item icon css class of JQueryUI.
   */
  private String getTreeItemIcon(WjrStoreItem storeItem) {
    switch (storeItem.getState()) {
    case SUCCESS:
      return UI_ICON_CHECK;
    case FAILURE:
      return UI_ICON_NOTICE;
    case ERROR:
      return UI_ICON_CLOSE;
    case NOT_YET:
      return UI_ICON_MINUS;
    case RUNNING:
      return UI_ICON_ARRORREFRESH_1_W;
    case RETRY_WAITING:
      return UI_ICON_CLOCK;
    default:
      // GWT does not emulate format, so i use the format method of Guice's
      // Preconditions class.
      throw new AssertionError(format("Unknown state. [State=%s]", storeItem
        .getState()
        .name()));
    }
  }

  /**
   * Gets the tree item selected style from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item selected css class name of JQueryUI.
   */
  private String getTreeItemSelectedStyle(WjrStoreItem storeItem) {
    switch (storeItem.getState()) {
    case SUCCESS:
    case NOT_YET:
    case RUNNING:
    case RETRY_WAITING:
      return UI_STATE_HIGHLIGHT;
    case FAILURE:
    case ERROR:
      return UI_STATE_ERROR;
    default:
      // GWT does not emulate format, so i use the format method of Guice's
      // Preconditions class.
      throw new AssertionError(format("Unknown state. [State=%s]", storeItem
        .getState()
        .name()));
    }
  }

  /**
   * Gets the tree item text from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item text.
   */
  private String getTreeItemText(WjrStoreItem storeItem) {
    if (storeItem instanceof WjrClassItem) {
      return getTreeItemTextFromClassItem((WjrClassItem) storeItem);
    } else {
      return getTreeItemTextFromMethodItem((WjrMethodItem) storeItem);
    }
  }

  /**
   * Gets the tree item text from the test class item.
   * 
   * @param classItem
   *          The test class item.
   * @return The tree item text.
   */
  private String getTreeItemTextFromClassItem(WjrClassItem classItem) {
    return classItem.getClassName();
  }

  /**
   * Gets the tree item text from the test method item.
   * 
   * @param methodItem
   *          The test method item.
   * @return The tree item text.
   */
  private String getTreeItemTextFromMethodItem(WjrMethodItem methodItem) {
    StringBuilder sb = new StringBuilder(methodItem.getMethodName());

    if (methodItem.getState() == State.RETRY_WAITING) {
      sb.append(" (").append(getRetryWaitingString(methodItem)).append(")");
    } else if (methodItem.getState() != State.NOT_YET
      && methodItem.getState() != State.RUNNING) {
      sb.append(" (").append(getTimeString(methodItem)).append(")");
    }

    return sb.toString();
  }

  /**
   * Gets the time string from the test method item.
   * 
   * @param methodItem
   *          The test method item.
   * @return The time string.
   */
  private String getTimeString(WjrMethodItem methodItem) {
    StringBuilder sb = new StringBuilder();

    String time = methodItem.getTime();
    sb.append(time.length() > 0 ? time : "-").append("ms");

    if (config.isCpumsEnabled()) {
      String cpuTime = methodItem.getCpuTime();
      sb.append(" ");
      sb.append(cpuTime.length() > 0 ? cpuTime : "-").append("cpu_ms");
    }

    if (config.isApimsEnabled()) {
      String apiTime = methodItem.getApiTime();
      sb.append(" ");
      sb.append(apiTime.length() > 0 ? apiTime : "-").append("api_ms");
    }

    return sb.toString();
  }

  /**
   * Gets the retry waiting string from the test method item.
   * 
   * @param methodItem
   *          The test method item.
   * @return The time string.
   */
  private Object getRetryWaitingString(WjrMethodItem methodItem) {
    StringBuilder sb = new StringBuilder();
    sb.append("waiting:");
    sb.append(methodItem.getWaitingSeconds());
    sb.append("s retry count:");
    sb.append(methodItem.getRetryCount());
    sb.append("/");
    sb.append(methodItem.getMaxRetryCount());
    return sb.toString();
  }

  /**
   * Gets the tree item log from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item log.
   */
  private String getTreeItemLog(WjrStoreItem storeItem) {
    if (storeItem == null || storeItem instanceof WjrClassItem) {
      return null;
    } else {
      WjrMethodItem methodItem = (WjrMethodItem) storeItem;
      if (methodItem.getState() == State.NOT_YET
        || methodItem.getState() == State.RUNNING
        || methodItem.getState() == State.RETRY_WAITING) {
        return null;
      } else {
        String log = methodItem.getLog();
        return getTimeString(methodItem) + (log != null ? "\n" + log : "");
      }
    }
  }

  /**
   * Gets the tree item trace from the test store item.
   * 
   * @param storeItem
   *          The test store item.
   * @return The tree item trace. If the storeItem is null or the trace is not
   *         set, returns null.
   */
  private String getTreeItemTrace(WjrStoreItem storeItem) {
    if (storeItem == null || storeItem instanceof WjrClassItem) {
      return null;
    } else {
      WjrMethodItem methodItem = (WjrMethodItem) storeItem;
      if (methodItem.getState() == State.NOT_YET
        || methodItem.getState() == State.RUNNING
        || methodItem.getState() == State.RETRY_WAITING) {
        return null;
      } else {
        String trace = methodItem.getTrace();
        return (trace != null ? trace : "");
      }
    }
  }
}
