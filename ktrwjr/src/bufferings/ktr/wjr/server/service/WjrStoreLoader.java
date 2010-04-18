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

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.io.File;
import java.io.FileFilter;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import org.junit.Ignore;
import org.junit.Test;

import bufferings.ktr.wjr.shared.model.WjrClassItem;
import bufferings.ktr.wjr.shared.model.WjrMethodItem;
import bufferings.ktr.wjr.shared.model.WjrStore;

public class WjrStoreLoader {

  protected static final String CLASSFILE_EXT = ".class";

  protected static final String INNER_CLASS_SEPARATOR = "$";

  protected static final String FILE_SEPARATOR_REGEX = "[\\\\/]";

  protected static final String PACKAGE_SEPARATOR = ".";

  public WjrStore loadWjrStore(String classesDirPath) {
    checkNotNull(classesDirPath, "The classesDirPath parameter is null.");

    File classesDir = new File(classesDirPath);
    checkArgument(classesDir.exists(), "%s not exists.", classesDirPath);
    checkArgument(
      classesDir.isDirectory(),
      "%s isn't a directory.",
      classesDirPath);

    WjrStore wjrStore = new WjrStore();
    findAndStoreTests(wjrStore, classesDirPath, classesDir);
    wjrStore.updateAllSummaries();
    return wjrStore;
  }

  protected void findAndStoreTests(WjrStore store, String classesDirPath,
      File targetDir) {
    File[] classFiles = listClassFiles(targetDir);
    for (File classFile : classFiles) {
      String classCanonicalName =
        getClassCanonicalNameFromClassFile(classesDirPath, classFile);
      Class<?> clazz = loadClass(classCanonicalName);
      checkAndStoreTestClass(store, clazz);
    }

    File[] dirs = listDirectories(targetDir);
    for (File dir : dirs) {
      findAndStoreTests(store, classesDirPath, dir);
    }
  }

  protected File[] listClassFiles(File dir) {
    return dir.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        return !pathname.isDirectory()
          && pathname.getName().endsWith(CLASSFILE_EXT)
          && !pathname.getName().contains(INNER_CLASS_SEPARATOR);
      }
    });
  }

  protected File[] listDirectories(File dir) {
    return dir.listFiles(new FileFilter() {
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    });
  }

  protected String getClassCanonicalNameFromClassFile(String classesDirPath,
      File classFile) {
    String path = classFile.getPath();

    int beginIndex = classesDirPath.length() + 1;
    int endIndex = path.length() - CLASSFILE_EXT.length();
    return path.substring(beginIndex, endIndex).replaceAll(
      FILE_SEPARATOR_REGEX,
      PACKAGE_SEPARATOR);
  }

  protected Class<?> loadClass(String classCanonicalName) {
    try {
      return Thread.currentThread().getContextClassLoader().loadClass(
        classCanonicalName);
    } catch (ClassNotFoundException e1) {
      throw new RuntimeException("Cannot load class("
        + classCanonicalName
        + ").", e1);
    }
  }

  protected void checkAndStoreTestClass(WjrStore store, Class<?> clazz) {
    WjrClassItem classItem = null;
    Method[] methods = clazz.getMethods();
    for (Method m : methods) {
      if (isTargetMethod(m)) {
        if (classItem == null) {
          classItem = new WjrClassItem(clazz.getCanonicalName());
          store.addClassItem(classItem);
        }
        store.addMethodItem(new WjrMethodItem(clazz.getCanonicalName(), m
          .getName()));
      }
    }
  }

  protected boolean isTargetMethod(Method m) {
    return (m.getModifiers() == Modifier.PUBLIC
      && m.getAnnotation(Test.class) != null && m.getAnnotation(Ignore.class) == null);
  }
}
