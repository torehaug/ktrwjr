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

import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.LogRecord;
import com.google.apphosting.api.ApiProxy.LogRecord.Level;

public class WjrGAEProdLogRecorderTest {

  @SuppressWarnings("unchecked")
  private ApiProxy.Delegate prevDelegate;

  @Before
  public void setUp() {
    prevDelegate = ApiProxy.getDelegate();
  }

  @After
  public void tearDown() {
    ApiProxy.setDelegate(prevDelegate);
  }

  @SuppressWarnings("unchecked")
  @Test
  public void startBare_WillChangeDelegate() {
    ApiProxy.Delegate delegate = ApiProxy.getDelegate();
    WjrGAEProdLogRecorder recorder = new WjrGAEProdLogRecorder();
    recorder.recording = false;
    recorder.startBare();

    assertThat(recorder.originalDelegate, is(delegate));
    assertThat(ApiProxy.getDelegate(), is(not(delegate)));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void stopBare_WillRestoreDelegate() {
    ApiProxy.Delegate delegate = ApiProxy.getDelegate();
    WjrGAEProdLogRecorder recorder = new WjrGAEProdLogRecorder();
    recorder.recording = false;
    recorder.startBare();

    assertThat(recorder.originalDelegate, is(delegate));
    assertThat(ApiProxy.getDelegate(), is(not(delegate)));

    recorder.stopBare();

    assertThat(recorder.originalDelegate, is(nullValue()));
    assertThat(ApiProxy.getDelegate(), is(delegate));
  }

  @Test
  public void formatLog() {
    GregorianCalendar calendar =
      new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    calendar.clear();
    calendar.set(2000, 11, 31, 13, 15);

    final long myTime = calendar.getTimeInMillis();
    final String myMsg = "Message";
    final Level myLevel = Level.info;

    // The timestamp of GAE LogRecord unit is microsecond.
    LogRecord logRecord = new LogRecord(myLevel, myTime * 1000, myMsg);

    final StringBuilder called = new StringBuilder();
    WjrGAEProdLogRecorder recorder = new WjrGAEProdLogRecorder() {
      @Override
      protected String formatLog(long millis, String level, String msg) {
        assertThat(millis, is(myTime));
        assertThat(level, is(myLevel.name()));
        assertThat(msg, is(myMsg));
        called.append("1");
        return super.formatLog(millis, level, msg);
      }
    };

    recorder.formatLog(logRecord);
    assertThat(called.toString(), is("1"));
  }

}
