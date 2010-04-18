package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class WjrIconTextLabel extends Composite implements HasText {

  private static WjrIconLabelUiBinder uiBinder =
    GWT.create(WjrIconLabelUiBinder.class);

  interface WjrIconLabelUiBinder extends UiBinder<Widget, WjrIconTextLabel> {
  }

  @UiField
  protected Label iconLabel;

  @UiField
  protected Label textLabel;

  public WjrIconTextLabel() {
    initWidget(uiBinder.createAndBindUi(this));
  }

  public void setIcon(String styleName) {
    iconLabel.setStyleName(join(UI_ICON, styleName));
  }

  @Override
  public String getText() {
    return textLabel.getText();
  }

  @Override
  public void setText(String text) {
    textLabel.setText(text);
  }

}
