package bufferings.ktr.wjr.shared.model;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WjrStoreItem implements IsSerializable {

  public enum State {
    NOT_YET, SUCCESS, FAILURE, ERROR, RUNNING
  }

  protected State state = State.NOT_YET;

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

}
