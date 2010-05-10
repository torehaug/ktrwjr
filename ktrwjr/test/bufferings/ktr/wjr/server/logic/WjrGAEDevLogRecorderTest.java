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

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WjrGAEDevLogRecorderTest {

  private WjrGAEDevLogRecorder.DevServerLogHandler prev;

  @Before
  public void setUp() {
    prev = WjrGAEDevLogRecorder.devServerLogHandler;
  }

  @After
  public void tearDown() {
    WjrGAEDevLogRecorder.devServerLogHandler = prev;
  }

  @Test
  public void startBare_WillSetHandleAndRecorder() {
    WjrGAEDevLogRecorder.devServerLogHandler = null;
    WjrGAEDevLogRecorder recorder = new WjrGAEDevLogRecorder();

    assertThat(Arrays.asList(Logger.getLogger("").getHandlers()).contains(
      WjrGAEDevLogRecorder.devServerLogHandler), is(false));

    try {
      recorder.startBare();
      assertThat(Arrays.asList(Logger.getLogger("").getHandlers()).contains(
        WjrGAEDevLogRecorder.devServerLogHandler), is(true));
      assertThat(WjrGAEDevLogRecorder.devServerLogHandler, is(not(nullValue())));
      assertThat(
        WjrGAEDevLogRecorder.devServerLogHandler.recorder,
        is(recorder));
    } finally {
      Logger.getLogger("").removeHandler(
        WjrGAEDevLogRecorder.devServerLogHandler);
      WjrGAEDevLogRecorder.devServerLogHandler = null;
    }
  }

  @Test
  public void startBare_WillSetHandleRecorder_WhenHandlerIsNotNull() {
    WjrGAEDevLogRecorder.devServerLogHandler =
      new WjrGAEDevLogRecorder.DevServerLogHandler();
    WjrGAEDevLogRecorder recorder = new WjrGAEDevLogRecorder();

    recorder.startBare();
    assertThat(WjrGAEDevLogRecorder.devServerLogHandler, is(not(nullValue())));
    assertThat(WjrGAEDevLogRecorder.devServerLogHandler.recorder, is(recorder));
  }

  @Test
  public void stopBare_WillClearHandleRecorder() {
    WjrGAEDevLogRecorder.devServerLogHandler =
      new WjrGAEDevLogRecorder.DevServerLogHandler();
    WjrGAEDevLogRecorder recorder = new WjrGAEDevLogRecorder();

    recorder.stopBare();
    assertThat(WjrGAEDevLogRecorder.devServerLogHandler, is(not(nullValue())));
    assertThat(
      WjrGAEDevLogRecorder.devServerLogHandler.recorder,
      is(nullValue()));
  }

  @SuppressWarnings("serial")
  @Test
  public void formatLog() {
    GregorianCalendar calendar =
      new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    calendar.clear();
    calendar.set(2000, 11, 31, 13, 15);

    final long myTime = calendar.getTimeInMillis();
    final String myMsg = "Message";
    final Level myLevel = Level.INFO;
    final String mySourceClassName = "SourceClassName";
    final String mySourceMethodName = "SourceMethodName";

    LogRecord logRecord = new LogRecord(myLevel, myMsg) {
      @Override
      public String getSourceClassName() {
        return mySourceClassName;
      }

      @Override
      public String getSourceMethodName() {
        return mySourceMethodName;
      }

    };
    logRecord.setMillis(myTime);

    final StringBuilder called = new StringBuilder();
    WjrGAEDevLogRecorder recorder = new WjrGAEDevLogRecorder() {
      @Override
      protected String formatLog(long millis, String level, String msg) {
        assertThat(millis, is(myTime));
        assertThat(level, is(myLevel.getName()));
        assertThat(msg, is(mySourceClassName
          + " "
          + mySourceMethodName
          + ": "
          + myMsg));
        called.append("1");
        return super.formatLog(millis, level, msg);
      }
    };

    recorder.formatLog(logRecord);
    assertThat(called.toString(), is("1"));
  }

  @Test
  public void canRecordLog() {
    WjrGAEDevLogRecorder.devServerLogHandler = null;
    WjrGAEDevLogRecorder recorder = new WjrGAEDevLogRecorder();

    try {
      recorder.startRecording("GMT");
      recorder.stopRecording();
      assertThat(recorder.log.toString(), is(""));

      Logger logger = Logger.getLogger(this.getClass().getName());
      recorder.startRecording("GMT");
      logger.info("Sample");
      recorder.stopRecording();
      assertThat(recorder.log.toString(), is(not("")));
    } finally {
      Logger.getLogger("").removeHandler(
        WjrGAEDevLogRecorder.devServerLogHandler);
      WjrGAEDevLogRecorder.devServerLogHandler = null;
    }
  }

}
