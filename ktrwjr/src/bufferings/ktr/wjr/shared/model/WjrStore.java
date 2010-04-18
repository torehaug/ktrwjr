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

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import com.google.gwt.user.client.rpc.IsSerializable;

public class WjrStore implements IsSerializable {

  protected static class Root extends WjrSummaryItem {
    @Override
    protected List<? extends WjrStoreItem> getChildren(WjrStore store) {
      return store.getClassItems();
    }
  }

  protected Root root = new Root();

  protected TreeMap<String, WjrClassItem> classItems =
    new TreeMap<String, WjrClassItem>();

  protected TreeMap<String, WjrMethodItem> methodItems =
    new TreeMap<String, WjrMethodItem>();

  public void addClassItem(WjrClassItem classItem) {
    checkNotNull(classItem, "The classItem parameter is null.");

    String classCanonicalName = classItem.getClassCanonicalName();
    checkState(
      !classItems.containsKey(classCanonicalName),
      "The %s has already exists.",
      classCanonicalName);

    classItems.put(classCanonicalName, classItem);
  }

  public void addMethodItem(WjrMethodItem methodItem) {
    checkNotNull(methodItem, "The methodItem parameter is null.");

    String classCanonicalName = methodItem.getClassCanonicalName();
    checkState(
      classItems.containsKey(classCanonicalName),
      "The %s is not found.",
      classCanonicalName);

    String methodCanonicalName = methodItem.getMethodCanonicalName();
    checkState(
      !methodItems.containsKey(methodCanonicalName),
      "The %s has already exists.",
      methodCanonicalName);

    methodItems.put(methodCanonicalName, methodItem);
  }

  public WjrClassItem getClassItem(String classCanonicalName) {
    checkNotNull(
      classCanonicalName,
      "The classCanonicalName parameter is null.");
    checkState(
      classItems.containsKey(classCanonicalName),
      "The %s is not found.",
      classCanonicalName);

    return classItems.get(classCanonicalName);
  }

  public WjrMethodItem getMethodItem(String methodCanonicalName) {
    checkNotNull(
      methodCanonicalName,
      "The methodCanonicalName parameter is null.");
    checkState(
      methodItems.containsKey(methodCanonicalName),
      "The %s is not found.",
      methodCanonicalName);

    return methodItems.get(methodCanonicalName);
  }

  public List<WjrClassItem> getClassItems() {
    return new ArrayList<WjrClassItem>(classItems.values());
  }

  public List<WjrMethodItem> getMethodItems(String classCanonicalName) {
    checkNotNull(
      classCanonicalName,
      "The classCanonicalName parameter is null.");
    checkState(
      classItems.containsKey(classCanonicalName),
      "The %s is not found.",
      classCanonicalName);

    List<WjrMethodItem> items = new ArrayList<WjrMethodItem>();

    SortedMap<String, WjrMethodItem> tailMap =
      methodItems.tailMap(classCanonicalName);
    for (WjrMethodItem item : tailMap.values()) {
      if (item.getClassCanonicalName().equals(classCanonicalName)) {
        items.add(item);
      } else {
        break;
      }
    }
    return items;
  }

  public int getTotalCount() {
    return root.getTotalCount();
  }

  public int getSuccessCount() {
    return root.getSuccessCount();
  }

  public int getFailureCount() {
    return root.getFailureCount();
  }

  public int getErrorCount() {
    return root.getErrorCount();
  }

  public int getNotYetCount() {
    return root.getNotYetCount() + root.getRunningCount();
  }

  public void updateSummary() {
    root.updateSummary(this);
  }

  public void clearAllResultsAndSummaries() {
    root.clearSummary();
    for (WjrMethodItem methodItem : methodItems.values()) {
      methodItem.clearResult();
    }
    for (WjrClassItem classItem : classItems.values()) {
      classItem.clearSummary();
    }
  }

  public void updateAllSummaries() {
    for (WjrClassItem classItem : classItems.values()) {
      classItem.updateSummary(this);
    }
    root.updateSummary(this);
  }

}
