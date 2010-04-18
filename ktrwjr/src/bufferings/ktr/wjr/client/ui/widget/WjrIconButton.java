/*
 * Copyright 2009 bufferings[at]gmail.com
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
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WjrIconButton extends WjrAbstractButton {

  private static WjrIconButtonUiBinder uiBinder =
    GWT.create(WjrIconButtonUiBinder.class);

  interface WjrIconButtonUiBinder extends UiBinder<Widget, WjrIconButton> {
  }

  @UiField
  protected Label iconLabel;

  public WjrIconButton() {
    super();
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setIcon(String styleName) {
    iconLabel.setStyleName(join(UI_ICON, styleName));
  }
}
