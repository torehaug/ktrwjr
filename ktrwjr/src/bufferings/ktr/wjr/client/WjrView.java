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
import bufferings.ktr.wjr.client.ui.WjrPopupPanel;
import bufferings.ktr.wjr.client.ui.WjrResultPanel;
import bufferings.ktr.wjr.client.ui.WjrTracePanel;
import bufferings.ktr.wjr.client.ui.WjrTreePanel;
import bufferings.ktr.wjr.client.ui.widget.WjrTree;
import bufferings.ktr.wjr.client.ui.widget.WjrTreeItem;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
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
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WjrView extends Composite implements WjrDisplay,
    SelectionHandler<WjrTreeItem>, ValueChangeHandler<WjrTreeItem> {

  private static final String LOADING_POPUP_TEXT = "Loading...";

  private static final String RUNNING_POPUP_TEXT = "Running...";

  private static final String CANCELING_POPUP_TEXT = "Canceling...";

  private static WjrViewUiBinder uiBinder = GWT.create(WjrViewUiBinder.class);

  interface WjrViewUiBinder extends UiBinder<Widget, WjrView> {
  }

  protected WjrDisplayHandler handler;

  protected HasWidgets container;

  protected Element loadingElem;

  @UiField
  protected WjrResultPanel resultPanel;

  @UiField
  protected WjrTreePanel treePanel;

  @UiField
  protected WjrButtonPanel buttonPanel;

  @UiField
  protected WjrTracePanel tracePanel;

  protected WjrPopupPanel popup;

  protected List<WjrTreeItem> treeItems = new ArrayList<WjrTreeItem>();

  protected List<WjrStoreItem> storeItems = new ArrayList<WjrStoreItem>();

  boolean loading = false;

  boolean running = false;

  boolean canceled = false;

  @Override
  public void go(WjrDisplayHandler handler, HasWidgets container,
      Element loadingElem) {
    checkNotNull(handler, "The handler parameter is null.");
    checkNotNull(container, "The container parameter is null.");
    this.handler = handler;
    this.container = container;
    this.loadingElem = loadingElem;
  }

  @Override
  public void notifyLoadingSucceeded(WjrStore store) {
    finishLoading(store);
  }

  @Override
  public void notifyLoadingFailed(Throwable caught) {
    DialogBox dialog = new DialogBox();
    dialog.add(new Label(caught.toString()));
    dialog.show();
    finishLoading(new WjrStore());
  }

  private void finishLoading(WjrStore store) {
    initWidget(uiBinder.createAndBindUi(WjrView.this));
    popup = new WjrPopupPanel();

    setData(store);
    updateRunButtonDisabled();
    updateTreeButtonsDisabled();

    loadingElem.removeFromParent();
    container.add(this);
  }

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

  @Override
  public void notifyReloadingSucceeded() {
    finishReloading();
  }

  @Override
  public void notifyReloadingFailed(Throwable caught) {
    if (loading) {
      DialogBox dialog = new DialogBox();
      dialog.add(new Label(caught.toString()));
      dialog.show();
      finishReloading();
    }
  }

  private void finishReloading() {
    if (loading) {
      loading = false;
      updateRunButtonDisabled();
      updateTreeButtonsDisabled();
      popup.hide();
    }
  }

  @UiFactory
  protected WjrButtonPanel createWjrButtonPanel() {
    return new WjrButtonPanel(new WjrButtonPanel.Handler() {
      @Override
      public void onButtonClicked(ClickEvent evt) {
        if (!running) {
          startRunning();
        } else {
          cancelRunning();
        }
      }
    });
  }

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

  private void cancelRunning() {
    if (running && !canceled) {
      canceled = true;
      updateRunButtonDisabled();
      popup.setText(CANCELING_POPUP_TEXT);
      handler.onCancelButtonClick();
    }
  }

  @Override
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

  @UiFactory
  protected WjrTreePanel createWjrTreePanel() {
    return new WjrTreePanel(new WjrTreePanel.Handler() {
      @Override
      public void onCheckAllButtonClicked(ClickEvent event) {
        updateRunButtonDisabled();
      }

      @Override
      public void onClearButtonClicked(ClickEvent event) {
        handler.onClearButtonClick();
      }

      @Override
      public void onReloadButtonClicked(ClickEvent event) {
        startReloading();
      }

      @Override
      public void onUncheckAllButtonClicked(ClickEvent event) {
        updateRunButtonDisabled();
      }
    });
  }

  @Override
  public void setData(WjrStore store) {
    treeItems.clear();
    storeItems.clear();
    tracePanel.setTrace("");
    tracePanel.setLog("");
    updateResultPanel(store);

    WjrTree tree = treePanel.getTree();
    tree.removeItems();

    List<WjrClassItem> classStoreItems = store.getClassItems();
    for (WjrClassItem classStoreItem : classStoreItems) {
      WjrTreeItem classTreeItem = createTreeItem(classStoreItem);
      storeItems.add(classStoreItem);
      treeItems.add(classTreeItem);

      List<WjrMethodItem> methodStoreItems =
        store.getMethodItems(classStoreItem.getClassCanonicalName());
      storeItems.addAll(methodStoreItems);
      for (WjrMethodItem wjrMethodItem : methodStoreItems) {
        WjrTreeItem methodTreeItem = createTreeItem(wjrMethodItem);
        treeItems.add(methodTreeItem);
        classTreeItem.addItem(methodTreeItem);
      }

      tree.addItem(classTreeItem);
    }
  }

  private WjrTreeItem createTreeItem(WjrStoreItem storeItem) {
    WjrTreeItem treeItem = new WjrTreeItem();
    treeItem.addSelectionHandler(this);
    treeItem.addValueChangeHandler(this);
    repaintTreeItem(treeItem, storeItem);
    return treeItem;
  }

  @Override
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

  @Override
  public void repaintAllTreeItems(WjrStore store) {
    updateResultPanel(store);
    for (int i = 0, n = treeItems.size(); i < n; i++) {
      repaintTreeItem(treeItems.get(i), storeItems.get(i));
    }
  }

  @Override
  public void repaintTreeItemAncestors(WjrStore store, WjrMethodItem methodItem) {
    updateResultPanel(store);

    WjrTreeItem treeItem = treeItems.get(storeItems.indexOf(methodItem));
    repaintTreeItem(treeItem, methodItem);

    WjrTreeItem parentTreeItem = treeItem.getParentItem();
    repaintTreeItem(parentTreeItem, storeItems.get(treeItems
      .indexOf(parentTreeItem)));
  }

  private void updateResultPanel(WjrStore store) {
    resultPanel.updateResults(
      store.getTotalCount() - store.getNotYetCount(),
      store.getTotalCount(),
      store.getErrorCount(),
      store.getFailureCount());
  }

  private void repaintTreeItem(WjrTreeItem treeItem, WjrStoreItem storeItem) {
    treeItem.setText(getTreeItemText(storeItem));
    treeItem.setIcon(getTreeItemIcon(storeItem));
    if (treeItem.isSelected()) {
      treeItem.setSelectedStyle(getTreeItemSelectedStyle(storeItem));
      tracePanel.setTrace(getTreeItemTrace(storeItem));
      tracePanel.setLog(getTreeItemLog(storeItem));
    }
  }

  @Override
  public void onSelection(SelectionEvent<WjrTreeItem> event) {
    WjrTreeItem treeItem = event.getSelectedItem();
    if (!treeItem.isSelected()) {
      treeItem.setSelectedStyle("");
      tracePanel.setTrace("");
      tracePanel.setLog("");
    } else {
      WjrStoreItem storeItem =
        (WjrStoreItem) storeItems.get(treeItems.indexOf(treeItem));
      treeItem.setSelectedStyle(getTreeItemSelectedStyle(storeItem));
      tracePanel.setTrace(getTreeItemTrace(storeItem));
      tracePanel.setLog(getTreeItemLog(storeItem));
    }
  }

  @Override
  public void onValueChange(ValueChangeEvent<WjrTreeItem> event) {
    updateRunButtonDisabled();
  }

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
    default:
      // GWT does not emulate format, so i use the format method of Guice's
      // Preconditions class.
      throw new AssertionError(format("Unknown state. [State=%s]", storeItem
        .getState()
        .name()));
    }
  }

  private String getTreeItemSelectedStyle(WjrStoreItem storeItem) {
    switch (storeItem.getState()) {
    case SUCCESS:
    case NOT_YET:
    case RUNNING:
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

  private String getTreeItemText(WjrStoreItem storeItem) {
    if (storeItem instanceof WjrClassItem) {
      return getTreeItemTextFromClassItem((WjrClassItem) storeItem);
    } else {
      return getTreeItemTextFromMethodItem((WjrMethodItem) storeItem);
    }
  }

  private String getTreeItemTextFromClassItem(WjrClassItem classItem) {
    return classItem.getClassCanonicalName();
  }

  private String getTreeItemTextFromMethodItem(WjrMethodItem methodItem) {
    StringBuilder sb = new StringBuilder(methodItem.getMethodSimpleName());

    if (methodItem.getState() != State.NOT_YET
      && methodItem.getState() != State.RUNNING) {

      sb.append(" (").append(getTimeString(methodItem)).append("api_ms)");
    }

    return sb.toString();
  }

  private String getTimeString(WjrMethodItem methodItem) {
    StringBuilder sb = new StringBuilder();

    String time = methodItem.getTime();
    sb.append(time != null ? time : "-").append("ms ");

    String cpuTime = methodItem.getCpuTime();
    sb.append(cpuTime != null ? cpuTime : "-").append("cpu_ms ");

    String apiTime = methodItem.getApiTime();
    sb.append(apiTime != null ? apiTime : "-").append("api_ms");

    return sb.toString();
  }

  private String getTreeItemLog(WjrStoreItem storeItem) {
    if (storeItem instanceof WjrClassItem) {
      return "";
    } else {
      WjrMethodItem methodItem = (WjrMethodItem) storeItem;
      if (methodItem.getState() == State.NOT_YET
        || methodItem.getState() == State.RUNNING) {
        return "";
      } else {
        String log = methodItem.getLog();
        return getTimeString(methodItem) + (log != null ? "\n" + log : "");
      }
    }
  }

  private String getTreeItemTrace(WjrStoreItem storeItem) {
    if (storeItem instanceof WjrClassItem) {
      return "";
    } else {
      WjrMethodItem methodItem = (WjrMethodItem) storeItem;
      String trace = methodItem.getTrace();
      return (trace != null ? trace : "");
    }
  }
}
