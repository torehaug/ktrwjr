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
package bufferings.ktr.wjr.shared.model;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

/**
 * The item stored in {@link WjrStore}.
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class WjrStoreItem {

  /**
   * The run test states.
   * 
   * @author bufferings[at]gmail.com
   */
  public enum State {
    NOT_YET, SUCCESS, FAILURE, ERROR, RUNNING, RETRY_WAITING
  }

  /**
   * The run test state. This cannot be null.
   */
  protected State state = State.NOT_YET;

  /**
   * Gets the run test state. This cannot be null.
   * 
   * @return The run test state.
   */
  public State getState() {
    return state;
  }

  /**
   * Sets the run test state.
   * 
   * @param state
   *          The run test state, cannot be null.
   * @throws NullPointerException
   *           When the state parameter is null.
   */
  public void setState(State state) {
    checkNotNull(state, "The state parameter is null.");
    this.state = state;
  }

}
