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
package bufferings.ktr.wjr.server.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.File;
import java.lang.reflect.Method;

import org.junit.Test;
import org.slim3.util.AppEngineUtil;

import bufferings.ktr.wjr.server.service.fortest.ForTest;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrStoreLoaderTest {
//
//  private WjrStoreLoader wjrStoreLoader = new WjrStoreLoader();
//
//  @Test(expected = NullPointerException.class)
//  public void loadWjrStore_WillThrowNPE_WhenClassesDirPathIsNull() {
//    wjrStoreLoader.loadWjrStore(null);
//  }
//
//  @Test
//  public void loadWjrStore_CanLoadWjrStore() {
//    WjrStore wjrStore;
//    if (AppEngineUtil.isServer()) {
//      wjrStore = wjrStoreLoader.loadWjrStore("WEB-INF/classes");
//    } else {
//      wjrStore = wjrStoreLoader.loadWjrStore("war/WEB-INF/classes");
//    }
//    for (WjrClassItem item : wjrStore.getFilteredClassItems()) {
//      if (item.getClassCanonicalName().equals(ForTest.class.getCanonicalName())) {
//        return;
//      }
//    }
//    fail();
//  }
//
//  @Test
//  public void findAndStoreTests_CanFindAndStoreTests() {
//    WjrStore wjrStore = new WjrStore();
//    if (AppEngineUtil.isServer()) {
//      File parentDir = new File("WEB-INF/classes");
//      wjrStoreLoader.findAndStoreTests(wjrStore, "WEB-INF/classes", parentDir);
//    } else {
//      File parentDir = new File("war/WEB-INF/classes");
//      wjrStoreLoader.findAndStoreTests(
//        wjrStore,
//        "war/WEB-INF/classes",
//        parentDir);
//    }
//    for (WjrClassItem item : wjrStore.getFilteredClassItems()) {
//      if (item.getClassCanonicalName().equals(ForTest.class.getCanonicalName())) {
//        return;
//      }
//    }
//    fail();
//  }
//
//  @Test
//  public void listClassFiles_CanListClassFiles() {
//    File parentDir;
//    if (AppEngineUtil.isServer()) {
//      parentDir =
//        new File("WEB-INF/classes/bufferings/ktr/wjr/server/service/fortest");
//    } else {
//      parentDir =
//        new File(
//          "war/WEB-INF/classes/bufferings/ktr/wjr/server/service/fortest");
//    }
//    File[] children = wjrStoreLoader.listClassFiles(parentDir);
//    assertThat(children.length, is(1));
//    assertThat(children[0].isDirectory(), is(false));
//    assertThat(children[0].getName(), is("ForTest.class"));
//  }
//
//  @Test
//  public void listDirectories_CanListDirectories() {
//    File parentDir;
//    if (AppEngineUtil.isServer()) {
//      parentDir = new File("WEB-INF/classes/bufferings/ktr/wjr/server/service");
//    } else {
//      parentDir =
//        new File("war/WEB-INF/classes/bufferings/ktr/wjr/server/service");
//    }
//    File[] children = wjrStoreLoader.listDirectories(parentDir);
//    assertThat(children.length, is(1));
//    assertThat(children[0].isDirectory(), is(true));
//  }
//
//  @Test
//  public void getClassCanonicalNameFromClassFile_CanGetName() {
//    if (AppEngineUtil.isServer()) {
//      File classFile =
//        new File(
//          "WEB-INF/classes/bufferings/ktr/wjr/server/service/fortest/ForTest.class");
//      String ret =
//        wjrStoreLoader.getClassCanonicalNameFromClassFile(
//          "WEB-INF/classes",
//          classFile);
//      assertThat(ret, is(ForTest.class.getCanonicalName()));
//    } else {
//      File classFile =
//        new File(
//          "war/WEB-INF/classes/bufferings/ktr/wjr/server/service/fortest/ForTest.class");
//      String ret =
//        wjrStoreLoader.getClassCanonicalNameFromClassFile(
//          "war/WEB-INF/classes",
//          classFile);
//      assertThat(ret, is(ForTest.class.getCanonicalName()));
//    }
//  }
//
//  @Test
//  public void loadClass_CanLoadClass() {
//    assertThat(
//      wjrStoreLoader.loadClass(ForTest.class.getCanonicalName()),
//      is(not(nullValue())));
//  }
//
//  @Test(expected = RuntimeException.class)
//  public void loadClass_WillThrowRTE_WhenCannotLoadClass() {
//    wjrStoreLoader.loadClass(ForTest.class.getCanonicalName() + "XXX");
//  }
//
//  @Test
//  public void checkAndStoreTestClass_CanStoreTest() {
//    WjrStore wjrStore = new WjrStore();
//    wjrStoreLoader.checkAndStoreTestClass(wjrStore, ForTest.class);
//
//    assertThat(wjrStore.getFilteredClassItems().size(), is(1));
//    assertThat(
//      wjrStore.getFilteredClassItems().get(0).getClassCanonicalName(),
//      is(ForTest.class.getCanonicalName()));
//    assertThat(wjrStore
//      .getFilteredMethodItems(ForTest.class.getCanonicalName())
//      .size(), is(1));
//    assertThat(wjrStore
//      .getFilteredMethodItems(ForTest.class.getCanonicalName())
//      .get(0)
//      .getMethodSimpleName(), is("normalMethod"));
//  }
//
//  @Test
//  public void isTargetMethod_WillReturnTrue() throws Exception {
//    Method m = ForTest.class.getMethod("normalMethod");
//    assertThat(wjrStoreLoader.isTargetMethod(m), is(true));
//  }
//
//  @Test
//  public void isTargetMethod_WillReturnFalse_WithIgnoreAnnotation()
//      throws Exception {
//    Method m = ForTest.class.getMethod("ignoreMethod");
//    assertThat(wjrStoreLoader.isTargetMethod(m), is(false));
//  }

}
