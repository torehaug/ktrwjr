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

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

/**
 * The icon only button with JQueryUI theme
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrIconButton extends WjrAbstractButton {

  private static WjrIconButtonUiBinder uiBinder =
    GWT.create(WjrIconButtonUiBinder.class);

  interface WjrIconButtonUiBinder extends UiBinder<Widget, WjrIconButton> {
  }

  /**
   * The panel to get focus.
   */
  @UiField
  protected FocusPanel focusPanel;

  /**
   * The label to show the icon.
   */
  @UiField
  protected Label iconLabel;

  /**
   * Constructs the WjrIconButton.
   */
  public WjrIconButton() {
    super();
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Sets the icon style name.
   * 
   * If the styleName is null, the empty icon is used.
   * 
   * @param styleName
   *          The icon style name.
   */
  public void setIcon(String styleName) {
    styleName = (styleName != null ? styleName : UI_ICON_EMPTY);
    iconLabel.setStyleName(join(UI_ICON, styleName));
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
}
