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
package bufferings.ktr.wjr.client;

import bufferings.ktr.wjr.client.service.KtrWjrService;
import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * The entry point class of Kotori Web JUnit Runner.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjr implements EntryPoint {

  private static final String LOADING_ELEMENT_ID = "loading";

  /**
   * {@inheritDoc}
   */
  public void onModuleLoad() {
    KtrWjrServiceAsync rpcService = GWT.create(KtrWjrService.class);
    WjrPresenter presenter = new WjrPresenter(rpcService, new WjrView());
    presenter.go(RootLayoutPanel.get(), RootPanel
      .get(LOADING_ELEMENT_ID)
      .getElement());
  }

}
