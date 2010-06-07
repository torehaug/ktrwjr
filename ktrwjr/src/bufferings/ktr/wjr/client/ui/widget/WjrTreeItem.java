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
package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The tree item composite with JQueryUI theme.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrTreeItem extends Composite implements
    HasValueChangeHandlers<WjrTreeItem>, HasSelectionHandlers<WjrTreeItem> {

  private static WjrTreeItemUiBinder uiBinder =
    GWT.create(WjrTreeItemUiBinder.class);

  interface WjrTreeItemUiBinder extends UiBinder<Widget, WjrTreeItem> {
  }

  /**
   * CssResource used in the WjrTreeItem.
   * 
   * @author bufferings[at]gmail.com
   */
  protected interface MyStyle extends CssResource {
    /**
     * The item panel basic style.
     * 
     * @return The item panel basic style.
     */
    String itemPanel();
  }

  /**
   * Hoverable simple panel.
   * 
   * @author bufferings[at]gmail.com
   */
  protected static class HoverableSimplePanel extends SimplePanel {

    /**
     * Constructs the ItemPanel.
     */
    public HoverableSimplePanel() {
      sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

    /**
     * {@inheritDoc}
     */
    public void onBrowserEvent(Event event) {
      super.onBrowserEvent(event);
      switch (DOM.eventGetType(event)) {
      case Event.ONMOUSEOVER:
        addStyleName(UI_STATE_HOVER);
        break;
      case Event.ONMOUSEOUT:
        removeStyleName(UI_STATE_HOVER);
        break;
      }
    }
  }

  /**
   * The css style.
   */
  @UiField
  protected MyStyle style;

  /**
   * The panel to control self visible(for only the tree root).
   */
  @UiField
  protected HorizontalPanel captionPanel;// for tree root

  /**
   * The panel to control children visible.
   */
  @UiField
  protected FlowPanel childrenPanel;

  /**
   * The icon label to show the toggle icon.
   */
  @UiField
  protected Label toggleIconLabel;

  /**
   * The panel to show selected state.
   */
  @UiField
  protected HoverableSimplePanel itemPanel;

  /**
   * The check box of the item.
   */
  @UiField
  protected CheckBox checkBox;

  /**
   * The icon label to show item's icon.
   */
  @UiField
  protected Label iconLabel;

  /**
   * The label to show item's name.
   */
  @UiField
  protected Label textLabel;

  /**
   * The parent tree item.
   */
  protected WjrTreeItem parent;

  /**
   * The children tree items.
   */
  protected List<WjrTreeItem> children = new ArrayList<WjrTreeItem>();

  /**
   * Whether if this tree item is open or not.
   */
  protected boolean open = false;

  /**
   * Whether if this tree item is selected or not.
   */
  protected boolean selected = false;

  /**
   * Constructs the WjrTreeItem.
   */
  public WjrTreeItem() {
    initWidget(uiBinder.createAndBindUi(this));
    checkBox.setTabIndex(-1);
  }

  /**
   * Gets whether if this tree item is open or not.
   * 
   * @return True if open, false if not.
   */
  public boolean getState() {
    return open;
  }

  /**
   * Sets whether if this tree item is open or not.
   * 
   * @param open
   *          True if open, false if not.
   */
  public void setState(boolean open) {
    this.open = open;
    updateState();
  }

  /**
   * Updates the state.
   * 
   * If this item has no children, the toggle icon is not shown. If this item
   * has one or more children, the icon that represents the open state is shown.
   */
  protected void updateState() {
    if (getChildCount() == 0) {
      open = false;
      toggleIconLabel.setStyleName(join(UI_ICON, UI_ICON_EMPTY));
      childrenPanel.setVisible(false);
      return;
    }

    // Change the status image
    if (open) {
      childrenPanel.setVisible(true);
      toggleIconLabel.setStyleName(join(UI_ICON, UI_ICON_TRIANGLE_1_S));
    } else {
      childrenPanel.setVisible(false);
      toggleIconLabel.setStyleName(join(UI_ICON, UI_ICON_TRIANGLE_1_E));
    }
  }

  /**
   * Gets the parent item.
   * 
   * @return The parent item.
   */
  public WjrTreeItem getParentItem() {
    return parent;
  }

  /**
   * Sets the parent item.
   * 
   * @param ktrWjrTreeItem
   *          The parent item.
   */
  public void setParentItem(WjrTreeItem ktrWjrTreeItem) {
    parent = ktrWjrTreeItem;
  }

  /**
   * Gets the check box value.
   * 
   * @return True if checked, false if not.
   */
  public boolean isChecked() {
    return checkBox.getValue();
  }

  /**
   * Sets the check box value.
   * 
   * @param checked
   *          True if checked, false if not.
   * @param fireEvent
   *          True if you want to fire {@link ValueChangeEvent}, false if not.
   */
  public void setChecked(boolean checked, boolean fireEvent) {
    checkBox.setValue(checked);
    if (fireEvent) {
      propagateCheckBoxValueToChildren(this, checked);
      propagateCheckBoxValueToParent(this);
      ValueChangeEvent.fire(this, this);
    }
  }

  /**
   * Gets whether if this tree item is selected or not.
   * 
   * @return True if selected, false if not.
   */
  public boolean isSelected() {
    return selected;
  }

  /**
   * Sets whether if this tree item is selected or not.
   * 
   * @param selected
   *          True if selected, false if not.
   * @param fireEvent
   *          True if you want to fire {@link SelectionEvent}, false if not.
   */
  public void setSelected(boolean selected, boolean fireEvent) {
    if (this.selected != selected) {
      this.selected = selected;
      if (fireEvent) {
        SelectionEvent.fire(this, this);
      }
    }
  }

  /**
   * Sets the css style when this item is selected.
   * 
   * The selected style is decided by logic class, because the style is
   * dependent on the model state.
   * 
   * @param selectedStyle
   *          The selected css class name. If the value is null, the empty
   *          string value is used instead.
   */
  public void setSelectedStyle(String selectedStyle) {
    selectedStyle = (selectedStyle != null ? selectedStyle : "");
    itemPanel.setStyleName(join(style.itemPanel(), selectedStyle));
  }

  /**
   * Sets the item text.
   * 
   * @param text
   *          The item text. If the value is null, the empty string value is
   *          used instead.
   */
  public void setText(String text) {
    text = (text != null ? text : "");
    textLabel.setText(text);
    itemPanel.setTitle(text);
  }

  /**
   * Sets the icon css style of JQueryUI.
   * 
   * @param icon
   *          The icon css class of JQueryUI. The value is null, the empty icon
   *          is used instead.
   */
  public void setIcon(String icon) {
    icon = (icon != null ? icon : UI_ICON_EMPTY);
    iconLabel.setStyleName(join(UI_ICON, icon));
  }

  /**
   * Adds the child item.
   * 
   * @param item
   *          The child item.
   */
  public void addItem(WjrTreeItem item) {
    if (item.getParentItem() != null) {
      item.getParentItem().removeItem(this);
    }

    item.setParentItem(this);
    children.add(item);
    childrenPanel.add(item);

    if (children.size() == 1) {
      updateState();
    }
  }

  /**
   * Gets the child item by the index.
   * 
   * @param index
   *          The index of child you want to get.
   * @return Null if not found, the child item if found.
   */
  public WjrTreeItem getChild(int index) {
    if ((index < 0) || (index >= getChildCount())) {
      return null;
    }
    return children.get(index);
  }

  /**
   * Gets whether this item has a child or not.
   * 
   * @return True if the item has a child, false if not.
   */
  public boolean hasChild() {
    return (children.size() > 0);
  }

  /**
   * Gets the children count.
   * 
   * @return The children count.
   */
  public int getChildCount() {
    return children.size();
  }

  /**
   * Gets the child index.
   * 
   * @param child
   *          The child you want to get the index of.
   * @return the index of the first occurrence of the specified element in this
   *         list, or -1 if this list does not contain the element
   */
  public int getChildIndex(WjrTreeItem child) {
    return children.indexOf(child);
  }

  /**
   * Removes the child item.
   * 
   * If the item is not a child of this item, do nothing.
   * 
   * @param item
   *          The child item you want to remove from this item.
   */
  public void removeItem(WjrTreeItem item) {
    if (!children.contains(item)) {
      return;
    }

    childrenPanel.remove(item);

    item.setParentItem(null);
    children.remove(item);

    if (children.size() == 0) {
      updateState();
    }
  }

  /**
   * Removes all children items.
   */
  public void removeItems() {
    while (getChildCount() > 0) {
      removeItem(getChild(0));
    }
  }

  /**
   * Handles check box value change event.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("checkBox")
  protected void handleCheckBoxValueChange(ValueChangeEvent<Boolean> event) {
    boolean value = event.getValue();
    propagateCheckBoxValueToChildren(this, value);
    propagateCheckBoxValueToParent(this);
    ValueChangeEvent.fire(this, this);
  }

  /**
   * Propagates the check box value to children recursively.
   * 
   * When the parent item is checked, then all its children will be checked.
   * When the parent item is unchecked, then all its children will be unchecked.
   * 
   * @param ktrWjrTreeItem
   *          The parent item.
   * @param value
   *          True if checked, false if unchecked.
   */
  protected void propagateCheckBoxValueToChildren(WjrTreeItem ktrWjrTreeItem,
      boolean value) {
    for (int i = 0; i < ktrWjrTreeItem.getChildCount(); i++) {
      WjrTreeItem child = ktrWjrTreeItem.getChild(i);
      child.setChecked(value, false);
      propagateCheckBoxValueToChildren(child, value);
    }
  }

  /**
   * Propagates the check box value to parent recursively.
   * 
   * When the child item is checked and all other children of the parent are
   * checked, the parent will be checked. When the child item is unchecked and
   * the parent is checked, the parent will be unchecked.
   * 
   * @param ktrWjrTreeItem
   *          The child item.
   */
  protected void propagateCheckBoxValueToParent(WjrTreeItem ktrWjrTreeItem) {
    WjrTreeItem parent = ktrWjrTreeItem.getParentItem();
    if (parent != null) {
      boolean parentCheck = true;
      for (int i = 0; i < parent.getChildCount(); i++) {
        WjrTreeItem sibling = parent.getChild(i);
        if (!sibling.isChecked()) {
          parentCheck = false;
          break;
        }
      }
      parent.setChecked(parentCheck, false);
      propagateCheckBoxValueToParent(parent);
    }
  }

  /**
   * {@inheritDoc}
   */
  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<WjrTreeItem> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  /**
   * {@inheritDoc}
   */
  public HandlerRegistration addSelectionHandler(
      SelectionHandler<WjrTreeItem> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

}
