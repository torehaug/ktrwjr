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

import java.lang.reflect.Method;
import java.util.List;

import org.junit.Test;

import bufferings.ktr.wjr.server.fortest.ForTest;
import bufferings.ktr.wjr.server.fortest.ForTestInherit;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3;
import bufferings.ktr.wjr.server.fortest.ForTestJUnit3Inherit;
import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrJUnit4StoreLoaderTest {

  private WjrJUnit4StoreLoader storeLoader = new WjrJUnit4StoreLoader();

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithCommonClass_JUnit4() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTest.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTest.class.getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTest.class.getName());
    assertThat(methodItems.size(), is(4));
    assertThat(methodItems.get(0).getMethodName(), is("errorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("failureMethod"));
    assertThat(
      methodItems.get(2).getMethodName(),
      is("overQuotaExceptionMethod"));
    assertThat(methodItems.get(3).getMethodName(), is("successMethod"));

  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithCommonClass_JUnit3() {

    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTestJUnit3.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTestJUnit3.class
      .getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTestJUnit3.class.getName());
    assertThat(methodItems.size(), is(4));
    assertThat(methodItems.get(0).getMethodName(), is("testErrorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("testFailureMethod"));
    assertThat(
      methodItems.get(2).getMethodName(),
      is("testOverQuotaExceptionMethod"));
    assertThat(methodItems.get(3).getMethodName(), is("testSuccessMethod"));
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithInheritClass_JUnit4() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTestInherit.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTestInherit.class
      .getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTestInherit.class.getName());
    assertThat(methodItems.size(), is(4));
    assertThat(methodItems.get(0).getMethodName(), is("errorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("failureMethod"));
    assertThat(
      methodItems.get(2).getMethodName(),
      is("overQuotaExceptionMethod"));
    assertThat(methodItems.get(3).getMethodName(), is("successMethod"));
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithInheritClass_JUnit3() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(store, ForTestJUnit3Inherit.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(classItems.get(0).getClassName(), is(ForTestJUnit3Inherit.class
      .getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTestJUnit3Inherit.class.getName());
    assertThat(methodItems.size(), is(4));
    assertThat(methodItems.get(0).getMethodName(), is("testErrorMethod"));
    assertThat(methodItems.get(1).getMethodName(), is("testFailureMethod"));
    assertThat(
      methodItems.get(2).getMethodName(),
      is("testOverQuotaExceptionMethod"));
    assertThat(methodItems.get(3).getMethodName(), is("testSuccessMethod"));
  }

  @Test
  public void checkAndStoreTestClass_CanStoreTest_WithInnerStaticClass_JUnit4() {
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
  public void checkAndStoreTestClass_CanStoreTest_WithInnerStaticClass_JUnit3() {
    WjrStore store = new WjrStore();
    storeLoader.checkAndStoreTestClass(
      store,
      ForTestJUnit3.ForTestJUnit3InnerStatic.class);

    List<WjrClassItem> classItems = store.getClassItems();
    assertThat(classItems.size(), is(1));
    assertThat(
      classItems.get(0).getClassName(),
      is(ForTestJUnit3.ForTestJUnit3InnerStatic.class.getName()));

    List<WjrMethodItem> methodItems =
      store.getMethodItems(ForTestJUnit3.ForTestJUnit3InnerStatic.class
        .getName());
    assertThat(methodItems.size(), is(1));
    assertThat(methodItems.get(0).getMethodName(), is("testSuccessMethod"));
  }

  @Test
  public void isJUnit4TargetClass_WillReturnTrue_WithCommonClass()
      throws Exception {
    assertThat(storeLoader.isJUnit4TargetClass(ForTest.class), is(true));
  }

  @Test
  public void isJUnit4TargetClass_WillReturnTrue_WithInnerStaticClass()
      throws Exception {
    assertThat(storeLoader
      .isJUnit4TargetClass(ForTest.ForTestInnerStatic.class), is(true));
  }

  @Test
  public void isJUnit4TargetClass_WillReturnFlase_WithInnerNotStaticClass()
      throws Exception {
    assertThat(storeLoader
      .isJUnit4TargetClass(ForTest.ForTestInnerNotStatic.class), is(false));
  }

  @Test
  public void isJUnit4TargetMethod_WillReturnTrue_WithTestAnnotation()
      throws Exception {
    Method m = ForTest.class.getMethod("successMethod");
    assertThat(storeLoader.isJUnit4TargetMethod(m), is(true));
  }

  @Test
  public void isJUnit4TargetMethod_WillReturnFalse_WithIgnoreAnnotation()
      throws Exception {
    Method m = ForTest.class.getMethod("ignoreMethod");
    assertThat(storeLoader.isJUnit4TargetMethod(m), is(false));
  }

}
