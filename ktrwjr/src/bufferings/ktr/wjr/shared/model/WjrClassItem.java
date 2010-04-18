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
 * Test case class information.
 * 
 * This class keeps the summary of the test methods which are the children of
 * this test case class. The parent-child relationship of test classes and the
 * methods is defined in {@link WjrStore}.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrClassItem extends WjrSummaryItem {

  /**
   * The class canonical name.
   */
  protected String classCanonicalName;

  /**
   * The class simple name.
   */
  protected String classSimpleName;

  /**
   * Constructor for only GWT-RPC serialization.
   */
  @SuppressWarnings("unused")
  private WjrClassItem() {
  }

  /**
   * Constructs with the class canonical name.
   * 
   * The class canonical name must not be null or empty.
   * 
   * @param classCanonicalName
   *          The class canonical name.
   * @throws NullPointerException
   *           When the classCanonicalName parameter is null.
   * @throws IllegalArgumentException
   *           When the classCanonicalName parameter is empty.
   */
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
   * Gets the class canonical name.
   * 
   * @return The class canonical name.
   */
  public String getClassCanonicalName() {
    return classCanonicalName;
  }

  /**
   * Gets the class simple name.
   * 
   * @return The class simple name.
   */
  public String getClassSimpleName() {
    return classSimpleName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * bufferings.ktr.wjr.shared.model.WjrSummaryItem#getChildren(bufferings.ktr
   * .wjr.shared.model.WjrStore)
   */
  protected List<? extends WjrStoreItem> fetchChildren(WjrStore store) {
    return store.getMethodItems(classCanonicalName);
  }

}
