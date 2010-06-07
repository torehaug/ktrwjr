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
import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The simple list box with JQueryUI theme.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrListBox extends Composite {

  protected static final int OTHER_KEY_DOWN = 63233;
  protected static final int OTHER_KEY_UP = 63232;

  /**
   * The resource client bundle for the WjrListBox.
   * 
   * @author bufferings[at]gmail.com
   */
  protected interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);

    /**
     * The css resource.
     * 
     * @return The css resource.
     */
    @Source("WjrListBox.css")
    Css css();
  }

  /**
   * CssResource used in the WjrListBox.
   * 
   * @author bufferings[at]gmail.com
   */
  protected interface Css extends CssResource {
    String itemStyle();
  }

  /**
   * Hoverable and clickable label.
   * 
   * @author bufferings[at]gmail.com
   */
  protected static class HoverableAndClickableLabel extends Label implements
      HasClickHandlers {

    /**
     * Constructs the HoverableAndClickableLabel.
     */
    public HoverableAndClickableLabel() {
      sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

    /**
     * {@inheritDoc}
     */
    public void onBrowserEvent(Event event) {
      super.onBrowserEvent(event);
      switch (DOM.eventGetType(event)) {
      case Event.ONMOUSEOVER:
        if (getTitle().length() > 0) {// Title is never null, it's only empty
          // when not set.
          addStyleName(UI_STATE_HOVER);
        }
        break;
      case Event.ONMOUSEOUT:
        removeStyleName(UI_STATE_HOVER);
        break;
      }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
      return addDomHandler(handler, ClickEvent.getType());
    }
  }

  /**
   * The main panel.
   */
  protected FlowPanel mainPanel;

  /**
   * The row items.
   */
  protected List<Label> children = new ArrayList<Label>();

  /**
   * The current selected row item.
   */
  protected Widget curSelection;

  /**
   * For controling key events.
   */
  protected boolean lastWasKeyDown;

  /**
   * The panel to get focus.
   */
  protected FocusPanel focusPanel;

  /**
   * Constructs the WjrListBox.
   */
  public WjrListBox() {
    Resources.INSTANCE.css().ensureInjected();
    mainPanel = new FlowPanel();
    focusPanel = new WjrNoBorderFocusPanel(mainPanel);
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
   * Adds the item.
   * 
   * @param item
   *          The item to add.
   * @throws NullPointerException
   *           If the item parameter is null.
   */
  public void addItem(String item) {
    checkNotNull(item, "The item parameter is null.");
    
    Label label = new HoverableAndClickableLabel();
    label.setStyleName(Resources.INSTANCE.css().itemStyle());
    
    // Use trimedItem not to show prefix tab on the tooltip.
    String trimedItem = item.trim();
    label.setTitle(trimedItem);

    if(trimedItem.length() == 0){
      // Allows &nbsp; to show empty line.
      label.getElement().setInnerHTML("&nbsp;");
    }else{
      // HTML element is escaped.
      label.setText(item);
    }

    mainPanel.add(label);
  }

  /**
   * Clears the all items.
   */
  public void clear() {
    mainPanel.clear();
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
        if (mainPanel.getWidgetCount() > 0) {
          setSelection(mainPanel.getWidget(0));
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
      if (isArrowKey(DOM.eventGetKeyCode(event))) {
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
        return;
      }
    }
    }

    super.onBrowserEvent(event);
  }

  /**
   * Sets the selected item.
   * 
   * @param item
   *          The item to select.
   */
  protected void setSelection(Widget item) {
    if (curSelection != null) {
      curSelection.setStyleName(Resources.INSTANCE.css().itemStyle());
    }

    curSelection = item;
    if (curSelection != null) {
      curSelection.setStyleName(join(
        Resources.INSTANCE.css().itemStyle(),
        UI_STATE_HIGHLIGHT));

      DOMUtil.scrollTopIntoView(curSelection.getElement());
    }
  }

  private boolean elementClicked(Element hElem) {
    ArrayList<Element> chain = new ArrayList<Element>();
    collectElementChain(chain, getElement(), hElem);

    Widget item = findItemByChain(chain, 0);
    if (item != null) {
      setSelection(item);
      return true;
    }

    return false;
  }

  private void collectElementChain(ArrayList<Element> chain, Element hRoot,
      Element hElem) {
    if ((hElem == null) || (hElem == hRoot)) {
      return;
    }

    collectElementChain(chain, hRoot, DOM.getParent(hElem));
    chain.add(hElem);
  }

  private Widget findItemByChain(ArrayList<Element> chain, int idx) {
    if (idx == chain.size()) {
      return null;
    }

    Element hCurElem = chain.get(idx);
    for (int i = 0, n = mainPanel.getWidgetCount(); i < n; ++i) {
      Widget child = mainPanel.getWidget(i);
      if (child.getElement() == hCurElem) {
        return child;
      }
    }

    return findItemByChain(chain, idx + 1);
  }

  private void keyboardNavigation(Event event) {
    int code = DOM.eventGetKeyCode(event);

    switch (standardizeKeycode(code)) {
    case KeyCodes.KEY_UP:
      moveSelectionUp(curSelection);
      break;
    case KeyCodes.KEY_DOWN:
      moveSelectionDown(curSelection);
      break;
    default:
      return;
    }
  }

  private void moveSelectionDown(Widget sel) {
    int idx = mainPanel.getWidgetIndex(sel);
    if (idx < mainPanel.getWidgetCount() - 1) {
      setSelection(mainPanel.getWidget(idx + 1));
    }
  }

  private void moveSelectionUp(Widget sel) {
    int idx = mainPanel.getWidgetIndex(sel);
    if (idx > 0) {
      setSelection(mainPanel.getWidget(idx - 1));
    }
  }

  private boolean isArrowKey(int code) {
    switch (code) {
    case OTHER_KEY_DOWN:
    case OTHER_KEY_UP:
    case KeyCodes.KEY_DOWN:
    case KeyCodes.KEY_UP:
      return true;
    default:
      return false;
    }
  }

  private int standardizeKeycode(int code) {
    switch (code) {
    case OTHER_KEY_DOWN:
      code = KeyCodes.KEY_DOWN;
      break;
    case OTHER_KEY_UP:
      code = KeyCodes.KEY_UP;
      break;
    }
    return code;
  }
}
