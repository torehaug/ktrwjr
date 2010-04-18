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
 package bufferings.ktr.wjr.client.ui;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;
import bufferings.ktr.wjr.client.ui.widget.WjrIconButton;
import bufferings.ktr.wjr.client.ui.widget.WjrTree;
import bufferings.ktr.wjr.client.ui.widget.WjrTreeItem;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WjrTreePanel extends Composite {

  public static interface Handler {
    public void onClearButtonClicked(ClickEvent event);

    public void onReloadButtonClicked(ClickEvent event);

    public void onCheckAllButtonClicked(ClickEvent event);

    public void onUncheckAllButtonClicked(ClickEvent event);
  }

  private static WjrTreePanelUiBinder uiBinder =
    GWT.create(WjrTreePanelUiBinder.class);

  interface WjrTreePanelUiBinder extends UiBinder<Widget, WjrTreePanel> {
  }

  protected Handler handler;

  @UiField
  protected WjrIconButton checkAllButton;

  @UiField
  protected WjrIconButton uncheckAllButton;

  @UiField
  protected WjrIconButton expandAllButton;

  @UiField
  protected WjrIconButton collapseAllButton;

  @UiField
  protected WjrIconButton clearButton;

  @UiField
  protected WjrIconButton reloadButton;

  @UiField
  protected WjrTree tree;

  public WjrTreePanel(Handler handler) {
    this.handler = checkNotNull(handler, "The handler parameter is null.");
    initWidget(uiBinder.createAndBindUi(this));
  }

  public WjrTree getTree() {
    return tree;
  }

  @UiHandler("clearButton")
  public void onClearButtonClicked(ClickEvent event) {
    handler.onClearButtonClicked(event);
  }

  @UiHandler("reloadButton")
  public void onReloadButtonClicked(ClickEvent event) {
    handler.onReloadButtonClicked(event);
  }

  @UiHandler("checkAllButton")
  public void onCheckAllButtonClicked(ClickEvent event) {
    for (int i = 0; i < tree.getItemCount(); i++) {
      checkRecursive(tree.getItem(i), true);
    }
    handler.onCheckAllButtonClicked(event);
  }

  @UiHandler("uncheckAllButton")
  public void onUncheckAllButtonClicked(ClickEvent event) {
    for (int i = 0; i < tree.getItemCount(); i++) {
      checkRecursive(tree.getItem(i), false);
    }
    handler.onUncheckAllButtonClicked(event);
  }

  protected void checkRecursive(WjrTreeItem treeItem, boolean value) {
    treeItem.setChecked(value, false);
    for (int i = 0; i < treeItem.getChildCount(); i++) {
      checkRecursive(treeItem.getChild(i), value);
    }
  }

  @UiHandler("expandAllButton")
  public void onExpandAllButtonClicked(ClickEvent event) {
    for (int i = 0; i < tree.getItemCount(); i++) {
      WjrTreeItem treeItem = tree.getItem(i);
      expandRecursive(treeItem);
    }
  }

  protected void expandRecursive(WjrTreeItem treeItem) {
    treeItem.setState(true);
    for (int i = 0; i < treeItem.getChildCount(); i++) {
      expandRecursive(treeItem.getChild(i));
    }
  }

  @UiHandler("collapseAllButton")
  public void onCollapseAllButtonClicked(ClickEvent event) {
    for (int i = 0; i < tree.getItemCount(); i++) {
      WjrTreeItem treeItem = tree.getItem(i);
      collapseRecursive(treeItem);
    }
  }

  protected void collapseRecursive(WjrTreeItem treeItem) {
    for (int i = 0; i < treeItem.getChildCount(); i++) {
      collapseRecursive(treeItem.getChild(i));
    }
    treeItem.setState(false);
  }

  public void setClearButtonDisabled(boolean disabled) {
    clearButton.setDisabled(disabled);
  }

  public void setReloadButtonDisabled(boolean disabled) {
    reloadButton.setDisabled(disabled);
  }

  public void setCheckAllButtonDisabled(boolean disabled) {
    checkAllButton.setDisabled(disabled);
  }

  public void setUncheckAllButtonDisabled(boolean disabled) {
    uncheckAllButton.setDisabled(disabled);
  }

  public void setExpandAllButtonDisabled(boolean disabled) {
    expandAllButton.setDisabled(disabled);
  }

  public void setCollapseAllButtonDisabled(boolean disabled) {
    collapseAllButton.setDisabled(disabled);
  }
}
