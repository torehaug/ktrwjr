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

import org.junit.Test;

public class WjrGAELogRecorderTest {

  public class WjrGAELogRecorderImpl extends WjrGAELogRecorder {
    protected void startBare() {
    }

    protected void stopBare() {
    }
  }

  @Test(expected = IllegalStateException.class)
  public void startRecording_WillThrowISE_WhenRecording() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recording = true;
    recorder.startRecording("JST");
  }

  @Test
  public void startRecording_CanStartRecording() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recording = false;
    recorder.startRecording("JST");

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.dateFormat.getTimeZone(), is(TimeZone
      .getTimeZone("JST")));
    assertThat(recorder.log.toString(), is(""));
  }

  @Test
  public void startRecording_CanStartRecording_WithTimeSupported() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recording = false;
    recorder.startRecording("JST");

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.dateFormat.getTimeZone(), is(TimeZone
      .getTimeZone("JST")));
    assertThat(recorder.log.toString(), is(""));
  }

  @Test(expected = IllegalStateException.class)
  public void stopRecording_WillThrowISE_WhenNotRecording() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recording = false;
    recorder.stopRecording();
  }

  @Test
  public void stopRecording_CanStopRecording() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.startRecording("PST");
    recorder.stopRecording();

    assertThat(recorder.recording, is(false));
    assertThat(recorder.recorded, is(true));
  }

  @Test
  public void stopRecording_CanStopRecording_WithTimeSupported() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.startRecording("PST");
    recorder.stopRecording();

    assertThat(recorder.recording, is(false));
    assertThat(recorder.recorded, is(true));
  }

  @Test(expected = IllegalStateException.class)
  public void getRecordedLog_WillThrowISE_WhenNotRecorded() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recorded = false;
    recorder.getRecordedLog();
  }

  @Test
  public void getRecordedLog_CanReturnLog() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.recorded = true;
    recorder.log = new StringBuilder("abcde");
    assertThat(recorder.getRecordedLog(), is("abcde"));
  }

  @Test
  public void formatLog_CanFormat() {
    GregorianCalendar calendar =
      new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    calendar.clear();
    calendar.set(2000, 11, 31, 13, 15);
    long millis = calendar.getTimeInMillis();

    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    assertThat(
      recorder.formatLog(millis, "INFO", "Message\n"),
      is("12-31 01:15PM 00.000+0000 [INFO] Message\n"));
  }

  @Test
  public void formatLog_CanFormat_AppendCR() {
    GregorianCalendar calendar =
      new GregorianCalendar(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
    calendar.clear();
    calendar.set(2000, 11, 31, 13, 15);
    long millis = calendar.getTimeInMillis();

    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    assertThat(
      recorder.formatLog(millis, "INFO", "MessageWithoutCR"),
      is("12-31 01:15PM 00.000+0000 [INFO] MessageWithoutCR\n"));
  }

  @Test
  public void formatLog_CanFormat_WithNullParam() {
    WjrGAELogRecorder recorder = new WjrGAELogRecorderImpl();
    recorder.dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
    assertThat(
      recorder.formatLog(0, null, null),
      is("01-01 12:00AM 00.000+0000 [null] null\n"));
  }
}
