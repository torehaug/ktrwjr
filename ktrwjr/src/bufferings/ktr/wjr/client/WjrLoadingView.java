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

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;

/**
 * The loading view of the Kotori Web JUnit Runner.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrLoadingView extends Composite implements WjrLoadingDisplay {

  private static WjrLoadingViewUiBinder uiBinder =
    GWT.create(WjrLoadingViewUiBinder.class);

  interface WjrLoadingViewUiBinder extends UiBinder<Widget, WjrLoadingView> {
  }

  /**
   * The container widget of this view.
   */
  protected HasWidgets container;

  /**
   * {@inheritDoc}
   */
  public void go(HasWidgets container) {
    checkNotNull(container, "The container parameter is null.");

    this.container = container;
    initWidget(uiBinder.createAndBindUi(this));
    this.container.add(this);
  }

  /**
   * {@inheritDoc}
   */
  public void notifyLoaded() {
    container.remove(this);
  }

}
