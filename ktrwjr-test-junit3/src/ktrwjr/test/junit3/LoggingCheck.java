package ktrwjr.test.junit3;

import java.util.logging.Logger;

import junit.framework.TestCase;

public class LoggingCheck extends TestCase {

  private static final Logger logger =
    Logger.getLogger(LoggingCheck.class.getSimpleName());

  public void testLog() {
    logger.finest("log finest");
    logger.finer("log finer");
    logger.config("log config");
    logger.info("log info");
    logger.warning("log warning");
    logger.severe("log severe");
  }
}
