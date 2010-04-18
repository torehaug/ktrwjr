package bufferings.ktr.wjr.shared.model;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.List;

public abstract class WjrSummaryItem extends WjrStoreItem {

  protected int totalCount = 0;

  protected int successCount = 0;

  protected int errorCount = 0;

  protected int failureCount = 0;

  protected int runningCount = 0;

  protected int notYetCount = 0;

  public int getTotalCount() {
    return totalCount;
  }

  public int getSuccessCount() {
    return successCount;
  }

  public int getErrorCount() {
    return errorCount;
  }

  public int getFailureCount() {
    return failureCount;
  }

  public int getRunningCount() {
    return runningCount;
  }

  public int getNotYetCount() {
    return notYetCount;
  }

  public void clearSummary() {
    state = State.NOT_YET;
    successCount = 0;
    errorCount = 0;
    failureCount = 0;
    runningCount = 0;
    notYetCount = totalCount;
  }

  public void updateSummary(WjrStore store) {
    checkNotNull(store, "The store parameter is null.");

    state = State.NOT_YET;
    successCount = 0;
    errorCount = 0;
    failureCount = 0;
    runningCount = 0;
    notYetCount = 0;
    totalCount = 0;

    List<? extends WjrStoreItem> items = getChildren(store);
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
    notYetCount += classItem.getNotYetCount();
    totalCount += classItem.getTotalCount();
  }

  private void appendSummaryWithStoreItem(WjrStoreItem storeItem) {
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
    case NOT_YET:
      notYetCount++;
      break;
    default:
      // GWT does not emulate format, so i use the format method of Guice's
      // Preconditions class.
      throw new AssertionError(format("Unknown state. [State=%s]", storeItem
        .getState()
        .name()));
    }
    totalCount++;
  }

  private void updateState() {
    if (errorCount > 0) {
      state = State.ERROR;
    } else if (failureCount > 0) {
      state = State.FAILURE;
    } else if (runningCount > 0) {
      state = State.RUNNING;
    } else if (notYetCount > 0) {
      state = State.NOT_YET;
    } else {
      state = State.SUCCESS;
    }
  }

  protected abstract List<? extends WjrStoreItem> getChildren(WjrStore store);
}
