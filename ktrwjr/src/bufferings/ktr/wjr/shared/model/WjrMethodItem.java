package bufferings.ktr.wjr.shared.model;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

public class WjrMethodItem extends WjrStoreItem {

  protected static final String CLASS_METHOD_SEPARATOR = "#";

  protected String classCanonicalName;

  protected String classSimpleName;

  protected String methodCanonicalName;

  protected String methodSimpleName;

  protected String trace;

  protected String log;

  protected String time;

  protected String cpuTime;

  protected String apiTime;

  /**
   * Default constructor for GWT serialization.
   */
  @SuppressWarnings("unused")
  private WjrMethodItem() {
  }

  public WjrMethodItem(String classCanonicalName, String methodName) {
    checkNotNull(
      classCanonicalName,
      "The classCanonicalName parameter is null.");
    checkArgument(
      !classCanonicalName.isEmpty(),
      "The classCanonicalName parameter is empty.");
    checkNotNull(methodName, "The methodName parameter is null.");
    checkArgument(!methodName.isEmpty(), "The methodName parameter is empty.");

    int simpleNameStartPos = classCanonicalName.lastIndexOf('.') + 1;
    this.classCanonicalName = classCanonicalName;
    this.classSimpleName = classCanonicalName.substring(simpleNameStartPos);
    this.methodCanonicalName =
      classCanonicalName + CLASS_METHOD_SEPARATOR + methodName;
    this.methodSimpleName = methodName;
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

  /**
   * @return the methodCanonicalName
   */
  public String getMethodCanonicalName() {
    return methodCanonicalName;
  }

  /**
   * @return the methodSimpleName
   */
  public String getMethodSimpleName() {
    return methodSimpleName;
  }

  /**
   * @return the trace
   */
  public String getTrace() {
    return trace;
  }

  /**
   * @param trace
   *          the trace to set
   */
  public void setTrace(String trace) {
    this.trace = trace;
  }

  /**
   * @return the time
   */
  public String getTime() {
    return time;
  }

  /**
   * @param time
   *          the time to set
   */
  public void setTime(String time) {
    this.time = time;
  }

  public String getCpuTime() {
    return cpuTime;
  }

  public void setCpuTime(String cpuTime) {
    this.cpuTime = cpuTime;
  }

  public String getApiTime() {
    return apiTime;
  }

  public void setApiTime(String apiTime) {
    this.apiTime = apiTime;
  }

  public String getLog() {
    return log;
  }

  public void setLog(String log) {
    this.log = log;
  }

  public void clearResult() {
    state = State.NOT_YET;
    trace = null;
    log = null;
    time = null;
    cpuTime = null;
    apiTime = null;
  }

}
