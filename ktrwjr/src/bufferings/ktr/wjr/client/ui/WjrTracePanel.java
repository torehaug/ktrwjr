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

import bufferings.ktr.wjr.client.ui.widget.WjrListBox;
import bufferings.ktr.wjr.client.ui.widget.WjrTabPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * The trace panel which shows the trace and the log.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrTracePanel extends Composite {

  private static WjrTracePanelUiBinder uiBinder =
    GWT.create(WjrTracePanelUiBinder.class);

  interface WjrTracePanelUiBinder extends UiBinder<Widget, WjrTracePanel> {
  }

  /**
   * The tab panel.
   */
  @UiField
  protected WjrTabPanel tabPanel;

  /**
   * The list to show the trace.
   */
  protected WjrListBox traceList;

  /**
   * The list to show the logs.
   */
  protected WjrListBox logList;

  /**
   * Constructs the WjrTracePanel.
   */
  public WjrTracePanel() {
    initWidget(uiBinder.createAndBindUi(this));

    traceList = new WjrListBox();
    traceList.setStyleName("");
    ScrollPanel traceSp = new ScrollPanel(traceList);

    logList = new WjrListBox();
    logList.setStyleName("");
    ScrollPanel logSp = new ScrollPanel(logList);

    tabPanel.add(traceSp, "Failure Trace");
    tabPanel.add(logSp, "Log");
  }

  /**
   * Sets the trace info.
   * 
   * @param trace
   *          The trace info.
   */
  public void setTrace(String trace) {
    traceList.clear();
    if (trace == null) {
      return;
    }

    String[] splits = trace.split("\n");
    for (String row : splits) {
      traceList.addItem(row);
    }
  }

  /**
   * Sets the log info.
   * 
   * @param log
   *          The log info.
   */
  public void setLog(String log) {
    logList.clear();
    if (log == null) {
      return;
    }

    String[] splits = log.split("\n");
    for (String row : splits) {
      logList.addItem(row);
    }
  }

}
