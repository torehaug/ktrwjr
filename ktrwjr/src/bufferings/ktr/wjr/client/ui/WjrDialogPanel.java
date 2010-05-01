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
import bufferings.ktr.wjr.client.ui.widget.WjrListBox;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The modal dialog panel using JQueryUI.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrDialogPanel extends Composite {

  /**
   * The label.
   */
  protected WjrListBox listBox;

  /**
   * Constructs the WjrDialogPanel.
   * 
   * @param title
   *          The dialog title.
   */
  public WjrDialogPanel(String title) {
    listBox = new WjrListBox();
    listBox.setStyleName(UI_WIDGET);
    listBox.getElement().setId(DOM.createUniqueId());
    listBox.setVisible(false);
    listBox.setTitle(title);

    initWidget(listBox);
    RootPanel.get().add(this);
  }

  /**
   * Start showing the panel.
   * 
   * @param message
   *          The message of the dialog.
   */
  public void show(String message, Throwable cause) {
    listBox.clear();
    listBox.addItem(message);
    listBox.addItem("[The cause]");
    listBox.addItem(cause.toString());
    for (StackTraceElement row : cause.getStackTrace()) {
      listBox.addItem(row.toString());
    }
    showDialog(getElement().getId());
  }

  /**
   * Shows the dialog.
   * 
   * @param id
   *          The element id.
   */
  private native void showDialog(String id)/*-{
      $wnd.$("#" + id).dialog('open');
    }-*/;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void onLoad() {
    super.onLoad();
    initDialog(getElement().getId());
  }

  /**
   * Initializes the dialog.
   * 
   * @param id
   *          The element id.
   */
  private native void initDialog(String id)/*-{
      $wnd.$("#" + id).dialog({
        autoOpen: false,
        height: 300,
        width: 400,
        modal: true,
        show: 'fade',
        hide: 'fade',
        dialogClass: 'alert',
        buttons: { "Ok": function() { $wnd.$(this).dialog("close"); } }
      });
    }-*/;
}
