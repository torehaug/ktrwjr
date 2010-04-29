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
package bufferings.ktr.wjr.client.ui;

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;
import static bufferings.ktr.wjr.shared.util.Preconditions.*;
import bufferings.ktr.wjr.client.ui.widget.WjrIconTextButton;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * The button panel of the run and the cancel button.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrButtonPanel extends Composite {

  /**
   * The event handler of the WjrButtonPanel.
   * 
   * @author bufferings[at]gmail.com
   */
  public static interface Handler {
    /**
     * Called when the button is clicked.
     * 
     * @param event
     *          The event information.
     */
    public void onButtonClicked(ClickEvent event);
  }

  private static WjrButtonPanelUiBinder uiBinder =
    GWT.create(WjrButtonPanelUiBinder.class);

  interface WjrButtonPanelUiBinder extends UiBinder<Widget, WjrButtonPanel> {
  }

  /**
   * The event handler of the WjrButtonPanel.
   */
  protected Handler handler;

  /**
   * The button, which acts as "Run" button and as "Cancel" button.
   */
  @UiField
  protected WjrIconTextButton button;

  /**
   * Constructs the WjrButtonPanel with the event handler.
   * 
   * @param handler
   *          The event handler of this class.
   * @throws NullPointerException
   *           If the handler parameter is null.
   */
  public WjrButtonPanel(Handler handler) {
    this.handler = checkNotNull(handler, "The handler parameter is null.");
    initWidget(uiBinder.createAndBindUi(this));
  }

  /**
   * Sets the button disabled.
   * 
   * @param disabled
   *          True if disabled, false if not.
   */
  public void setButtonDisabled(boolean disabled) {
    button.setDisabled(disabled);
  }

  /**
   * Called when the run button is clicked.
   * 
   * @param event
   *          The event information.
   */
  @UiHandler("button")
  public void onRunButtonClicked(ClickEvent event) {
    handler.onButtonClicked(event);
  }

  /**
   * Changes the button to the cancel button.
   */
  public void changeToCancelButton() {
    button.setIcon(UI_ICON_PAUSE);
    button.setText("Cancel");
    button.setTitle("Cancel Running");
  }

  /**
   * Changes the button to the run button.
   */
  public void changeToRunButton() {
    button.setIcon(UI_ICON_PLAY);
    button.setText("Run");
    button.setTitle("Run Checked Tests");
  }
}
