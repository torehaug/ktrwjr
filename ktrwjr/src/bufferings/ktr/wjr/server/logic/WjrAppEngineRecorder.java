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
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import bufferings.ktr.wjr.server.util.WjrUtils;

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

/**
 * The recorder of the App Engine info.
 * 
 * This class records the log, cpu time and api cpu time.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrAppEngineRecorder {

  /**
   * Whether on recording or not.
   */
  protected boolean recording = false;

  /**
   * Whether recorded or not.
   */
  protected boolean recorded = false;

  /**
   * The original delegate.
   */
  protected Delegate<Environment> originalDelegate;

  /**
   * The log buffer.
   */
  protected StringBuilder log;

  /**
   * The date format for logging.
   */
  protected DateFormat dateFormat =
    new SimpleDateFormat("MM-dd hh:mma ss.SSSZ ");

  /**
   * Whether the cpu time is supported or not.
   * 
   * It's only supported on the production server.
   */
  protected boolean cpuTimeSupported;

  /**
   * Start cpu time in megacycle.
   */
  protected long startCpuTime;

  /**
   * Stop cpu time in megacycle.
   */
  protected long stopCpuTime;

  /**
   * Whether the api time is supported or not.
   * 
   * It's only supported on the production server.
   */
  protected boolean apiTimeSupported;

  /**
   * Start api time in megacycle.
   */
  protected long startApiTime;

  /**
   * Stop api time in megacycle.
   */
  protected long stopApiTime;

  /**
   * The quota service of GAE.
   */
  protected QuotaService quotaService;

  /**
   * Start recording.
   * 
   * @param timeZoneId
   *          The timezoneId for the log time.
   * @throws IllegalStateException
   *           The recording has been already started.
   */
  @SuppressWarnings("unchecked")
  public void startRecording(String timeZoneId) {
    checkState(!recording, "Recording has been already started.");
    recording = true;
    recorded = false;

    dateFormat.setTimeZone(WjrUtils.getTimeZone(timeZoneId));
    quotaService = getQuotaService();
    log = new StringBuilder();

    originalDelegate = ApiProxy.getDelegate();
    ApiProxy.setDelegate(this.new LogHookDelegate());

    cpuTimeSupported =
      quotaService.supports(QuotaService.DataType.CPU_TIME_IN_MEGACYCLES);
    apiTimeSupported =
      quotaService.supports(QuotaService.DataType.API_TIME_IN_MEGACYCLES);

    if (cpuTimeSupported) {
      startCpuTime = quotaService.getCpuTimeInMegaCycles();
    }
    if (apiTimeSupported) {
      startApiTime = quotaService.getApiTimeInMegaCycles();
    }
  }

  /**
   * Gets the quota service.
   * 
   * @return The quota service.
   */
  protected QuotaService getQuotaService() {
    return QuotaServiceFactory.getQuotaService();
  }

  /**
   * Stops recording.
   */
  public void stopRecording() {
    checkState(recording, "Recording hasn't been started.");

    ApiProxy.setDelegate(originalDelegate);
    originalDelegate = null;

    if (cpuTimeSupported) {
      stopCpuTime = quotaService.getCpuTimeInMegaCycles();
    }
    if (apiTimeSupported) {
      stopApiTime = quotaService.getApiTimeInMegaCycles();
    }

    recording = false;
    recorded = true;
  }

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
   * Gets the recorded cpu time. The unit is cpu_ms.
   * 
   * It the cpu time is not supported, returns empty string.
   * 
   * @return The recorded cpu time.
   * @throws IllegalStateException
   *           When the recording hasn't been done.
   */
  public String getRecordedCpuTime() {
    checkState(recorded, "Recording hasn't been done.");
    if (!cpuTimeSupported) {
      return "";
    }

    long duration = stopCpuTime - startCpuTime;
    long milliSeconds =
      (long) (quotaService.convertMegacyclesToCpuSeconds(duration) * 1000);
    return Long.toString(milliSeconds);
  }

  /**
   * Gets the recorded api cpu time. The unit is api_cpu_ms.
   * 
   * It the api cpu time is not supported, returns empty string.
   * 
   * @return The recorded api cpu time.
   * @throws IllegalStateException
   *           When the recording hasn't been done.
   */
  public String getRecordedApiTime() {
    checkState(recorded, "Recording hasn't been done.");
    if (!apiTimeSupported) {
      return "";
    }

    long duration = stopApiTime - startApiTime;
    long milliSeconds =
      (long) (quotaService.convertMegacyclesToCpuSeconds(duration) * 1000);
    return Long.toString(milliSeconds);
  }

  /**
   * The delegate class which hooks the logging.
   * 
   * This class uses the instance variable of {@link WjrAppEngineRecorder}
   * instance.
   * 
   * @author bufferings[at]gmail.com
   */
  protected class LogHookDelegate implements Delegate<Environment> {

    /**
     * {@inheritDoc}
     */
    @Override
    public void log(Environment arg0, LogRecord arg1) {
      log.append(formatLog(arg1));
      originalDelegate.log(arg0, arg1);
    }

    /**
     * Formats the log.
     * 
     * @param logRecord
     *          The log record.
     * @return The formatted string.
     */
    protected String formatLog(LogRecord logRecord) {
      long millis =
        TimeUnit.MILLISECONDS.convert(
          logRecord.getTimestamp(),
          TimeUnit.MICROSECONDS);
      return dateFormat.format(new Date(millis))
        + "["
        + logRecord.getLevel()
        + "] "
        + logRecord.getMessage()
        + "\n";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Future<byte[]> makeAsyncCall(Environment arg0, String arg1,
        String arg2, byte[] arg3, ApiConfig arg4) {
      return originalDelegate.makeAsyncCall(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] makeSyncCall(Environment arg0, String arg1, String arg2,
        byte[] arg3) throws ApiProxyException {
      return originalDelegate.makeSyncCall(arg0, arg1, arg2, arg3);
    }
  }

}
