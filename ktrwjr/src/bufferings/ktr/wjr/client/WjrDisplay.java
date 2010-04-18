/**
 * 
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