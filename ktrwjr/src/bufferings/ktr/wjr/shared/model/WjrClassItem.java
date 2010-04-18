package bufferings.ktr.wjr.shared.model;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.util.List;

public class WjrClassItem extends WjrSummaryItem {
  protected String classCanonicalName;

  protected String classSimpleName;

  @SuppressWarnings("unused")
  private WjrClassItem() {
  }

  public WjrClassItem(String classCanonicalName) {
    checkNotNull(
      classCanonicalName,
      "The classCanonicalName parameter is null.");
    checkArgument(
      !classCanonicalName.isEmpty(),
      "The classCanonicalName parameter is empty.");

    int simpleNameStartPos = classCanonicalName.lastIndexOf('.') + 1;
    this.classCanonicalName = classCanonicalName;
    this.classSimpleName = classCanonicalName.substring(simpleNameStartPos);
  }

  /**
   * @return the classCanonicalName
   */
  public String getClassCanonicalName() {
    return classCanonicalName;
  }

  /**
   * @return the classSimpleName
   */
  public String getClassSimpleName() {
    return classSimpleName;
  }

  @Override
  protected List<? extends WjrStoreItem> getChildren(WjrStore store) {
    return store.getMethodItems(classCanonicalName);
  }

}
