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

import com.google.gwt.dom.client.Element;

/**
 * DOM utility class.
 * 
 * @author bufferings[at]gmail.com
 */
public class DOMUtil {

  /**
   * Scrolls the given element top into view.
   * 
   * <p>
   * This method crawls up the DOM hierarchy, adjusting the scrollTop property
   * of each scrollable element to ensure that the specified element is
   * completely in view. It adjusts each scroll position by the minimum amount
   * necessary.
   * </p>
   * 
   * @param elem
   *          the element to be made visible
   */
  public static native void scrollTopIntoView(Element elem) /*-{
    var top = elem.offsetTop;
    var height = elem.offsetHeight;

    if (elem.parentNode != elem.offsetParent) {
      top -= elem.parentNode.offsetTop;
    }

    var cur = elem.parentNode;
    while (cur && (cur.nodeType == 1)) {
      if (top < cur.scrollTop) {
        cur.scrollTop = top;
      }
      if (top + height > cur.scrollTop + cur.clientHeight) {
        cur.scrollTop = (top + height) - cur.clientHeight;
      }

      var offsetTop = cur.offsetTop;
      if (cur.parentNode != cur.offsetParent) {
        offsetTop -= cur.parentNode.offsetTop;
      }

      top += offsetTop - cur.scrollTop;
      cur = cur.parentNode;
    }
  }-*/;
}
