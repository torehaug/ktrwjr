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

import java.util.List;

import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.HasWidgets;

public interface WjrDisplay {

  public void setData(WjrStore store);

  public List<WjrMethodItem> getCheckedMethodItems();

  public void repaintAllTreeItems(WjrStore store);

  public void repaintTreeItemAncestors(WjrStore store, WjrMethodItem methodItem);

  public void go(WjrDisplayHandler handler, HasWidgets container, Element store);
  
  public void notifyLoadingSucceeded(WjrStore store);
  
  public void notifyLoadingFailed(Throwable caught);
  
  public void notifyReloadingSucceeded();
  
  public void notifyReloadingFailed(Throwable caught);

  public void notifyRunningFinished();
}