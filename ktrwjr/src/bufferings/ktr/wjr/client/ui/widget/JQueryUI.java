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
package bufferings.ktr.wjr.client.ui.widget;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

/**
 * JQueryUI constants.
 * 
 * @author bufferings[at]gmail.com
 */
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

  // This is prepared by KtrWjr
  public static final String UI_ICON_EMPTY = "ui-icon-empty";

  public static final String UI_ICON_TRIANGLE_1_S = "ui-icon-triangle-1-s";
  public static final String UI_ICON_TRIANGLE_1_E = "ui-icon-triangle-1-e";
  public static final String UI_ICON_CHECK = "ui-icon-check";
  public static final String UI_ICON_NOTICE = "ui-icon-notice";
  public static final String UI_ICON_CLOSE = "ui-icon-close";
  public static final String UI_ICON_MINUS = "ui-icon-minus";
  public static final String UI_ICON_ARRORREFRESH_1_W =
    "ui-icon-arrowrefresh-1-w";
  public static final String UI_ICON_CLOCK = "ui-icon-clock";
  
  public static final String UI_ICON_PLAY = "ui-icon-play";
  public static final String UI_ICON_PAUSE = "ui-icon-pause";

  /**
   * Joins the css class names with whitespace.
   * 
   * @param args
   *          The css class names.
   * @return The joinded css class names.
   * @throws NullPointerException
   *           If the args is null or the args contains null value.
   */
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
