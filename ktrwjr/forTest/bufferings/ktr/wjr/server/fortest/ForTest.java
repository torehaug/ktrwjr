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

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Ignore;
import org.junit.Test;

import com.google.apphosting.api.ApiProxy;

public class ForTest {
  private static final Logger logger = Logger
    .getLogger(ForTest.class.getName());

  @Test
  public void successMethod() {
    logger.info("successMethod");
  }

  @Test
  public void failureMethod() {
    logger.info("failureMethod");
    assertThat("", is("aaaa"));
  }

  @Test
  public void errorMethod() {
    logger.info("errorMethod");
    throw new RuntimeException();
  }

  @Test
  @Ignore
  public void ignoreMethod() {
    logger.info("ignoreMethod");
  }

  @Test
  public void overQuotaExceptionMethod() {
    logger.info("overQuotaExceptionMethod");
    throw new ApiProxy.OverQuotaException(
      ForTest.class.getPackage().getName(),
      ForTest.class.getSimpleName());
  }

  public static class ForTestInnerStatic {
    @Test
    public void successMethod() {
      logger.info("ForTestInner#successMethod");
    }

    @Test
    @Ignore
    public void ignoreMethod() {
      logger.info("ignoreMethod");
    }
  }

  public class ForTestInnerNotStatic {
    @Test
    public void successMethod() {
      logger.info("ForTestInner#successMethod");
    }
  }
}
