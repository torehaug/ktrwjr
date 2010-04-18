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

public class WjrButtonPanel extends Composite {

  public static interface Handler {
    public void onButtonClicked(ClickEvent event);
  }

  private static WjrButtonPanelUiBinder uiBinder =
    GWT.create(WjrButtonPanelUiBinder.class);

  interface WjrButtonPanelUiBinder extends UiBinder<Widget, WjrButtonPanel> {
  }

  protected Handler handler;

  @UiField
  protected WjrIconTextButton button;

  public WjrButtonPanel(Handler handler) {
    this.handler = checkNotNull(handler, "The handler parameter is null.");
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setButtonDisabled(boolean disabled) {
    button.setDisabled(disabled);
  }

  @UiHandler("button")
  public void onRunButtonClicked(ClickEvent event) {
    handler.onButtonClicked(event);
  }

  public void changeToCancelButton() {
    button.setIcon(UI_ICON_PAUSE);
    button.setText("Cancel");
    button.setTitle("Cancel Running");
  }

  public void changeToRunButton() {
    button.setIcon(UI_ICON_PLAY);
    button.setText("Run");
    button.setTitle("Run Checked Tests");
  }
}
