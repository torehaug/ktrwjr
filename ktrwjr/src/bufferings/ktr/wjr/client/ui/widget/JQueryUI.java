package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

public class JQueryUI {

  public static final String UI_WIDGET = "ui-widget";
  public static final String UI_BUTTON = "ui-button";

  public static final String UI_STATE_DEFAULT = "ui-state-default";
  public static final String UI_STATE_HOVER = "ui-state-hover";
  public static final String UI_STATE_ACTIVE = "ui-state-active";
  public static final String UI_STATE_HIGHLIGHT = "ui-state-highlight";
  public static final String UI_STATE_ERROR = "ui-state-error";
  public static final String UI_STATE_DISABLED = "ui-state-disabled";

  public static final String UI_CORNER_ALL = "ui-corner-all";
  public static final String UI_CORNER_TOP = "ui-corner-top";

  public static final String UI_ICON = "ui-icon";
  // This is prepared by ktrwjr
  public static final String UI_ICON_EMPTY = "ui-icon-empty";
  public static final String UI_ICON_TRIANGLE_1_S = "ui-icon-triangle-1-s";
  public static final String UI_ICON_TRIANGLE_1_E = "ui-icon-triangle-1-e";
  public static final String UI_ICON_CHECK = "ui-icon-check";
  public static final String UI_ICON_NOTICE = "ui-icon-notice";
  public static final String UI_ICON_CLOSE = "ui-icon-close";
  public static final String UI_ICON_MINUS = "ui-icon-minus";
  public static final String UI_ICON_ARRORREFRESH_1_W = "ui-icon-arrowrefresh-1-w";

  public static final String UI_ICON_PLAY = "ui-icon-play";
  public static final String UI_ICON_PAUSE = "ui-icon-pause";

  public static String join(String... args) {
    checkNotNull(args, "The args parameter is null.");

    int count = 0;
    for (String s : args) {
      count += checkNotNull(s, "The args contains null element.").length();
      count++;
    }
    count--;

    StringBuilder sb = new StringBuilder(count);
    sb.append(args[0]);
    for (int i = 1; i < args.length; i++) {
      sb.append(" ").append(args[i]);
    }
    return sb.toString();
  }

  private JQueryUI() {
  }
}
