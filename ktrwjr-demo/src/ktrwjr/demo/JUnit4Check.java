package ktrwjr.demo;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class JUnit4Check {

  private static final Logger logger =
    Logger.getLogger(JUnit4Check.class.getName());

  @BeforeClass
  public static void setUpBeforeClass() {
    logger.info("setUpBeforeClass");
  }

  @AfterClass
  public static void tearDownAfterClass() {
    logger.info("tearDownAfterClass");
  }

  @Before
  public void setUp() {
    logger.info("setUp");
  }

  @After
  public void tearDown() {
    logger.info("tearDown");
  }

  @Test
  public void willSucceed() {
    logger.info("willSucceed");
    assertThat(true, is(true));
  }

  @Test
  public void willFail() {
    logger.info("willFail");
    assertThat(true, is(false));
  }

  @Test
  public void willError() {
    logger.info("willError");
    throw new RuntimeException();
  }

  @Test
  @Ignore
  public void willIgnore() {
    logger.info("willIgnore");
  }

  public static class InnerStaticCheck {
    @Test
    public void willSucceed() {
      logger.info("willSucceed");
      assertThat(true, is(true));
    }
  }

  public class InnerNotStaticCheck {
    @Test
    public void willSucceed() {
      logger.info("willSucceed");
      assertThat(true, is(true));
    }
  }
}
