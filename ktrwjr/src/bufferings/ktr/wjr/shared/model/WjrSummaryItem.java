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

import java.util.List;

/**
 * The test summary item.
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class WjrSummaryItem extends WjrStoreItem {

  /**
   * The total count of the children.
   */
  protected int totalCount = 0;

  /**
   * The success count of the children.
   */
  protected int successCount = 0;

  /**
   * The error count of the children.
   */
  protected int errorCount = 0;

  /**
   * The failure count of the children.
   */
  protected int failureCount = 0;

  /**
   * The running count of the children.
   */
  protected int runningCount = 0;

  /**
   * The retry-waiting count of the children.
   */
  protected int retryWaitingCount = 0;

  /**
   * The not yet count of the children.
   */
  protected int notYetCount = 0;

  /**
   * Gets the total count of the children.
   * 
   * @return The total count of the children.
   */
  public int getTotalCount() {
    return totalCount;
  }

  /**
   * Gets the success count of the children.
   * 
   * @return The success count of the children.
   */
  public int getSuccessCount() {
    return successCount;
  }

  /**
   * Gets the error count of the children.
   * 
   * @return The error count of the children.
   */
  public int getErrorCount() {
    return errorCount;
  }

  /**
   * Gets the failure count of the children.
   * 
   * @return The failure count of the children.
   */
  public int getFailureCount() {
    return failureCount;
  }

  /**
   * Gets the running count of the children.
   * 
   * @return The running count of the children.
   */
  public int getRunningCount() {
    return runningCount;
  }

  /**
   * Gets the retry waiting count of the children.
   * 
   * @return The retry waiting count of the children.
   */
  public int getRetryWaitingCount() {
    return retryWaitingCount;
  }

  /**
   * Gets the not yet count of the children.
   * 
   * @return The not yet count of the children.
   */
  public int getNotYetCount() {
    return notYetCount;
  }

  /**
   * Clears the summary, but remains totalCount and set the not yet count equals
   * to totalCount.
   */
  public void clearSummary() {
    state = State.NOT_YET;
    successCount = 0;
    errorCount = 0;
    failureCount = 0;
    runningCount = 0;
    retryWaitingCount = 0;
    notYetCount = totalCount;
  }

  /**
   * Fetches the children from the store, and updates the state and the summary
   * of the children. This method does not search grand children.
   * 
   * @param store
   *          The store, cannot be null.
   * @throws NullPointerException
   *           When the store parameter is null.
   */
  public void updateSummary(WjrStore store) {
    checkNotNull(store, "The store parameter is null.");

    state = State.NOT_YET;
    successCount = 0;
    errorCount = 0;
    failureCount = 0;
    runningCount = 0;
    retryWaitingCount = 0;
    notYetCount = 0;
    totalCount = 0;

    List<? extends WjrStoreItem> items = fetchChildren(store);
    for (WjrStoreItem item : items) {
      if (item instanceof WjrSummaryItem) {
        appendSummaryWithSummaryItem((WjrSummaryItem) item);
      } else {
        appendSummaryWithStoreItem((WjrStoreItem) item);
      }
    }

    updateState();
  }

  private void appendSummaryWithSummaryItem(WjrSummaryItem classItem) {
    successCount += classItem.getSuccessCount();
    errorCount += classItem.getErrorCount();
    failureCount += classItem.getFailureCount();
    runningCount += classItem.getRunningCount();
    retryWaitingCount += classItem.getRetryWaitingCount();
    notYetCount += classItem.getNotYetCount();
    totalCount += classItem.getTotalCount();
  }

  private void appendSummaryWithStoreItem(WjrStoreItem storeItem) {
    totalCount++;
    switch (storeItem.getState()) {
    case SUCCESS:
      successCount++;
      break;
    case ERROR:
      errorCount++;
      break;
    case FAILURE:
      failureCount++;
      break;
    case RUNNING:
      runningCount++;
      break;
    case RETRY_WAITING:
      retryWaitingCount++;
      break;
    case NOT_YET:
      notYetCount++;
      break;
    default:
      // GWT does not emulate String#format, so i use the format method of
      // Guice's
      // Preconditions class.
      throw new AssertionError(format("Unknown state. [State=%s]", storeItem
        .getState()
        .name()));
    }
  }

  private void updateState() {
    if (errorCount > 0) {
      state = State.ERROR;
    } else if (failureCount > 0) {
      state = State.FAILURE;
    } else if (runningCount > 0 || retryWaitingCount > 0) {
      state = State.RUNNING;
    } else if (notYetCount > 0) {
      state = State.NOT_YET;
    } else {
      state = State.SUCCESS;
    }
  }

  /**
   * Fetches the children from {@link WjrStore}. When no children exist, the
   * override method must return empty list, not null.
   * 
   * @param store
   *          The store.
   * @return The children list.
   */
  protected abstract List<? extends WjrStoreItem> fetchChildren(WjrStore store);
}
