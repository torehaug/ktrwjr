package bufferings.ktr.wjr.client.runner;

import java.util.List;

import bufferings.ktr.wjr.client.WjrDisplay;
import bufferings.ktr.wjr.client.service.KtrWjrServiceAsync;
import bufferings.ktr.wjr.shared.model.WjrConfig;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;
import bufferings.ktr.wjr.shared.model.WjrStoreItem.State;

import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The test runner which runs the tests sequentially.
 * 
 * @author bufferings[at]gmail.com
 */
public class SequentialRunner extends AbstractRunner implements
    AsyncCallback<WjrMethodItem> {

  /**
   * The timer to retry tests after waiting.
   * 
   * @author bufferings[at]gmail.com
   */
  protected class RetryTimer extends Timer {

    /**
     * The target method item.
     */
    protected final WjrMethodItem targetMethodItem;

    /**
     * Constructs the retry timer.
     * 
     * @param targetMethod
     *          The target method item.
     */
    public RetryTimer(WjrMethodItem targetMethod) {
      this.targetMethodItem = targetMethod;
    }

    /**
     * {@inheritDoc}
     */
    public void run() {
      if (cancelRequested) {
        onFinishRunning(methodItems);
        return;
      }

      targetMethodItem.setState(State.RUNNING);
      updateSummaryAndRepaintView(targetMethodItem);

      runCurrentIndexTestMethod();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void cancel() {
      super.cancel();
      if (cancelRequested) {
        onFinishRunning(methodItems);
      }
    }
  }

  /**
   * The index of the running test.
   */
  private int currentIndex;

  /**
   * The method items to run.
   */
  private List<WjrMethodItem> methodItems;

  /**
   * Constructs the runner.
   * 
   * This class receives information from presenter, and use it. This class is a
   * part of presenter.
   * 
   * @param rpcService
   *          The rpc service.
   * @param view
   *          The view.
   * @param config
   *          The configuration.
   * @param store
   *          The test store.
   */
  public SequentialRunner(KtrWjrServiceAsync rpcService, WjrDisplay view,
      WjrConfig config, WjrStore store) {
    super(rpcService, view, config, store);
  }

  /**
   * {@inheritDoc}
   */
  protected void runBare(List<WjrMethodItem> methodItemsToRun) {
    methodItems = methodItemsToRun;
    currentIndex = 0;
    runCurrentIndexTestMethod();
  }

  /**
   * Runs the test that is the currentIndex of the methodItems.
   */
  protected void runCurrentIndexTestMethod() {
    rpcService.runTest(
      methodItems.get(currentIndex),
      config.isCpumsEnabled(),
      config.isApimsEnabled(),
      config.isLogHookEnabled(),
      config.getLogHookTimezone(),
      this);
  }

  /**
   * {@inheritDoc}
   */
  public void onFailure(Throwable caught) {
    WjrMethodItem stored = methodItems.get(currentIndex);
    unregisterRetryTimer(stored);

    stored.setState(State.ERROR);
    stored.setTrace(getTrace(caught));
    updateSummaryAndRepaintView(stored);

    currentIndex++;
    if (methodItems.size() <= currentIndex || cancelRequested) {
      onFinishRunning(methodItems);
      return;
    }

    runCurrentIndexTestMethod();
  }

  /**
   * {@inheritDoc}
   */
  public void onSuccess(WjrMethodItem result) {
    WjrMethodItem stored = store.getMethodItem(result.getClassAndMethodName());
    result.copyResult(stored);

    if (!cancelRequested
      && result.isOverQuota()
      && config.isRetryOverQuotaEnabled()
      && stored.getRetryCount() < stored.getMaxRetryCount()) {

      stored.setState(State.RETRY_WAITING);
      stored.setRetryCount(stored.getRetryCount() + 1);
      updateSummaryAndRepaintView(stored);

      registerRetryTimer(stored);
    } else {
      unregisterRetryTimer(stored);
      updateSummaryAndRepaintView(stored);

      currentIndex++;
      if (methodItems.size() <= currentIndex || cancelRequested) {
        onFinishRunning(methodItems);
        return;
      }

      runCurrentIndexTestMethod();
    }
  }

  /**
   * Creates and registers the retry timer.
   * 
   * @param targeMethodItem
   *          The method item to retry.
   */
  protected void registerRetryTimer(WjrMethodItem targeMethodItem) {
    Timer retryTimer = new RetryTimer(targeMethodItem);
    retryTimerMap.put(targeMethodItem.getClassAndMethodName(), retryTimer);
    retryTimer.schedule(config.getRetryOverQuotaInterval() * 1000);
  }

  /**
   * Unregisters the retry timer. If the timer is not defined, does nothing.
   * 
   * @param targeMethodItem
   *          The method item to retry.
   */
  protected void unregisterRetryTimer(WjrMethodItem targeMethodItem) {
    retryTimerMap.remove(targeMethodItem.getClassAndMethodName());
  }

  /**
   * Updates the summary and repaints the view.
   * 
   * @param updatedMethodItem
   *          The updated method item.
   */
  protected void updateSummaryAndRepaintView(WjrMethodItem updatedMethodItem) {
    store.getClassItem(updatedMethodItem.getClassName()).updateSummary(store);
    store.updateSummary();
    view.repaintTreeItemAncestors(store, updatedMethodItem);
  }
}
