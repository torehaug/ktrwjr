package ktrwjr.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.logging.Logger;

import org.junit.Test;

import com.google.appengine.api.utils.SystemProperty;

public class DEEAndHDECheck {
  private static final Logger logger =
    Logger.getLogger(DEEAndHDECheck.class.getSimpleName());

  @Test
  public void waitForDEE() throws Exception {
    assertThat(
      SystemProperty.environment.value(),
      is(SystemProperty.Environment.Value.Production));
    
    try{
      Thread.sleep(40000);
    } catch (Exception e) {
      logger.info("[catch]" + e.toString());
      throw e;
    } finally {
      logger.info("[finally]");
    }
  }

  @Test
  public void waitForHDE() throws Exception {
    assertThat(
      SystemProperty.environment.value(),
      is(SystemProperty.Environment.Value.Production));
    nestCatchWait(100, 10);
  }

  private void nestCatchWait(int nestCount, long wait) throws Exception {
    try {
      if (--nestCount <= 0) {
        Thread.sleep(40000);
      } else {
        nestCatchWait(nestCount, wait);
      }
    } catch (Exception e) {
      logger.info("[catch]" + e.toString());
      throw e;
    } finally {
      logger.info("[finally]");
      Thread.sleep(wait);
    }
  }

}
