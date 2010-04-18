package bufferings.ktr.wjr.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class WjrTitlePanel extends Composite {

  private static WjrTitlePanelUiBinder uiBinder =
    GWT.create(WjrTitlePanelUiBinder.class);

  interface WjrTitlePanelUiBinder extends UiBinder<Widget, WjrTitlePanel> {
  }

  public WjrTitlePanel() {
    initWidget(uiBinder.createAndBindUi(this));
  }
}
