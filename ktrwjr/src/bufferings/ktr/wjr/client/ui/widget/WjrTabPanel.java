package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.client.ui.widget.JQueryUI.*;
import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.ArrayList;
import java.util.Iterator;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.LayoutPanel;
import com.google.gwt.user.client.ui.ProvidesResize;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.WidgetCollection;

public class WjrTabPanel extends ResizeComposite implements ProvidesResize,
    IndexedPanel, HasBeforeSelectionHandlers<Integer>,
    HasSelectionHandlers<Integer> {

  private static WjrTabPanelUiBinder uiBinder =
    GWT.create(WjrTabPanelUiBinder.class);

  interface WjrTabPanelUiBinder extends UiBinder<Widget, WjrTabPanel> {
  }

  interface MyStyle extends CssResource {
    String tabSelected();

    String tabUnselected();
  }

  protected static final int PADDING = 3;

  protected static final double BAR_HEIGHT = 35;

  @UiField
  protected MyStyle style;

  @UiField
  protected LayoutPanel panel;

  @UiField
  protected HorizontalPanel tabBar;

  protected ArrayList<Tab> tabs;

  protected WidgetCollection children;

  protected int selectedIndex = -1;

  public WjrTabPanel() {
    initWidget(uiBinder.createAndBindUi(this));

    tabs = new ArrayList<Tab>();
    children = new WidgetCollection(panel);
  }

  public HandlerRegistration addBeforeSelectionHandler(
      BeforeSelectionHandler<Integer> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  public HandlerRegistration addSelectionHandler(
      SelectionHandler<Integer> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  public void add(Widget child, String text) {
    insert(child, text, getWidgetCount());
  }

  public void insert(final Widget child, String text, int beforeIndex) {
    checkArgument(
      (beforeIndex >= 0) && (beforeIndex <= getWidgetCount()),
      "beforeIndex out of bounds");

    int idx = getWidgetIndex(child);
    if (idx != -1) {
      remove(child);
      if (idx < beforeIndex) {
        beforeIndex--;
      }
    }

    final Tab tab = new Tab(text);
    children.insert(child, beforeIndex);
    tabs.add(beforeIndex, tab);

    tabBar.insert(tab, beforeIndex);
    tab.addClickHandler(new ClickHandler() {
      public void onClick(ClickEvent event) {
        selectTab(child);
      }
    });

    panel.insert(child, beforeIndex);
    layoutChild(child);

    if (selectedIndex == -1) {
      selectTab(0);
    }
  }

  public void clear() {
    Iterator<Widget> it = iterator();
    while (it.hasNext()) {
      it.next();
      it.remove();
    }
  }

  public int getSelectedIndex() {
    return selectedIndex;
  }

  public Widget getWidget(int index) {
    checkIndex(index);
    return children.get(index);
  }

  public int getWidgetCount() {
    return children.size();
  }

  public int getWidgetIndex(Widget child) {
    return children.indexOf(child);
  }

  public Iterator<Widget> iterator() {
    return children.iterator();
  }

  public boolean remove(int index) {
    if ((index < 0) || (index >= getWidgetCount())) {
      return false;
    }

    Widget child = children.get(index);
    tabBar.remove(index);
    panel.remove(child);

    children.remove(index);
    tabs.remove(index);

    if (index == selectedIndex) {
      selectedIndex = -1;
      if (getWidgetCount() > 0) {
        selectTab(0);
      }
    } else if (index < selectedIndex) {
      --selectedIndex;
    }
    return true;
  }

  public boolean remove(Widget w) {
    int index = children.indexOf(w);
    if (index == -1) {
      return false;
    }
    return remove(index);
  }

  public void selectTab(int index) {
    checkIndex(index);
    if (index == selectedIndex) {
      return;
    }

    BeforeSelectionEvent<Integer> event =
      BeforeSelectionEvent.fire(this, index);
    if ((event != null) && event.isCanceled()) {
      return;
    }

    if (selectedIndex != -1) {
      Widget child = children.get(selectedIndex);
      Element container = panel.getWidgetContainerElement(child);
      container.getStyle().setDisplay(Display.NONE);
      child.setVisible(false);
      tabs.get(selectedIndex).setSelected(false);
    }

    Widget child = children.get(index);
    Element container = panel.getWidgetContainerElement(child);
    container.getStyle().clearDisplay();
    child.setVisible(true);
    tabs.get(index).setSelected(true);
    selectedIndex = index;

    SelectionEvent.fire(this, index);
  }

  public void selectTab(Widget child) {
    selectTab(getWidgetIndex(child));
  }

  protected void checkIndex(int index) {
    checkArgument(
      (index >= 0) && (index < getWidgetCount()),
      "Index out of bounds");
  }

  protected void layoutChild(Widget child) {
    panel.setWidgetLeftRight(child, PADDING, Unit.PX, PADDING, Unit.PX);
    panel.setWidgetTopBottom(child, BAR_HEIGHT + PADDING, Unit.PX, 0, Unit.PX);
    panel.getWidgetContainerElement(child).getStyle().setDisplay(Display.NONE);
    child.setVisible(false);
  }

  protected class Tab extends Label {

    public Tab(String label) {
      super(label);
      setStyleName(join(UI_WIDGET, UI_STATE_DEFAULT, UI_CORNER_TOP));
      setSelected(false);
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
      }
      super.onBrowserEvent(event);
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
      return addDomHandler(handler, ClickEvent.getType());
    }

    public void setSelected(boolean selected) {
      if (selected) {
        addStyleName(UI_STATE_ACTIVE);
        addStyleName(style.tabSelected());
        removeStyleName(style.tabUnselected());
      } else {
        removeStyleName(UI_STATE_ACTIVE);
        removeStyleName(style.tabSelected());
        addStyleName(style.tabUnselected());
      }
    }
  }

}
