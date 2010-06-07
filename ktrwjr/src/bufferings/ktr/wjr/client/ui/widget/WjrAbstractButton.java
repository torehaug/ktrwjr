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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

/**
 * The button base with JQueryUI theme
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class WjrAbstractButton extends Composite implements
    HasClickHandlers {

  /**
   * Check if the key code is assigned or not.
   * 
   * @param code
   *          The key code.
   * @return True if the key is assigned, false if not.
   */
  protected static boolean isKeyAssigned(int code) {
    switch (code) {
    case KeyCodes.KEY_ENTER:
      return true;
    default:
      return false;
    }
  }

  /**
   * Whether this button is disabled or not.
   */
  protected boolean disabled = false;

  /**
   * For controling key events.
   */
  protected boolean lastWasKeyDown;

  /**
   * Constructs the WjrAbstractButton.
   */
  public WjrAbstractButton() {
    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT | Event.KEYEVENTS);
  }

  /**
   * {@inheritDoc}
   */
  public void onBrowserEvent(Event event) {
    int eventType = DOM.eventGetType(event);

    switch (eventType) {
    case Event.ONMOUSEOVER:
      addStyleName(UI_STATE_HOVER);
      break;
    case Event.ONMOUSEOUT:
      removeStyleName(UI_STATE_HOVER);
      break;
    case Event.ONCLICK:
      if (disabled) {
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
      if (isKeyAssigned(DOM.eventGetKeyCode(event))) {
        DOM.eventCancelBubble(event, true);
        DOM.eventPreventDefault(event);
        return;
      }
    }
    }

    super.onBrowserEvent(event);
  }

  /**
   * Sets whether this is disabled or not.
   * 
   * @param disabled
   *          Whether this is disabled or not.
   */
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    setStyleName(this.getElement(), UI_STATE_DISABLED, disabled);
  }

  /**
   * Gets whether this is disabled or not.
   * 
   * @return Whether this is disabled or not.
   */
  public boolean isDisabled() {
    return disabled;
  }

  /**
   * {@inheritDoc}
   */
  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

  /**
   * Sets the id of this element.
   * 
   * @param id
   *          id to set
   */
  public void setElemId(String id) {
    getElement().setId(id);
  }

  private void keyboardNavigation(Event event) {
    if (disabled) {
      return;
    }

    int code = DOM.eventGetKeyCode(event);
    switch (code) {
    case KeyCodes.KEY_ENTER: {
      NativeEvent nativeEvent =
        Document.get().createClickEvent(
          0,
          0,
          0,
          0,
          0,
          false,
          false,
          false,
          false);
      ClickEvent.fireNativeEvent(nativeEvent, this);
      break;
    }
    }
  }

}
