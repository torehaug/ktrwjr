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

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The focus panel with no border.
 * 
 * This class is used by WjrTree and WjrListBox. If you want to use key to
 * operate the selection, the focus is needed. So I used the focusable panel in
 * the back end of them. The focusable panel has borders, but I want not to show
 * borders, so I prepared this class.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrNoBorderFocusPanel extends FocusPanel {

  /**
   * Constructs the WjrNoBorderFocusPanel.
   */
  public WjrNoBorderFocusPanel() {
    super();
    initialize();
  }

  /**
   * Constructs the WjrNoBorderFocusPanel with child.
   * 
   * @parma child The child to add.
   */
  public WjrNoBorderFocusPanel(Widget child) {
    super(child);
    initialize();
  }

  /**
   * Hides the focus border.
   */
  protected void initialize() {
    DOM.setStyleAttribute(getElement(), "outline", "0px");
    DOM.setElementAttribute(getElement(), "hideFocus", "true");
  }
}
