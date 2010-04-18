package bufferings.ktr.wjr.server.service;

import static bufferings.ktr.wjr.shared.util.Preconditions.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

public class WjrAppEngineRecorder {
  protected boolean recording = false;

  protected boolean recorded = false;

  protected Delegate<Environment> originalDelegate;

  protected StringBuilder log;

  protected DateFormat dateFormat =
    new SimpleDateFormat("MM-dd hh:mma ss.SSSZ ");

  protected boolean cpuTimeSupported;

  protected long startCpuTime;

  protected long stopCpuTime;

  protected boolean apiTimeSupported;

  protected long startApiTime;

  protected long stopApiTime;

  protected QuotaService quotaService;

  @SuppressWarnings("unchecked")
  public void startRecording(String timeZoneId) {
    checkState(!recording, "Recording has been already started.");
    recording = true;
    recorded = false;

    dateFormat.setTimeZone(TimeZone.getTimeZone(timeZoneId));
    quotaService = QuotaServiceFactory.getQuotaService();
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

  public String getRecordedLog() {
    checkState(recorded, "Recording hasn't been done.");
    return log.toString();
  }

  public String getRecordedCpuTime() {
    checkState(recorded, "Recording hasn't been done.");
    if (!cpuTimeSupported) {
      return null;
    }

    long duration = stopCpuTime - startCpuTime;
    long microSeconds =
      (long) quotaService.convertMegacyclesToCpuSeconds(duration);
    long milliSeconds =
      TimeUnit.MILLISECONDS.convert(microSeconds, TimeUnit.MICROSECONDS);
    return Long.toString(milliSeconds);
  }

  public String getRecordedApiTime() {
    checkState(recorded, "Recording hasn't been done.");
    if (!apiTimeSupported) {
      return null;
    }

    long duration = stopApiTime - startApiTime;
    long microSeconds =
      (long) quotaService.convertMegacyclesToCpuSeconds(duration);
    long milliSeconds =
      TimeUnit.MILLISECONDS.convert(microSeconds, TimeUnit.MICROSECONDS);
    return Long.toString(milliSeconds);
  }

  protected class LogHookDelegate implements Delegate<Environment> {

    @Override
    public void log(Environment arg0, LogRecord arg1) {
      log.append(formatLog(arg1));
      originalDelegate.log(arg0, arg1);
    }

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

    @Override
    public Future<byte[]> makeAsyncCall(Environment arg0, String arg1,
        String arg2, byte[] arg3, ApiConfig arg4) {
      return originalDelegate.makeAsyncCall(arg0, arg1, arg2, arg3, arg4);
    }

    @Override
    public byte[] makeSyncCall(Environment arg0, String arg1, String arg2,
        byte[] arg3) throws ApiProxyException {
      return originalDelegate.makeSyncCall(arg0, arg1, arg2, arg3);
    }
  }

}
