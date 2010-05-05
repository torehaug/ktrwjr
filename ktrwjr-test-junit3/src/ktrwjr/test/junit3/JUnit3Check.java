package ktrwjr.test.junit3;

import java.util.logging.Logger;

import junit.framework.TestCase;

public class JUnit3Check extends TestCase {

  private static final Logger logger =
    Logger.getLogger(JUnit3Check.class.getSimpleName());

  public void setUp() {
    logger.info("setUp");
  }

  public void tearDown() {
    logger.info("tearDown");
  }

  public void testWillSucceed() {
    logger.info("willSucceed");
    assertEquals(true, true);
  }

  public void testWillFail() {
    logger.info("willFail");
    assertEquals(true, false);
  }

  public void testWillError() {
    logger.info("willError");
    throw new RuntimeException();
  }

  // not "test" prefix
  public void willIgnore() {
    logger.info("willIgnore");
  }

  public static class InnerStaticCheck {
    public void testWillSucceed() {
      logger.info("willSucceed");
      assertEquals(true, true);
    }
  }

  public class InnerNotStaticCheck {
    public void testWillSucceed() {
      logger.info("willSucceed");
      assertEquals(true, true);
    }
  }
}
