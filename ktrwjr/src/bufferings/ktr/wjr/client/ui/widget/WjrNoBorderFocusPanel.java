package bufferings.ktr.wjr.client.ui.widget;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;

public class WjrNoBorderFocusPanel extends FocusPanel {

  public WjrNoBorderFocusPanel() {
    super();
    initialize();
  }

  public WjrNoBorderFocusPanel(Widget child) {
    super(child);
    initialize();
  }

  protected void initialize() {
    DOM.setStyleAttribute(getElement(), "outline", "0px");
    DOM.setElementAttribute(getElement(), "hideFocus", "true");
  }
}
