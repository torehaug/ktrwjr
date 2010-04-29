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

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
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
   * Whether this button is disabled or not.
   */
  protected boolean disabled = false;

  /**
   * Constructs the WjrAbstractButton.
   */
  public WjrAbstractButton() {
    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
  }

  /**
   * {@inheritDoc}
   */
  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
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

}
