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

import org.junit.Test;
import org.slim3.util.AppEngineUtil;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrStoreLoaderTest {

  private WjrStoreLoader storeLoader = new WjrStoreLoader() {
    @Override
    protected void checkAndStoreTestClass(WjrStore store, Class<?> clazz) {
      store.addClassItem(new WjrClassItem(clazz.getName()));
    }
  };

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
    assertThat(children.length, is(8));
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
}
