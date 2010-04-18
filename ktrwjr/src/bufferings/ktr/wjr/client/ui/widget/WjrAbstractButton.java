package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Composite;

public abstract class WjrAbstractButton extends Composite implements
    HasClickHandlers {

  protected boolean disabled = false;

  public WjrAbstractButton() {
    sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
  }

  public void onBrowserEvent(Event event) {
    switch (DOM.eventGetType(event)) {
    case Event.ONMOUSEOVER:
      addStyleName(UI_STATE_HOVER);
      break;
    case Event.ONMOUSEOUT:
      removeStyleName(UI_STATE_HOVER);
      break;
    case Event.ONCLICK:
      if (disabled) {
        return;
      }
    }
    super.onBrowserEvent(event);
  }

  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    setStyleName(this.getElement(), UI_STATE_DISABLED, disabled);
  }

  public boolean isDisabled() {
    return disabled;
  }

  public HandlerRegistration addClickHandler(ClickHandler handler) {
    return addDomHandler(handler, ClickEvent.getType());
  }

}
