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
package bufferings.ktr.wjr.server.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;
import org.slim3.util.AppEngineUtil;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.server.fortest.ForTestInherit;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrStoreLoaderTest {

  private WjrStoreLoader storeLoader = new WjrStoreLoader();

   @Test(expected = NullPointerException.class)
  public void loadWjrStore_WillThrowNPE_WhenSearchRootDirPathIsNull() {
    storeLoader.loadWjrStore(null);
  }

   @Test(expected = IllegalArgumentException.class)
  public void loadWjrStore_WillThrowIAE_WhenSearchRootDirPathDoesNotExist() {
     if (AppEngineUtil.isServer()) {
       storeLoader.loadWjrStore("NOTEXIST-WEB-INF/web.xml");
     } else {
       storeLoader.loadWjrStore("war/NOTEXIST-WEB-INF/web.xml");
     }
  }

   @Test(expected = IllegalArgumentException.class)
  public void loadWjrStore_WillThrowIAE_WhenSearchRootDirPathIsNotDir() {
    if (AppEngineUtil.isServer()) {
      storeLoader.loadWjrStore("WEB-INF/web.xml");
    } else {
      storeLoader.loadWjrStore("war/WEB-INF/web.xml");
    }
  }
  
  @Test
  public void loadWjrStore_CanLoadWjrStore() {
    WjrStore store;
    if (AppEngineUtil.isServer()) {
      store = storeLoader.loadWjrStore("WEB-INF/classes");
    } else {
      store = storeLoader.loadWjrStore("war/WEB-INF/classes");
    }
    for (WjrClassItem item : store.getClassItems()) {
      if (item.getClassName().equals(ForTest.class.getName())) {
        return;
      }
    }
    fail();
  }

  @Test
  public void findAndStoreTests_CanFindAndStoreTests() {
    WjrStore store = new WjrStore();
    if (AppEngineUtil.isServer()) {
      File parentDir = new File("WEB-INF/classes");
      storeLoader.findAndStoreTests(store, "WEB-INF/classes", parentDir);
    } else {
      File parentDir = new File("war/WEB-INF/classes");
      storeLoader.findAndStoreTests(store, "war/WEB-INF/classes", parentDir);
    }
    for (WjrClassItem item : store.getClassItems()) {
      if (item.getClassName().equals(ForTest.class.getName())) {
        return;
      }
    }
    fail();
  }

  @Test
  public void listClassFiles_CanListClassFiles() {
    File parentDir;
    if (AppEngineUtil.isServer()) {
      parentDir = new File("WEB-INF/classes/bufferings/ktr/wjr/server/fortest");
    } else {
      parentDir =
        new File("war/WEB-INF/classes/bufferings/ktr/wjr/server/fortest");
    }
    File[] children = storeLoader.listClassFiles(parentDir);
    assertThat(children.length, is(4));
    // ForTest,ForTestInnerStatic,ForTestInnerNotStatic,ForTestInherit
    for (File child : children) {
      assertThat(child.isFile(), is(true));
    }
  }

  @Test
  public void listDirectories_CanListDirectories() {
    File parentDir;
    if (AppEngineUtil.isServer()) {
      parentDir = new File("WEB-INF/classes/bufferings/ktr/wjr/server");
    } else {
      parentDir = new File("war/WEB-INF/classes/bufferings/ktr/wjr/server");
    }
    File[] children = storeLoader.listDirectories(parentDir);
    assertThat(children.length, is(4));
    assertThat(children[0].isDirectory(), is(true));
    assertThat(children[1].isDirectory(), is(true));
    assertThat(children[2].isDirectory(), is(true));
    assertThat(children[3].isDirectory(), is(true));
  }

  @Test
  public void getClassNameFromClassFile_CanGetName() {
    if (AppEngineUtil.isServer()) {
      File classFile =
        new File(
          "WEB-INF/classes/bufferings/ktr/wjr/server/fortest/ForTest.class");
      String ret =
        storeLoader.getClassNameFromClassFile("WEB-INF/classes", classFile);
      assertThat(ret, is(ForTest.class.getName()));
    } else {
      File classFile =
        new File(
          "war/WEB-INF/classes/bufferings/ktr/wjr/server/fortest/ForTest.class");
      String ret =
        storeLoader.getClassNameFromClassFile("war/WEB-INF/classes", classFile);
      assertThat(ret, is(ForTest.class.getName()));
    }
  }

  @Test
  public void getClassNameFromClassFile_CanGetName_WithInnerStaticClass() {
    if (AppEngineUtil.isServer()) {
      File classFile =
        new File(
          "WEB-INF/classes/bufferings/ktr/wjr/server/fortest/ForTest$ForTestInnerStatic.class");
      String ret =
        storeLoader.getClassNameFromClassFile("WEB-INF/classes", classFile);
      assertThat(ret, is(ForTest.ForTestInnerStatic.class.getName()));
    } else {
      File classFile =
        new File(
          "war/WEB-INF/classes/bufferings/ktr/wjr/server/fortest/ForTest$ForTestInnerStatic.class");
      String ret =
        storeLoader.getClassNameFromClassFile("war/WEB-INF/classes", classFile);
      assertThat(ret, is(ForTest.ForTestInnerStatic.class.getName()));
    }
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithCommonClass() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTest.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTest.class.getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTest.class.getName());
    assertThat(methodItems.size(), is(3));
    assertThat(methodItems.get(0).getMethodName(), is("errorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("failureMethod"));
    assertThat(methodItems.get(2).getMethodName(), is("successMethod"));
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithInheritClass() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTestInherit.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTestInherit.class
      .getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTestInherit.class.getName());
    assertThat(methodItems.size(), is(3));
    assertThat(methodItems.get(0).getMethodName(), is("errorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("failureMethod"));
    assertThat(methodItems.get(2).getMethodName(), is("successMethod"));
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithInnerStaticClass() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTest.ForTestInnerStatic.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(
      classItems.get(0).getClassName(),
      is(ForTest.ForTestInnerStatic.class.getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTest.ForTestInnerStatic.class.getName());
    assertThat(methodItems.size(), is(1));
    assertThat(methodItems.get(0).getMethodName(), is("successMethod"));
  }

  @Test
  public void isTargetClass_WillReturnTrue_WithCommonClass() throws Exception {
    assertThat(storeLoader.isTargetClass(ForTest.class), is(true));
  }

  @Test
  public void isTargetClass_WillReturnTrue_WithInnerStaticClass()
      throws Exception {
    assertThat(
      storeLoader.isTargetClass(ForTest.ForTestInnerStatic.class),
      is(true));
  }

  @Test
  public void isTargetClass_WillReturnFlase_WithInnerNotStaticClass()
      throws Exception {
    assertThat(
      storeLoader.isTargetClass(ForTest.ForTestInnerNotStatic.class),
      is(false));
  }

  @Test
  public void isTargetMethod_WillReturnTrue_WithTestAnnotation()
      throws Exception {
    Method m = ForTest.class.getMethod("successMethod");
    assertThat(storeLoader.isTargetMethod(m), is(true));
  }

  @Test
  public void isTargetMethod_WillReturnFalse_WithIgnoreAnnotation()
      throws Exception {
    Method m = ForTest.class.getMethod("ignoreMethod");
    assertThat(storeLoader.isTargetMethod(m), is(false));
  }

}
