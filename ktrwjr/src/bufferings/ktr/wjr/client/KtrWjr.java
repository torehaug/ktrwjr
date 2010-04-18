package bufferings.ktr.wjr.client;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

public class KtrWjr implements EntryPoint {

  private static final String LOADING_ELEMENT_ID = "loading";

  @Override
  public void onModuleLoad() {
    KtrWjrServiceAsync rpcService = GWT.create(KtrWjrService.class);
    WjrPresenter presenter = new WjrPresenter(rpcService, new WjrView());
    presenter.go(RootLayoutPanel.get(), RootPanel
      .get(LOADING_ELEMENT_ID)
      .getElement());
  }

}
