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

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * The tree panel which shows the test cases and some icon buttons.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrTreePanel extends Composite {

  /**
   * The event handler of the WjrTreePanel.
   * 
   * @author bufferings[at]gmail.com
   */
  public static interface Handler {
    /**
     * Called when the clear button is clicked.
     * 
     * @param event
     *          The event information.
     */
    public void onClearButtonClicked(ClickEvent event);

    /**
     * Called when the reload button is clicked.
     * 
     * @param event
     *          The event information.
     */
    public void onReloadButtonClicked(ClickEvent event);

    /**
     * Called when the check-all button is clicked.
     * 
     * @param event
     *          The event information.
     */
    public void onCheckAllButtonClicked(ClickEvent event);

    /**
     * Called when the uncheck-all button is clicked.
     * 
     * @param event
     *          The event information.
     */
    public void onUncheckAllButtonClicked(ClickEvent event);
  }

  private static WjrTreePanelUiBinder uiBinder =
    GWT.create(WjrTreePanelUiBinder.class);

  interface WjrTreePanelUiBinder extends UiBinder<Widget, WjrTreePanel> {
  }

  /**
   * The event handler of the WjrTreePanel.
   */
  protected Handler handler;

  /**
   * The check-all button.
   */
  @UiField
  protected WjrIconButton checkAllButton;

  /**
   * The uncheck-all button.
   */
  @UiField
  protected WjrIconButton uncheckAllButton;

  /**
   * The expand-all button.
   */
  @UiField
  protected WjrIconButton expandAllButton;

  /**
   * The collapse-all button.
   */
  @UiField
  protected WjrIconButton collapseAllButton;

  /**
   * The clear button.
   */
  @UiField
  protected WjrIconButton clearButton;

  /**
   * The reload button.
   */
  @UiField
  protected WjrIconButton reloadButton;

  /**
   * The tree composite.
   */
  @UiField
  protected WjrTree tree;

  /**
   * Constructs the WjrTreePanel with the event handler.
   * 
   * @param handler
   *          The event handler of this class.
   * @throws NullPointerException
   *           If the handler parameter is null.
   */
  public WjrTreePanel(Handler handler) {
    this.handler = checkNotNull(handler, "The handler parameter is null.");
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Gets the tree composite.
   * 
   * @return The tree composite.
   */
  public WjrTree getTree() {
    return tree;
  }

  /**
   * Called when the clear button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("clearButton")
  public void onClearButtonClicked(ClickEvent event) {
    handler.onClearButtonClicked(event);
  }

  /**
   * Called when the reload button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("reloadButton")
  public void onReloadButtonClicked(ClickEvent event) {
    handler.onReloadButtonClicked(event);
  }

  /**
   * Called when the checkAll button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("checkAllButton")
  public void onCheckAllButtonClicked(ClickEvent event) {
    tree.checkAllItems();
    handler.onCheckAllButtonClicked(event);
  }

  /**
   * Called when the uncheckAll button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("uncheckAllButton")
  public void onUncheckAllButtonClicked(ClickEvent event) {
    tree.uncheckAllItems();
    handler.onUncheckAllButtonClicked(event);
  }

  /**
   * Called when the expandAll button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("expandAllButton")
  public void onExpandAllButtonClicked(ClickEvent event) {
    tree.expandAllItems();
  }

  /**
   * Called when the collapseAll button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("collapseAllButton")
  public void onCollapseAllButtonClicked(ClickEvent event) {
    tree.collapseAllItems();
  }

  /**
   * Sets the clear button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setClearButtonDisabled(boolean disabled) {
    clearButton.setDisabled(disabled);
  }

  /**
   * Sets the reload button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setReloadButtonDisabled(boolean disabled) {
    reloadButton.setDisabled(disabled);
  }

  /**
   * Sets the checkAll button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setCheckAllButtonDisabled(boolean disabled) {
    checkAllButton.setDisabled(disabled);
  }

  /**
   * Sets the uncheckAll button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setUncheckAllButtonDisabled(boolean disabled) {
    uncheckAllButton.setDisabled(disabled);
  }

  /**
   * Sets the expandAll button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setExpandAllButtonDisabled(boolean disabled) {
    expandAllButton.setDisabled(disabled);
  }

  /**
   * Sets the collapseAll button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setCollapseAllButtonDisabled(boolean disabled) {
    collapseAllButton.setDisabled(disabled);
  }
}
