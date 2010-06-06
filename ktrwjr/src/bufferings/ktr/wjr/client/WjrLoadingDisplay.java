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

import com.google.gwt.user.client.ui.HasWidgets;

/**
 * KtrWjr loading view interface.
 * 
 * @author bufferings[at]gmail.com
 */
public interface WjrLoadingDisplay {

  /**
   * Starts the view.
   * 
   * @param container
   *          The view container.
   */
  public void go(HasWidgets container);

  /**
   * Notifies loaded to the view.
   */
  public void notifyLoaded();
}