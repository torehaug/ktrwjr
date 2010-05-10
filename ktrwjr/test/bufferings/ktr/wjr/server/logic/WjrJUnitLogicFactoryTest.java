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

import org.junit.After;
import org.junit.Test;


public class WjrJUnitLogicFactoryTest {

  @After
  public void tearDown(){
    WjrJUnitLogicFactory.versionChecker = null;
  }
  
  @Test(expected = IllegalStateException.class)
  public void getMethodRunner_WillThrowISE_WhenJUnitIsNotAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return false;
      }

      @Override
      public boolean isJUnit4Available() {
        return false;
      }
    };

    WjrJUnitLogicFactory.getMethodRunner();
  }

  @Test
  public void getMethodRunner_WillReturnForJUnit4_WhenJUnit4IsAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return true;
      }

      @Override
      public boolean isJUnit4Available() {
        return true;
      }
    };

    assertThat(
      WjrJUnitLogicFactory.getMethodRunner() instanceof WjrJUnit4MethodRunner,
      is(true));
  }

  @Test
  public void getMethodRunner_WillReturnForJUnit3_WhenJUnit4IsNotAvailableAndJUnit3IsAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return true;
      }

      @Override
      public boolean isJUnit4Available() {
        return false;
      }
    };

    assertThat(
      WjrJUnitLogicFactory.getMethodRunner() instanceof WjrJUnit3MethodRunner,
      is(true));
  }

  @Test(expected = IllegalStateException.class)
  public void getStoreLoader_WillThrowISE_WhenJUnitIsNotAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return false;
      }

      @Override
      public boolean isJUnit4Available() {
        return false;
      }
    };

    WjrJUnitLogicFactory.getStoreLoader();
  }

  @Test
  public void getStoreLoader_WillReturnForJUnit4_WhenJUnit4IsAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return true;
      }

      @Override
      public boolean isJUnit4Available() {
        return true;
      }
    };

    assertThat(
      WjrJUnitLogicFactory.getStoreLoader() instanceof WjrJUnit4StoreLoader,
      is(true));
  }

  @Test
  public void getStoreLoader_WillReturnForJUnit3_WhenJUnit4IsNotAvailableAndJUnit3IsAvailable() {
    WjrJUnitLogicFactory.versionChecker = new WjrJUnitVersionChecker() {
      @Override
      public boolean isJUnit3Available() {
        return true;
      }

      @Override
      public boolean isJUnit4Available() {
        return false;
      }
    };

    assertThat(
      WjrJUnitLogicFactory.getStoreLoader() instanceof WjrJUnit3StoreLoader,
      is(true));
  }
}
