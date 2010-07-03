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

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Ignore;
import org.junit.Test;

import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

/**
 * The loader of the WjrStore for JUnit4.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrJUnit4StoreLoader extends WjrJUnit3StoreLoader {

  /**
   * Checks if the class is the target one and has test method(s), then stores
   * it.
   * 
   * @param store
   *          The store.
   * @param clazz
   *          The class.
   */
  protected void checkAndStoreTestClass(WjrStore store, Class<?> clazz) {
    WjrClassItem classItem = null;
    if (isJUnit4TargetClass(clazz)) {
      Method[] methods = clazz.getMethods();
      for (Method m : methods) {
        if (isJUnit4TargetMethod(m)) {
          if (classItem == null) {
            classItem = new WjrClassItem(clazz.getName());
            store.addClassItem(classItem);
          }
          store.addMethodItem(new WjrMethodItem(clazz.getName(), m.getName()));
        }
      }
    }

    if (classItem == null) {
      super.checkAndStoreTestClass(store, clazz);
    }
  }

  /**
   * Checks if the class is a target class or not.
   * 
   * If the class is not member class or static inner class then returns true,
   * otherwise returns false.
   * 
   * @param clazz
   *          The class to check.
   * @return If the class is not member class or static inner class then returns
   *         true, otherwise returns false.
   */
  protected boolean isJUnit4TargetClass(Class<?> clazz) {
    if (!clazz.isMemberClass()) {
      return true;
    }

    if ((clazz.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
      return true;
    }

    return false;
  }

  /**
   * Checks if the method is a target method or not.
   * 
   * If the method's modifier is public only, has Test annotation and doesn't
   * have the Ignore annotation, then returns true, otherwise returns false.
   * 
   * @param method
   *          The method to check.
   * @return If the method's modifier is public only, has Test annotation and
   *         doesn't have the Ignore annotation, then returns true, otherwise
   *         returns false.
   */
  protected boolean isJUnit4TargetMethod(Method method) {
    return (method.getModifiers() == Modifier.PUBLIC
      && method.getAnnotation(Test.class) != null && method
      .getAnnotation(Ignore.class) == null);
  }
}
