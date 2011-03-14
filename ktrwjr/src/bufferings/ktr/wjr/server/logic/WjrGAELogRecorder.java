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

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import bufferings.ktr.wjr.server.util.WjrServerUtils;

/**
 * The recorder template of the GAE log.
 * 
 * @author bufferings[at]gmail.com
 */
public abstract class WjrGAELogRecorder {

  /**
   * Whether on recording or not.
   */
  protected boolean recording = false;

  /**
   * Whether recorded or not.
   */
  protected boolean recorded = false;

  /**
   * The log buffer.
   */
  protected StringBuilder log;

  /**
   * The date format for logging.
   */
  protected DateFormat dateFormat = new SimpleDateFormat(
    "MM-dd hh:mma ss.SSSZ ",
    Locale.ENGLISH);

  /**
   * Start recording.
   * 
   * @param timeZoneId
   *          The timezoneId for the log time.
   * @throws IllegalStateException
   *           The recording has been already started.
   */
  public void startRecording(String timeZoneId) {
    checkState(!recording, "Recording has been already started.");
    recording = true;
    recorded = false;

    dateFormat.setTimeZone(WjrServerUtils.getTimeZone(timeZoneId));
    log = new StringBuilder();
    startBare();
  }

  /**
   * Start log recording.
   */
  protected abstract void startBare();

  /**
   * Stops recording.
   * 
   * @throws IllegalStateException
   *           The recording hasn't been started.
   */
  public void stopRecording() {
    checkState(recording, "Recording hasn't been started.");

    stopBare();

    recording = false;
    recorded = true;
  }

  /**
   * Stop log recording.
   */
  protected abstract void stopBare();

  /**
   * Gets whether the recording is running or not.
   * 
   * @return Whether the recording is running or not.
   */
  public boolean isRecording() {
    return recording;
  }

  /**
   * Gets the recorded log.
   * 
   * @return The recorded log.
   * @throws IllegalStateException
   *           When the recording hasn't been done.
   */
  public String getRecordedLog() {
    checkState(recorded, "Recording hasn't been done.");
    return log.toString();
  }

  /**
   * Formats the log.
   * 
   * @param millis
   *          The timestamp[ms].
   * @param level
   *          The log level string.
   * @param msg
   *          The log message.
   * @return The formatted string.
   */
  protected String formatLog(long millis, String level, String msg) {
    String newline = "";
    if (msg == null || !msg.endsWith("\n")) {
      newline = "\n";
    }
    return dateFormat.format(new Date(millis))
      + "["
      + level
      + "] "
      + msg
      + newline;
  }
}
