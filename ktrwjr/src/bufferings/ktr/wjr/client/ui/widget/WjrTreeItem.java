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

public class WjrTreeItem extends Composite implements
    HasValueChangeHandlers<WjrTreeItem>, HasSelectionHandlers<WjrTreeItem> {

  private static WjrTreeItemUiBinder uiBinder =
    GWT.create(WjrTreeItemUiBinder.class);

  interface WjrTreeItemUiBinder extends UiBinder<Widget, WjrTreeItem> {
  }

  protected interface MyStyle extends CssResource {
    String itemPanel();
  }

  protected static class ItemPanel extends SimplePanel {

    public ItemPanel() {
      sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

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

  @UiField
  protected MyStyle style;

  @UiField
  protected HorizontalPanel captionPanel;// for tree root

  @UiField
  protected FlowPanel childrenPanel;

  @UiField
  protected Label toggleIconLabel;

  @UiField
  protected ItemPanel itemPanel;

  protected boolean open = false;

  @UiField
  protected CheckBox checkBox;

  @UiField
  protected Label iconLabel;

  @UiField
  protected Label textLabel;

  protected WjrTreeItem parent;

  protected List<WjrTreeItem> children = new ArrayList<WjrTreeItem>();

  protected boolean selected = false;

  public WjrTreeItem() {
    initWidget(uiBinder.createAndBindUi(this));
    checkBox.setTabIndex(-1);
  }

  public boolean getState() {
    return open;
  }

  public void setState(boolean open) {
    this.open = open;
    updateState();
  }

  protected void updateState() {
    if (getChildCount() == 0) {
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

  public WjrTreeItem getParentItem() {
    return parent;
  }

  public void setParentItem(WjrTreeItem ktrWjrTreeItem) {
    parent = ktrWjrTreeItem;
  }

  public boolean isChecked() {
    return checkBox.getValue();
  }

  public void setChecked(boolean checked, boolean fireEvent) {
    checkBox.setValue(checked);
    if (fireEvent) {
      ValueChangeEvent.fire(this, this);
    }
  }

  public boolean isSelected() {
    return selected;
  }

  public void setSelected(boolean selected, boolean fireEvent) {
    if (this.selected != selected) {
      this.selected = selected;
      if (fireEvent) {
        SelectionEvent.fire(this, this);
      }
    }
  }

  public void setSelectedStyle(String selectedStyle) {
    itemPanel.setStyleName(join(style.itemPanel(), selectedStyle));
  }

  public void setText(String text) {
    textLabel.setText(text);
    itemPanel.setTitle(text);
  }

  public void setIcon(String icon) {
    iconLabel.setStyleName(join(UI_ICON, icon));
  }

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

  public WjrTreeItem getChild(int index) {
    if ((index < 0) || (index >= getChildCount())) {
      return null;
    }
    return children.get(index);
  }

  public int getChildCount() {
    return children.size();
  }

  public int getChildIndex(WjrTreeItem child) {
    return children.indexOf(child);
  }

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

  public void removeItems() {
    while (getChildCount() > 0) {
      removeItem(getChild(0));
    }
  }

  @UiHandler("checkBox")
  protected void handleCheckBoxValueChange(ValueChangeEvent<Boolean> event) {
    boolean value = event.getValue();
    propagateCheckBoxValueToChildren(this, value);
    propagateCheckBoxValueToParent(this);
    ValueChangeEvent.fire(this, this);
  }

  protected void propagateCheckBoxValueToChildren(WjrTreeItem ktrWjrTreeItem,
      boolean value) {
    for (int i = 0; i < ktrWjrTreeItem.getChildCount(); i++) {
      WjrTreeItem child = ktrWjrTreeItem.getChild(i);
      child.setChecked(value, false);
      propagateCheckBoxValueToChildren(child, value);
    }
  }

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

  @Override
  public HandlerRegistration addValueChangeHandler(
      ValueChangeHandler<WjrTreeItem> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectionHandler(
      SelectionHandler<WjrTreeItem> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

}
