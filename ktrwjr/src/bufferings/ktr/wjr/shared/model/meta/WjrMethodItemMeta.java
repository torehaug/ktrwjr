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
package bufferings.ktr.wjr.shared.model.meta;

/**
 * A meta of WjrMethodItem.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrMethodItemMeta {

  private static final WjrMethodItemMeta meta = new WjrMethodItemMeta();

  public static WjrMethodItemMeta meta() {
    return meta;
  }

  public final String className = "className";

  public final String methodName = "methodName";

  public final String trace = "trace";

  public final String log = "log";

  public final String time = "time";

  public final String cpuTime = "cpuTime";

  public final String apiTime = "apiTime";

  public final String isOverQuota = "isOverQuota";

  public final String retryCount = "retryCount";

  public final String maxRetryCount = "maxRetryCount";

  public final String waitingSeconds = "waitingSeconds";

  public final String state = "state";

  private WjrMethodItemMeta() {
  };
}
