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

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WjrResultPanel extends Composite {

  private static WjrResultPanelUiBinder uiBinder =
    GWT.create(WjrResultPanelUiBinder.class);

  interface WjrResultPanelUiBinder extends UiBinder<Widget, WjrResultPanel> {
  }

  interface ResultBarStyle extends CssResource {
    String notyet();

    String fail();

    String succeed();
  }

  @UiField
  ResultBarStyle resultBarStyle;

  @UiField
  HasText runsLabel;

  @UiField
  HasText errorsLabel;

  @UiField
  HasText failuresLabel;

  @UiField
  Label resultBar;

  public WjrResultPanel() {
    initWidget(uiBinder.createAndBindUi(this));
    updateResults(0,0,0,0);
  }

  public void updateResults(int runsCount, int totalCount, int errorsCount,
      int failuresCount) {
    runsLabel.setText(getRunsLabelString(runsCount, totalCount));
    errorsLabel.setText(getErrorsLabelString(errorsCount));
    failuresLabel.setText(getFailuresLabelString(failuresCount));

    if (errorsCount > 0 || failuresCount > 0) {
      resultBar.setStyleName(resultBarStyle.fail());
    } else if (runsCount > 0) {
      resultBar.setStyleName(resultBarStyle.succeed());
    } else {
      resultBar.setStyleName(resultBarStyle.notyet());
    }
  }

  protected String getRunsLabelString(int runsCount, int totalCount) {
    return new StringBuilder()
      .append("Runs: ")
      .append(runsCount)
      .append("/")
      .append(totalCount)
      .toString();
  }

  protected String getErrorsLabelString(int errorsCount) {
    return new StringBuilder()
      .append("Errors: ")
      .append(errorsCount)
      .toString();
  }

  protected String getFailuresLabelString(int failuresCount) {
    return new StringBuilder()
      .append("Failures: ")
      .append(failuresCount)
      .toString();
  }
}
