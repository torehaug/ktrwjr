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
package bufferings.ktr.wjr.server.fortest;

import java.util.logging.Logger;

import junit.framework.TestCase;

public class ForTestJUnit3 extends TestCase {
  private static final Logger logger =
    Logger.getLogger(ForTestJUnit3.class.getSimpleName());

  public void testSuccessMethod() {
    logger.info("testSuccessMethod");
  }

  public void testFailureMethod() {
    logger.info("testFailureMethod");
    assertTrue(false);
  }

  public void testErrorMethod() {
    logger.info("testErrorMethod");
    throw new RuntimeException();
  }

  public void ignoreMethod() {
    logger.info("ignoreMethod");
  }

  public int testHasReturnMethod() {
    logger.info("testHasReturnMethod");
    return 0;
  }

  public void testHasParamMethod(int a) {
    logger.info("testHasParamMethod");
  }

  public static class ForTestJUnit3InnerStatic extends TestCase {
    public void testSuccessMethod() {
      logger.info("ForTestJUnit3InnerStatic#testSuccessMethod");
    }

    public void ignoreMethod() {
      logger.info("ignoreMethod");
    }
  }

  public class ForTestJUnit3InnerNotStatic extends TestCase {
    public void testSuccessMethod() {
      logger.info("ForTestJUnit3InnerNotStatic#testSuccessMethod");
    }
  }
}
