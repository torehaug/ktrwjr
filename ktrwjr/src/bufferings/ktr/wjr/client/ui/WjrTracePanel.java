package bufferings.ktr.wjr.client.ui;

import bufferings.ktr.wjr.client.ui.widget.WjrListBox;
import bufferings.ktr.wjr.client.ui.widget.WjrTabPanel;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;

public class WjrTracePanel extends Composite {

  private static WjrTracePanelUiBinder uiBinder =
    GWT.create(WjrTracePanelUiBinder.class);

  interface WjrTracePanelUiBinder extends UiBinder<Widget, WjrTracePanel> {
  }

  @UiField
  protected WjrTabPanel tabPanel;

  protected WjrListBox traceList;

  protected WjrListBox logList;

  public WjrTracePanel() {
    initWidget(uiBinder.createAndBindUi(this));

    traceList = new WjrListBox();
    traceList.setStyleName("");
    ScrollPanel traceSp = new ScrollPanel(traceList);

    logList = new WjrListBox();
    logList.setStyleName("");
    ScrollPanel logSp = new ScrollPanel(logList);

    tabPanel.add(traceSp, "Trace");
    tabPanel.add(logSp, "Log");
  }

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
