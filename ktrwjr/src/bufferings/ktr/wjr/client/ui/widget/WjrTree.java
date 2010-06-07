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

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Tree;

/**
 * The tree composite with JQueryUI theme.
 * 
 * @author bufferings[at]gmail.com
 * @see Tree
 */
public class WjrTree extends Composite {

  protected static final int OTHER_KEY_DOWN = 63233;
  protected static final int OTHER_KEY_LEFT = 63234;
  protected static final int OTHER_KEY_RIGHT = 63235;
  protected static final int OTHER_KEY_UP = 63232;
  protected static final int KEY_SPACE = 32;

  /**
   * The root tree item. This is used only inside this class.
   * 
   * The item self showing panel is invlisible, and children panel's margin is
   * 0.
   * 
   * @author bufferings[at]gmail.com
   */
  protected static class Root extends WjrTreeItem {

    public Root() {
      captionPanel.setVisible(false);
      childrenPanel.getElement().getStyle().setMargin(0, Unit.PX);
      setState(true);
    }

    /**
     * {@inheritDoc}
     */
    protected void updateState() {
      if (getChildCount() == 0) {
        toggleIconLabel.setStyleName(join(UI_ICON, UI_ICON_EMPTY));
        childrenPanel.setVisible(false);
        return;
      }

      childrenPanel.setVisible(true);
      toggleIconLabel.setStyleName(join(UI_ICON, UI_ICON_TRIANGLE_1_S));
    }
  }

  protected static boolean isAssignedKey(int code) {
    switch (code) {
    case OTHER_KEY_DOWN:
    case OTHER_KEY_RIGHT:
    case OTHER_KEY_UP:
    case OTHER_KEY_LEFT:
    case KeyCodes.KEY_DOWN:
    case KeyCodes.KEY_RIGHT:
    case KeyCodes.KEY_UP:
    case KeyCodes.KEY_LEFT:
    case KEY_SPACE:
      return true;
    default:
      return false;
    }
  }

  /**
   * Normalized key codes. Also switches KEY_RIGHT and KEY_LEFT in RTL
   * languages.
   */
  protected static int standardizeKeycode(int code) {
    switch (code) {
    case OTHER_KEY_DOWN:
      code = KeyCodes.KEY_DOWN;
      break;
    case OTHER_KEY_RIGHT:
      code = KeyCodes.KEY_RIGHT;
      break;
    case OTHER_KEY_UP:
      code = KeyCodes.KEY_UP;
      break;
    case OTHER_KEY_LEFT:
      code = KeyCodes.KEY_LEFT;
      break;
    }
    return code;
  }

  /**
   * The root item.
   */
  protected Root root;

  /**
   * Current selected item.
   */
  protected WjrTreeItem curSelection;

  /**
   * For controling key events.
   */
  protected boolean lastWasKeyDown;

  /**
   * The panel to get focus.
   */
  protected FocusPanel focusPanel;

  /**
   * Constructs the WjrTree.
   */
  public WjrTree() {
    root = new Root();
    focusPanel = new WjrNoBorderFocusPanel(root);
    initWidget(focusPanel);
    sinkEvents(Event.ONMOUSEDOWN | Event.ONCLICK | Event.KEYEVENTS);
  }

  /**
   * Sets the tab index.
   * 
   * @param index
   *          The tab index.
   */
  public void setTabIndex(int index) {
    focusPanel.setTabIndex(index);
  }

  /**
   * Adds the item to the root.
   * 
   * @param item
   *          The item to add.
   */
  public void addItem(WjrTreeItem item) {
    root.addItem(item);
  }

  /**
   * Removes the item from the root.
   * 
   * If the item is not a child of the root, do nothing.
   * 
   * @param item
   *          The item to remove.
   */
  public void removeItem(WjrTreeItem item) {
    root.removeItem(item);
  }

  /**
   * Removes all items from the root.
   */
  public void removeItems() {
    root.removeItems();
  }

  /**
   * Gets the child item by the index.
   * 
   * @param index
   *          The index of child you want to get.
   * @return Null if not found, the child item if found.
   */
  public WjrTreeItem getItem(int index) {
    return root.getChild(index);
  }

  /**
   * Gets the items count.
   * 
   * @return The items count.
   */
  public int getItemCount() {
    return root.getChildCount();
  }

  /**
   * Gets the selected item.
   * 
   * @return The item to select.
   */
  public WjrTreeItem getSelectedItem() {
    return curSelection;
  }

  /**
   * Sets the selected item and fire the selection event.
   * 
   * @param item
   *          The item to select.
   */
  public void setSelectedItem(WjrTreeItem item) {
    setSelectedItem(item, true);
  }

  /**
   * Sets the selected item.
   * 
   * @param item
   *          The item to select.
   * @param fireEvents
   *          True if you want to fire {@link SelectionEvent}, false if not.
   */
  public void setSelectedItem(WjrTreeItem item, boolean fireEvents) {
    if (item == root) {
      return;
    }

    if (curSelection != null) {
      curSelection.setSelected(false, fireEvents);
    }
    curSelection = item;

    if (curSelection != null) {
      curSelection.setSelected(true, fireEvents);

      DOMUtil.scrollTopIntoView(curSelection.getElement());
    }
  }

  /**
   * Checks all items.
   */
  public void checkAllItems() {
    for (int i = 0; i < getItemCount(); i++) {
      checkRecursive(getItem(i), true);
    }
  }

  /**
   * Unchecks all items.
   */
  public void uncheckAllItems() {
    for (int i = 0; i < getItemCount(); i++) {
      checkRecursive(getItem(i), false);
    }
  }

  /**
   * Expands all items.
   */
  public void expandAllItems() {
    for (int i = 0; i < getItemCount(); i++) {
      expandRecursive(getItem(i));
    }
  }

  /**
   * Collapses all items.
   */
  public void collapseAllItems() {
    for (int i = 0; i < getItemCount(); i++) {
      collapseRecursive(getItem(i));
    }
  }

  /**
   * Checks or unchecks the treeItem and its children recursively.
   * 
   * @param treeItem
   *          The treeItem.
   * @param value
   *          True if check, false if uncheck.
   */
  protected void checkRecursive(WjrTreeItem treeItem, boolean value) {
    treeItem.setChecked(value, false);
    for (int i = 0; i < treeItem.getChildCount(); i++) {
      checkRecursive(treeItem.getChild(i), value);
    }
  }

  /**
   * Expands the treeItem and its children recursively.
   * 
   * @param treeItem
   *          The treeItem.
   */
  protected void expandRecursive(WjrTreeItem treeItem) {
    if (treeItem.hasChild()) {
      treeItem.setState(true);
      for (int i = 0; i < treeItem.getChildCount(); i++) {
        expandRecursive(treeItem.getChild(i));
      }
    }
  }

  /**
   * Collapses the treeItem and its children recursively.
   * 
   * @param treeItem
   *          The treeItem.
   */
  protected void collapseRecursive(WjrTreeItem treeItem) {
    for (int i = 0; i < treeItem.getChildCount(); i++) {
      collapseRecursive(treeItem.getChild(i));
    }
    if (treeItem == curSelection) {
      WjrTreeItem parentItem = treeItem.getParentItem();
      if (parentItem != root) {
        setSelectedItem(parentItem, true);
      }
    }
    treeItem.setState(false);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void onBrowserEvent(Event event) {
    int eventType = DOM.eventGetType(event);

    switch (eventType) {
    case Event.ONKEYDOWN:
      if (curSelection == null) {
        if (getItemCount() > 0) {
          setSelectedItem(getItem(0), true);
        }
        super.onBrowserEvent(event);
        return;
      }
    }

    switch (eventType) {
    case Event.ONKEYDOWN:
    case Event.ONKEYPRESS:
    case Event.ONKEYUP:
      if (DOM.eventGetAltKey(event) || DOM.eventGetMetaKey(event)) {
        super.onBrowserEvent(event);
        return;
      }
    }

    switch (eventType) {
    case Event.ONMOUSEDOWN: {
      if ((DOM.eventGetCurrentTarget(event) == getElement())
        && (event.getButton() == Event.BUTTON_LEFT)) {
        elementClicked(DOM.eventGetTarget(event));
      }
      break;
    }
    case Event.ONKEYDOWN: {
      keyboardNavigation(event);
      lastWasKeyDown = true;
      break;
    }
    case Event.ONKEYPRESS: {
      if (!lastWasKeyDown) {
        keyboardNavigation(event);
      }
      lastWasKeyDown = false;
      break;
    }
    case Event.ONKEYUP: {
      lastWasKeyDown = false;
      break;
    }
    }

    switch (eventType) {
    case Event.ONKEYDOWN:
    case Event.ONKEYUP: {
      if (isAssignedKey(DOM.eventGetKeyCode(event))) {
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
        return;
      }
    }
    }

    super.onBrowserEvent(event);
  }

  private void collectElementChain(ArrayList<Element> chain, Element hRoot,
      Element hElem) {
    if ((hElem == null) || (hElem == hRoot)) {
      return;
    }

    collectElementChain(chain, hRoot, DOM.getParent(hElem));
    chain.add(hElem);
  }

  private WjrTreeItem findItemByChain(ArrayList<Element> chain, int idx,
      WjrTreeItem root) {
    if (idx == chain.size()) {
      return root;
    }

    Element hCurElem = chain.get(idx);
    for (int i = 0, n = root.getChildCount(); i < n; ++i) {
      WjrTreeItem child = root.getChild(i);
      if (child.getElement() == hCurElem) {
        WjrTreeItem retItem = findItemByChain(chain, idx + 1, root.getChild(i));
        if (retItem == null) {
          return child;
        }
        return retItem;
      }
    }

    return findItemByChain(chain, idx + 1, root);
  }

  private boolean elementClicked(Element hElem) {
    ArrayList<Element> chain = new ArrayList<Element>();
    collectElementChain(chain, getElement(), hElem);

    WjrTreeItem item = findItemByChain(chain, 0, root);
    if (item != null && item != root) {
      if (item.hasChild()
        && DOM.isOrHasChild(item.toggleIconLabel.getElement(), hElem)) {
        boolean newState = !item.getState();
        item.setState(newState);
        maybeUpdateSelection(item, newState);
        return true;
      } else if (DOM.isOrHasChild(item.getElement(), hElem)) {
        setSelectedItem(item, true);
        return true;
      }
    }

    return false;
  }

  private void keyboardNavigation(Event event) {
    int code = DOM.eventGetKeyCode(event);

    switch (standardizeKeycode(code)) {
    case KeyCodes.KEY_UP: {
      moveSelectionUp(curSelection);
      break;
    }
    case KeyCodes.KEY_DOWN: {
      moveSelectionDown(curSelection, true);
      break;
    }
    case KeyCodes.KEY_LEFT: {
      maybeCollapseTreeItem();
      break;
    }
    case KeyCodes.KEY_RIGHT: {
      maybeExpandTreeItem();
      break;
    }
    case KEY_SPACE: {
      curSelection.setChecked(!curSelection.isChecked(), true);
      break;
    }
    default: {
      return;
    }
    }
  }

  private void maybeCollapseTreeItem() {

    WjrTreeItem topClosedParent = getTopClosedParent(curSelection);
    if (topClosedParent != null) {
      // Select the first visible parent if curSelection is hidden
      setSelectedItem(topClosedParent);
    } else if (curSelection.getState()) {
      curSelection.setState(false);
    } else {
      WjrTreeItem parent = curSelection.getParentItem();
      if (parent != null) {
        setSelectedItem(parent);
      }
    }
  }

  private void maybeExpandTreeItem() {

    WjrTreeItem topClosedParent = getTopClosedParent(curSelection);
    if (topClosedParent != null) {
      // Select the first visible parent if curSelection is hidden
      setSelectedItem(topClosedParent);
    } else if (curSelection.hasChild()) {
      if (!curSelection.getState()) {
        curSelection.setState(true);
      } else {
        setSelectedItem(curSelection.getChild(0));
      }
    }
  }

  private void maybeUpdateSelection(WjrTreeItem itemThatChangedState,
      boolean isItemOpening) {
    if (!isItemOpening) {
      WjrTreeItem tempItem = curSelection;
      while (tempItem != null) {
        if (tempItem == itemThatChangedState) {
          setSelectedItem(itemThatChangedState);
          return;
        }
        tempItem = tempItem.getParentItem();
      }
    }
  }

  private void moveSelectionDown(WjrTreeItem sel, boolean dig) {
    if (sel == root) {
      return;
    }

    // Find a parent that is visible
    WjrTreeItem topClosedParent = getTopClosedParent(sel);
    if (topClosedParent != null) {
      moveSelectionDown(topClosedParent, false);
      return;
    }

    WjrTreeItem parent = sel.getParentItem();
    if (parent == null) {
      parent = root;
    }
    int idx = parent.getChildIndex(sel);

    if (!dig || !sel.getState()) {
      if (idx < parent.getChildCount() - 1) {
        setSelectedItem(parent.getChild(idx + 1), true);
      } else {
        moveSelectionDown(parent, false);
      }
    } else if (sel.getChildCount() > 0) {
      setSelectedItem(sel.getChild(0), true);
    }
  }

  private void moveSelectionUp(WjrTreeItem sel) {
    // Find a parent that is visible
    WjrTreeItem topClosedParent = getTopClosedParent(sel);
    if (topClosedParent != null) {
      setSelectedItem(topClosedParent, true);
      return;
    }

    WjrTreeItem parent = sel.getParentItem();
    int idx = parent.getChildIndex(sel);

    if (idx > 0) {
      WjrTreeItem sibling = parent.getChild(idx - 1);
      setSelectedItem(findDeepestOpenChild(sibling), true);
    } else {
      setSelectedItem(parent, true);
    }
  }

  private WjrTreeItem findDeepestOpenChild(WjrTreeItem item) {
    if (!item.getState()) {
      return item;
    }
    return findDeepestOpenChild(item.getChild(item.getChildCount() - 1));
  }

  private WjrTreeItem getTopClosedParent(WjrTreeItem item) {
    WjrTreeItem topClosedParent = null;
    WjrTreeItem parent = item.getParentItem();
    while (parent != null && parent != root) {
      if (!parent.getState()) {
        topClosedParent = parent;
      }
      parent = parent.getParentItem();
    }
    return topClosedParent;
  }

}
