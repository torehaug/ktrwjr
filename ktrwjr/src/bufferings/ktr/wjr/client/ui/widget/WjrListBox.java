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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WjrListBox extends Composite {

  protected static final int OTHER_KEY_DOWN = 63233;
  protected static final int OTHER_KEY_UP = 63232;

  protected interface Resources extends ClientBundle {
    Resources INSTANCE = GWT.create(Resources.class);

    @Source("WjrListBox.css")
    Css css();
  }

  protected interface Css extends CssResource {
    String itemStyle();
  }

  protected static class ItemLabel extends Label implements HasClickHandlers {

    public ItemLabel() {
      sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
    }

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

    @Override
    public HandlerRegistration addClickHandler(ClickHandler handler) {
      return addDomHandler(handler, ClickEvent.getType());
    }
  }

  protected FlowPanel mainPanel;

  protected List<Label> children = new ArrayList<Label>();

  protected Widget curSelection;

  protected boolean lastWasKeyDown;

  public WjrListBox() {
    Resources.INSTANCE.css().ensureInjected();

    mainPanel = new FlowPanel();
    initWidget(new WjrNoBorderFocusPanel(mainPanel));
    sinkEvents(Event.ONMOUSEDOWN | Event.ONCLICK | Event.KEYEVENTS);
  }

  public void addItem(String item) {
    checkNotNull(item, "The item parameter is null.");

    Label label = new ItemLabel();
    label.setText(item);
    label.setStyleName(Resources.INSTANCE.css().itemStyle());
    label.setTitle(item.trim());

    mainPanel.add(label);
  }

  public void clear() {
    mainPanel.clear();
  }

  @Override
  public void onBrowserEvent(Event event) {
    int eventType = DOM.eventGetType(event);

    switch (eventType) {
    case Event.ONKEYDOWN:
      if (curSelection == null) {
        if (mainPanel.getWidgetCount() > 0) {
          onSelection(mainPanel.getWidget(0));
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

  protected void onSelection(Widget item) {
    if (curSelection != null) {
      curSelection.setStyleName(Resources.INSTANCE.css().itemStyle());
    }

    curSelection = item;
    if (curSelection != null) {
      curSelection.setStyleName(join(
        Resources.INSTANCE.css().itemStyle(),
        UI_STATE_HIGHLIGHT));
    }
  }

  protected boolean elementClicked(Element hElem) {
    ArrayList<Element> chain = new ArrayList<Element>();
    collectElementChain(chain, getElement(), hElem);

    Widget item = findItemByChain(chain, 0);
    if (item != null) {
      onSelection(item);
      return true;
    }

    return false;
  }

  protected void collectElementChain(ArrayList<Element> chain, Element hRoot,
      Element hElem) {
    if ((hElem == null) || (hElem == hRoot)) {
      return;
    }

    collectElementChain(chain, hRoot, DOM.getParent(hElem));
    chain.add(hElem);
  }

  protected Widget findItemByChain(ArrayList<Element> chain, int idx) {
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

  protected void keyboardNavigation(Event event) {
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
      onSelection(mainPanel.getWidget(idx + 1));
    }
  }

  private void moveSelectionUp(Widget sel) {
    int idx = mainPanel.getWidgetIndex(sel);
    if (idx > 0) {
      onSelection(mainPanel.getWidget(idx - 1));
    }
  }

  protected boolean isArrowKey(int code) {
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

  protected int standardizeKeycode(int code) {
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
