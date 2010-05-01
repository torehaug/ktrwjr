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
 * The test case class information.
 * 
 * This class keeps the summary of the test methods which are the children of
 * this test case class. The parent-child relationship between test classes and
 * methods is defined in {@link WjrStore}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrClassItem extends WjrSummaryItem {

  /**
   * The test class name.
   */
  protected String className;

  /**
   * Constructor for only GWT-RPC serialization.
   */
  @SuppressWarnings("unused")
  private WjrClassItem() {
  }

  /**
   * Constructs with the class name.
   * 
   * @param className
   *          The class name.
   * @throws NullPointerException
   *           When the className parameter is null.
   * @throws IllegalArgumentException
   *           When the className parameter is empty.
   */
  public WjrClassItem(String className) {
    checkNotNull(className, "The className parameter is null.");
    checkArgument(className.length() > 0, "The className parameter is empty.");

    this.className = className;
  }

  /**
   * Gets the class item name.
   * 
   * @return The class item name.
   */
  public String getClassName() {
    return className;
  }

  /**
   * {@inheritDoc}
   */
  protected List<? extends WjrStoreItem> fetchChildren(WjrStore store) {
    return store.getMethodItems(className);
  }

}
