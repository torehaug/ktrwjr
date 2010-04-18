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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

public class WjrStoreTest {
//  private WjrStore wjrStore = new WjrStore();
//
//  @Test
//  public void setWjrFilter_CanSetFilter() {
//    HideSuccessMethodFilter filter = new HideSuccessMethodFilter();
//    wjrStore.setFilter(filter);
//    assertTrue(wjrStore.filter == filter);
//  }
//
//  @Test
//  public void setWjrFilter_WillSetAllPassFilter_WhenFilterIsNull() {
//    wjrStore.setFilter(null);
//    assertThat(wjrStore.filter.getClass(), is(AllPassFilter.class.getClass()));
//  }
//
//  @Test(expected = NullPointerException.class)
//  public void addWjrClassItem_WillThrowNPE_WhenWjrClassItemIsNull() {
//    wjrStore.addClassItem(null);
//  }
//
//  @Test
//  public void addWjrClassItem_CanAdditem() {
//    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
//    wjrStore.addClassItem(wjrClassItem);
//    assertThat(wjrStore.classItems.get("foo.Foo"), is(wjrClassItem));
//  }
//
//  @Test(expected = IllegalStateException.class)
//  public void addWjrClassItem_WillThrowISE_WhenWjrClassItemExist() {
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo"));
//  }
//
//  @Test(expected = NullPointerException.class)
//  public void addWjrMethodItem_WillThrowNPE_WhenWjrMethodItemIsNull() {
//    wjrStore.addMethodItem(null);
//  }
//
//  @Test(expected = IllegalStateException.class)
//  public void addWjrMethodItem_WillThrowISE_WhenWjrClassItemNotExist() {
//    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
//    wjrStore.addMethodItem(wjrMethodItem);
//  }
//
//  @Test
//  public void addWjrMethodItem_CanAdditem() {
//    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
//    wjrStore.addClassItem(wjrClassItem);
//
//    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
//    wjrStore.addMethodItem(wjrMethodItem);
//    assertThat(wjrStore.methodItems.get("foo.Foo#barMethod"), is(wjrMethodItem));
//  }
//
//  @Test(expected = IllegalStateException.class)
//  public void addWjrMethodItem_WillThrowISE_WhenWjrMethodItemExist() {
//    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
//    wjrStore.addClassItem(wjrClassItem);
//
//    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
//    wjrStore.addMethodItem(wjrMethodItem);
//    wjrStore.addMethodItem(wjrMethodItem);
//  }
////
////  @Test(expected = NullPointerException.class)
////  public void setWjrClassItem_WillThrowNPE_WhenWjrClassItemIsNull() {
////    wjrStore.setClassItem(null);
////  }
////
////  @Test(expected = IllegalStateException.class)
////  public void setWjrClassItem_WillThrowISE_WhenWjrClassItemNotExist() {
////    wjrStore.setClassItem(new WjrClassItem("foo.Foo"));
////  }
////
////  @Test
////  public void setWjrClassItem_CanSetItem() {
////    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
////    wjrStore.addClassItem(wjrClassItem);
////
////    WjrClassItem wjrClassItem2 = new WjrClassItem("foo.Foo");
////    wjrStore.setClassItem(wjrClassItem2);
////
////    assertThat(wjrStore.classItems.get("foo.Foo"), is(not(wjrClassItem)));
////    assertThat(wjrStore.classItems.get("foo.Foo"), is(wjrClassItem2));
////  }
////
////  @Test(expected = NullPointerException.class)
////  public void setWjrMethodItem_WillThrowNPE_WhenWjrMethodItemIsNull() {
////    wjrStore.setMethodItem(null);
////  }
////
////  @Test(expected = IllegalStateException.class)
////  public void setWjrMethodItem_WillThrowISE_WhenWjrClassItemNotExist() {
////    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
////    wjrStore.setMethodItem(wjrMethodItem);
////  }
////
////  @Test(expected = IllegalStateException.class)
////  public void setWjrMethodItem_WillThrowISE_WhenWjrMethodItemNotExist() {
////    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
////    wjrStore.addClassItem(wjrClassItem);
////
////    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
////    wjrStore.setMethodItem(wjrMethodItem);
////  }
////
////  @Test
////  public void setWjrMethodItem_CanSetItem() {
////    WjrClassItem wjrClassItem = new WjrClassItem("foo.Foo");
////    wjrStore.addClassItem(wjrClassItem);
////
////    WjrMethodItem wjrMethodItem = new WjrMethodItem("foo.Foo", "barMethod");
////    wjrStore.addMethodItem(wjrMethodItem);
////
////    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo", "barMethod");
////    wjrStore.setMethodItem(wjrMethodItem2);
////
////    assertThat(
////      wjrStore.methodItems.get("foo.Foo#barMethod"),
////      is(not(wjrMethodItem)));
////    assertThat(
////      wjrStore.methodItems.get("foo.Foo#barMethod"),
////      is(wjrMethodItem2));
////  }
//
//  @Test
//  public void getWjrClassItems_WillReturnEmptyList_WhenNoItemsExist() {
//    assertTrue(wjrStore.getFilteredClassItems().isEmpty());
//  }
//
//  @Test
//  public void getWjrClassItems_CanReturnList() {
//    WjrClassItem wjrClassItem1 = new WjrClassItem("foo.Foo1");
//    WjrClassItem wjrClassItem2 = new WjrClassItem("foo.Foo2");
//    WjrClassItem wjrClassItem3 = new WjrClassItem("foo.Foo3");
//    WjrClassItem wjrClassItem4 = new WjrClassItem("foo.Foo4");
//    WjrClassItem wjrClassItem5 = new WjrClassItem("foo.Foo5");
//
//    wjrStore.addClassItem(wjrClassItem1);
//    wjrStore.addClassItem(wjrClassItem3);
//    wjrStore.addClassItem(wjrClassItem2);
//    wjrStore.addClassItem(wjrClassItem5);
//    wjrStore.addClassItem(wjrClassItem4);
//
//    List<WjrClassItem> items = wjrStore.getFilteredClassItems();
//    assertThat(items.size(), is(5));
//    assertThat(items.get(0), is(wjrClassItem1));
//    assertThat(items.get(1), is(wjrClassItem2));
//    assertThat(items.get(2), is(wjrClassItem3));
//    assertThat(items.get(3), is(wjrClassItem4));
//    assertThat(items.get(4), is(wjrClassItem5));
//  }
//
//  @Test
//  public void getWjrClassItems_CanReturnFilteredList() {
//    wjrStore.setFilter(new WjrFilter() {
//      @Override
//      public boolean accept(WjrClassItem wjrClassItem) {
//        if (wjrClassItem.getClassCanonicalName().equals("foo.Foo1")
//          || wjrClassItem.getClassCanonicalName().equals("foo.Foo4")) {
//          return false;
//        }
//        return true;
//      }
//
//      @Override
//      public boolean accept(WjrMethodItem wjrMethodItem) {
//        return true;
//      }
//    });
//
//    WjrClassItem wjrClassItem1 = new WjrClassItem("foo.Foo1");
//    WjrClassItem wjrClassItem2 = new WjrClassItem("foo.Foo2");
//    WjrClassItem wjrClassItem3 = new WjrClassItem("foo.Foo3");
//    WjrClassItem wjrClassItem4 = new WjrClassItem("foo.Foo4");
//    WjrClassItem wjrClassItem5 = new WjrClassItem("foo.Foo5");
//
//    wjrStore.addClassItem(wjrClassItem1);
//    wjrStore.addClassItem(wjrClassItem3);
//    wjrStore.addClassItem(wjrClassItem2);
//    wjrStore.addClassItem(wjrClassItem5);
//    wjrStore.addClassItem(wjrClassItem4);
//
//    List<WjrClassItem> items = wjrStore.getFilteredClassItems();
//    assertThat(items.size(), is(3));
//    assertThat(items.get(0), is(wjrClassItem2));
//    assertThat(items.get(1), is(wjrClassItem3));
//    assertThat(items.get(2), is(wjrClassItem5));
//  }
//
//  @Test(expected = IllegalStateException.class)
//  public void getWjrMethodItems_WillThrowISE_WhenWjrClassItemNotExist() {
//    wjrStore.getFilteredMethodItems("foo.Foo");
//  }
//
//  @Test
//  public void getWjrMethodItems_WillReturnEmptyList_WhenNoMethodsExist() {
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo1"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo2"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo3"));
//
//    assertTrue(wjrStore.getFilteredMethodItems("foo.Foo1").isEmpty());
//  }
//
//  @Test
//  public void getWjrMethodItems_CanReturnList_InFirstParent() {
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo1"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo2"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo3"));
//
//    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo1", "barMethod1");
//    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo1", "barMethod2");
//    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo1", "barMethod3");
//    wjrStore.addMethodItem(wjrMethodItem3);
//    wjrStore.addMethodItem(wjrMethodItem2);
//    wjrStore.addMethodItem(wjrMethodItem1);
//
//    List<WjrMethodItem> items = wjrStore.getFilteredMethodItems("foo.Foo1");
//    assertThat(items.size(), is(3));
//    assertThat(items.get(0), is(wjrMethodItem1));
//    assertThat(items.get(1), is(wjrMethodItem2));
//    assertThat(items.get(2), is(wjrMethodItem3));
//  }
//
//  @Test
//  public void getWjrMethodItems_CanReturnList_InSecondParent() {
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo1"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo2"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo3"));
//
//    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo2", "barMethod1");
//    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo2", "barMethod2");
//    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo2", "barMethod3");
//    wjrStore.addMethodItem(wjrMethodItem3);
//    wjrStore.addMethodItem(wjrMethodItem2);
//    wjrStore.addMethodItem(wjrMethodItem1);
//
//    List<WjrMethodItem> items = wjrStore.getFilteredMethodItems("foo.Foo2");
//    assertThat(items.size(), is(3));
//    assertThat(items.get(0), is(wjrMethodItem1));
//    assertThat(items.get(1), is(wjrMethodItem2));
//    assertThat(items.get(2), is(wjrMethodItem3));
//  }
//
//  @Test
//  public void getWjrMethodItems_CanReturnList_InLastParent() {
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo1"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo2"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo3"));
//
//    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo3", "barMethod1");
//    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo3", "barMethod2");
//    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo3", "barMethod3");
//    wjrStore.addMethodItem(wjrMethodItem3);
//    wjrStore.addMethodItem(wjrMethodItem2);
//    wjrStore.addMethodItem(wjrMethodItem1);
//
//    List<WjrMethodItem> items = wjrStore.getFilteredMethodItems("foo.Foo3");
//    assertThat(items.size(), is(3));
//    assertThat(items.get(0), is(wjrMethodItem1));
//    assertThat(items.get(1), is(wjrMethodItem2));
//    assertThat(items.get(2), is(wjrMethodItem3));
//  }
//
//  @Test
//  public void getWjrMethodItems_CanReturnFilteredList() {
//    wjrStore.setFilter(new WjrFilter() {
//      @Override
//      public boolean accept(WjrClassItem wjrClassItem) {
//        return true;
//      }
//
//      @Override
//      public boolean accept(WjrMethodItem wjrMethodItem) {
//        if (wjrMethodItem.getMethodSimpleName().equals("barMethod1")
//          || wjrMethodItem.getMethodSimpleName().equals("barMethod2")) {
//          return false;
//        }
//        return true;
//      }
//    });
//
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo1"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo2"));
//    wjrStore.addClassItem(new WjrClassItem("foo.Foo3"));
//
//    WjrMethodItem wjrMethodItem1 = new WjrMethodItem("foo.Foo1", "barMethod1");
//    WjrMethodItem wjrMethodItem2 = new WjrMethodItem("foo.Foo1", "barMethod2");
//    WjrMethodItem wjrMethodItem3 = new WjrMethodItem("foo.Foo1", "barMethod3");
//    wjrStore.addMethodItem(wjrMethodItem3);
//    wjrStore.addMethodItem(wjrMethodItem2);
//    wjrStore.addMethodItem(wjrMethodItem1);
//
//    List<WjrMethodItem> items = wjrStore.getFilteredMethodItems("foo.Foo1");
//    assertThat(items.size(), is(1));
//    assertThat(items.get(0), is(wjrMethodItem3));
//  }

}
