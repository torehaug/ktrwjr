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
  private WjrStore store = new WjrStore();

  @Test(expected = NullPointerException.class)
  public void addClassItem_WillThrowNPE_WhenClassItemIsNull() {
    store.addClassItem(null);
  }

  @Test
  public void addClassItem_CanAdditem() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);
    assertThat(store.classItems.get("foo.Foo"), is(classItem));
  }

  @Test(expected = IllegalStateException.class)
  public void addClassItem_WillThrowISE_WhenClassItemExists() {
    store.addClassItem(new WjrClassItem("foo.Foo"));
    store.addClassItem(new WjrClassItem("foo.Foo"));
  }

  @Test(expected = NullPointerException.class)
  public void addMethodItem_WillThrowNPE_WhenMethodItemIsNull() {
    store.addMethodItem(null);
  }

  @Test(expected = IllegalStateException.class)
  public void addMethodItem_WillThrowISE_WhenClassItemNotExist() {
    WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "barMethod");
    store.addMethodItem(methodItem);
  }

  @Test
  public void addMethodItem_CanAdditem() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "barMethod");
    store.addMethodItem(methodItem);
    assertThat(store.methodItems.get("foo.Foo#barMethod"), is(methodItem));
  }

  @Test(expected = IllegalStateException.class)
  public void addMethodItem_WillThrowISE_WhenMethodItemExist() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "barMethod");
    store.addMethodItem(methodItem);
    store.addMethodItem(methodItem);
  }

  @Test(expected = NullPointerException.class)
  public void getClassItem_WillThrowNPE_WhenNameIsNull() {
    store.getClassItem(null);
  }

  @Test(expected = IllegalStateException.class)
  public void getClassItem_WillThrowISE_WhenNameNotExist() {
    store.getClassItem("foo.Foo");
  }

  @Test
  public void getClassItem_CanGetClassItem() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    assertThat(store.getClassItem("foo.Foo"), is(classItem));
  }

  @Test(expected = NullPointerException.class)
  public void getMethodItem_WillThrowNPE_WhenNameIsNull() {
    store.getMethodItem(null);
  }

  @Test(expected = IllegalStateException.class)
  public void getMethodItem_WillThrowISE_WhenNameNotExist() {
    store.getMethodItem("foo.Foo#barMethod");
  }

  @Test
  public void getMethodItem_CanGetMethodItem() {
    WjrClassItem classItem = new WjrClassItem("foo.Foo");
    store.addClassItem(classItem);

    WjrMethodItem methodItem = new WjrMethodItem("foo.Foo", "barMethod");
    store.addMethodItem(methodItem);
    assertThat(store.getMethodItem("foo.Foo#barMethod"), is(methodItem));
  }

  @Test
  public void getClassItems_WillReturnEmptyList_WhenNoItemsExist() {
    assertTrue(store.getClassItems().isEmpty());
  }

  @Test
  public void getClassItems_CanReturnSortedList() {
    WjrClassItem classItem1 = new WjrClassItem("foo.Foo1");
    WjrClassItem classItem2 = new WjrClassItem("foo.Foo2");
    WjrClassItem classItem3 = new WjrClassItem("foo.Foo3");
    WjrClassItem classItem4 = new WjrClassItem("foo.Foo4");
    WjrClassItem classItem5 = new WjrClassItem("foo.Foo5");

    store.addClassItem(classItem1);
    store.addClassItem(classItem3);
    store.addClassItem(classItem2);
    store.addClassItem(classItem5);
    store.addClassItem(classItem4);

    List<WjrClassItem> items = store.getClassItems();
    assertThat(items.size(), is(5));
    assertThat(items.get(0), is(classItem1));
    assertThat(items.get(1), is(classItem2));
    assertThat(items.get(2), is(classItem3));
    assertThat(items.get(3), is(classItem4));
    assertThat(items.get(4), is(classItem5));
  }

  @Test
  public void getMethodItems_WillReturnEmptyList_WhenNoItemsExist() {
    assertTrue(store.getMethodItems().isEmpty());
  }

  @Test
  public void getMethodItems_CanReturnSortedList() {
    store.addClassItem(new WjrClassItem("foo.Foo1"));
    store.addClassItem(new WjrClassItem("foo.Foo2"));
    store.addClassItem(new WjrClassItem("foo.Foo3"));

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo1", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo1", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo1", "barMethod3");
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem1);

    List<WjrMethodItem> items = store.getMethodItems();
    assertThat(items.size(), is(3));
    assertThat(items.get(0), is(methodItem1));
    assertThat(items.get(1), is(methodItem2));
    assertThat(items.get(2), is(methodItem3));
  }

  @Test(expected = IllegalStateException.class)
  public void getMethodItems_WillThrowISE_WhenClassItemNotExist() {
    store.getMethodItems("foo.Foo");
  }

  @Test
  public void getMethodItems_WillReturnEmptyList_WhenNoMethodsExist() {
    store.addClassItem(new WjrClassItem("foo.Foo1"));
    store.addClassItem(new WjrClassItem("foo.Foo2"));
    store.addClassItem(new WjrClassItem("foo.Foo3"));

    assertTrue(store.getMethodItems("foo.Foo1").isEmpty());
  }

  @Test
  public void getMethodItems_CanReturnSortedList_InFirstParent() {
    store.addClassItem(new WjrClassItem("foo.Foo1"));
    store.addClassItem(new WjrClassItem("foo.Foo2"));
    store.addClassItem(new WjrClassItem("foo.Foo3"));

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo1", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo1", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo1", "barMethod3");
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem1);

    List<WjrMethodItem> items = store.getMethodItems("foo.Foo1");
    assertThat(items.size(), is(3));
    assertThat(items.get(0), is(methodItem1));
    assertThat(items.get(1), is(methodItem2));
    assertThat(items.get(2), is(methodItem3));
  }

  @Test
  public void getMethodItems_CanReturnSortedList_InSecondParent() {
    store.addClassItem(new WjrClassItem("foo.Foo1"));
    store.addClassItem(new WjrClassItem("foo.Foo2"));
    store.addClassItem(new WjrClassItem("foo.Foo3"));

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo2", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo2", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo2", "barMethod3");
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem1);

    List<WjrMethodItem> items = store.getMethodItems("foo.Foo2");
    assertThat(items.size(), is(3));
    assertThat(items.get(0), is(methodItem1));
    assertThat(items.get(1), is(methodItem2));
    assertThat(items.get(2), is(methodItem3));
  }

  @Test
  public void getMethodItems_CanReturnSortedList_InLastParent() {
    store.addClassItem(new WjrClassItem("foo.Foo1"));
    store.addClassItem(new WjrClassItem("foo.Foo2"));
    store.addClassItem(new WjrClassItem("foo.Foo3"));

    WjrMethodItem methodItem1 = new WjrMethodItem("foo.Foo3", "barMethod1");
    WjrMethodItem methodItem2 = new WjrMethodItem("foo.Foo3", "barMethod2");
    WjrMethodItem methodItem3 = new WjrMethodItem("foo.Foo3", "barMethod3");
    store.addMethodItem(methodItem3);
    store.addMethodItem(methodItem2);
    store.addMethodItem(methodItem1);

    List<WjrMethodItem> items = store.getMethodItems("foo.Foo3");
    assertThat(items.size(), is(3));
    assertThat(items.get(0), is(methodItem1));
    assertThat(items.get(1), is(methodItem2));
    assertThat(items.get(2), is(methodItem3));
  }

  @Test
  public void updateAllSummary_CanUpdateAllSummaries() {
    final StringBuilder called = new StringBuilder(4);

    store.addClassItem(new WjrClassItem("foo.Foo1") {
      @Override
      public void updateSummary(WjrStore param) {
        assertThat(param, is(store));
        called.append("1");
      }
    });
    store.addClassItem(new WjrClassItem("foo.Foo2") {
      @Override
      public void updateSummary(WjrStore param) {
        assertThat(param, is(store));
        called.append("2");
      }
    });
    store.addClassItem(new WjrClassItem("foo.Foo3") {
      @Override
      public void updateSummary(WjrStore param) {
        assertThat(param, is(store));
        called.append("3");
      }
    });
    store.root = new WjrStore.Root() {
      @Override
      public void updateSummary(WjrStore param) {
        assertThat(param, is(store));
        called.append("4");
      }
    };
    store.updateAllSummaries();
    assertThat(called.toString(), is("1234"));
  }

  @Test
  public void clearAllResultsAndSummaries_CanClearAllResultsAndSummaries() {
    final StringBuilder called = new StringBuilder(7);

    store.addClassItem(new WjrClassItem("foo.Foo1") {
      @Override
      public void clearSummary() {
        called.append("4");
      }
    });
    store.addClassItem(new WjrClassItem("foo.Foo2") {
      @Override
      public void clearSummary() {
        called.append("5");
      }
    });
    store.addClassItem(new WjrClassItem("foo.Foo3") {
      @Override
      public void clearSummary() {
        called.append("6");
      }
    });

    store.addMethodItem(new WjrMethodItem("foo.Foo1", "barMethod1") {
      @Override
      public void clearResult() {
        called.append("1");
      }
    });
    store.addMethodItem(new WjrMethodItem("foo.Foo1", "barMethod2") {
      @Override
      public void clearResult() {
        called.append("2");
      }
    });
    store.addMethodItem(new WjrMethodItem("foo.Foo2", "barMethod1") {
      @Override
      public void clearResult() {
        called.append("3");
      }
    });

    store.root = new WjrStore.Root() {
      @Override
      public void clearSummary() {
        called.append("7");
      }
    };
    store.clearAllResultsAndSummaries();
    assertThat(called.toString(), is("1234567"));
  }
}
