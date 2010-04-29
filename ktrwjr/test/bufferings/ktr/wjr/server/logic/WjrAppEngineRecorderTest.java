package bufferings.ktr.wjr.server.logic;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.TimeZone;

import org.junit.Test;

import com.google.appengine.api.quota.QuotaService;
import com.google.apphosting.api.ApiProxy;

public class WjrAppEngineRecorderTest {

  @Test(expected = IllegalStateException.class)
  public void startRecording_WillThrowISE_WhenRecording() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recording = true;
    recorder.startRecording("JST");
  }

  @SuppressWarnings("unchecked")
  @Test
  public void startRecording_CanStartRecording() {
    ApiProxy.Delegate delegate = ApiProxy.getDelegate();
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder() {
      @Override
      protected QuotaService getQuotaService() {
        return new QuotaServiceAdapter() {
          @Override
          public boolean supports(DataType type) {
            return false;
          }
        };
      }
    };
    recorder.recording = false;
    recorder.startRecording("JST");

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.dateFormat.getTimeZone(), is(TimeZone
      .getTimeZone("JST")));
    assertThat(recorder.log.toString(), is(""));
    assertThat(recorder.originalDelegate, is(delegate));
    assertThat(recorder.cpuTimeSupported, is(false));
    assertThat(recorder.apiTimeSupported, is(false));
    assertThat(recorder.startCpuTime, is(0L));
    assertThat(recorder.startApiTime, is(0L));
  }

  @SuppressWarnings("unchecked")
  @Test
  public void startRecording_CanStartRecording_WithTimeSupported() {
    ApiProxy.Delegate delegate = ApiProxy.getDelegate();
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder() {
      @Override
      protected QuotaService getQuotaService() {
        return new QuotaServiceAdapter() {
          @Override
          public boolean supports(DataType type) {
            return true;
          }

          @Override
          public long getApiTimeInMegaCycles() {
            return 1;
          }

          @Override
          public long getCpuTimeInMegaCycles() {
            return 2;
          }
        };
      }
    };
    recorder.recording = false;
    recorder.startRecording("JST");

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.dateFormat.getTimeZone(), is(TimeZone
      .getTimeZone("JST")));
    assertThat(recorder.log.toString(), is(""));
    assertThat(recorder.originalDelegate, is(delegate));
    assertThat(recorder.cpuTimeSupported, is(true));
    assertThat(recorder.apiTimeSupported, is(true));
    assertThat(recorder.startCpuTime, is(2L));
    assertThat(recorder.startApiTime, is(1L));
  }

  @Test(expected = IllegalStateException.class)
  public void stopRecording_WillThrowISE_WhenNotRecording() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recording = false;
    recorder.stopRecording();
  }

  @Test
  public void stopRecording_CanStopRecording() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.quotaService = new QuotaServiceAdapter() {
      @Override
      public long getApiTimeInMegaCycles() {
        return 1;
      }

      @Override
      public long getCpuTimeInMegaCycles() {
        return 2;
      }
    };
    recorder.recording = true;
    recorder.cpuTimeSupported = false;
    recorder.apiTimeSupported = false;
    recorder.stopRecording();

    assertThat(recorder.recording, is(false));
    assertThat(recorder.recorded, is(true));
    assertThat(recorder.originalDelegate, is(nullValue()));
    assertThat(recorder.stopCpuTime, is(0L));
    assertThat(recorder.stopApiTime, is(0L));
  }

  @Test
  public void stopRecording_CanStopRecording_WithTimeSupported() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.quotaService = new QuotaServiceAdapter() {
      @Override
      public long getApiTimeInMegaCycles() {
        return 1;
      }

      @Override
      public long getCpuTimeInMegaCycles() {
        return 2;
      }
    };
    recorder.recording = true;
    recorder.cpuTimeSupported = true;
    recorder.apiTimeSupported = true;
    recorder.stopRecording();

    assertThat(recorder.recording, is(false));
    assertThat(recorder.recorded, is(true));
    assertThat(recorder.originalDelegate, is(nullValue()));
    assertThat(recorder.stopCpuTime, is(2L));
    assertThat(recorder.stopApiTime, is(1L));
  }

  @Test(expected = IllegalStateException.class)
  public void getRecordedLog_WillThrowISE_WhenNotRecorded() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = false;
    recorder.getRecordedLog();
  }

  public void getRecordedLog_CanReturnLog() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = true;
    recorder.log = new StringBuilder("abcde");
    assertThat(recorder.getRecordedLog(), is("abcde"));
  }

  @Test(expected = IllegalStateException.class)
  public void getRecordedCpuTime_WillThrowISE_WhenNotRecorded() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = false;
    recorder.getRecordedCpuTime();
  }

  @Test
  public void getRecordedCpuTime_WillReturnEmptyString_WhenNotSupported() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = true;
    recorder.cpuTimeSupported = false;
    assertThat(recorder.getRecordedCpuTime(), is(""));
  }

  @Test
  public void getRecordedCpuTime_CanReturnCpuTime() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = true;
    recorder.cpuTimeSupported = true;
    recorder.stopCpuTime = 11200;
    recorder.startCpuTime = 10000;
    recorder.quotaService = new QuotaServiceAdapter();

    // expected: (11200 - 10000) * 1000 / 1200d
    assertThat(recorder.getRecordedCpuTime(), is("1000"));
  }

  @Test(expected = IllegalStateException.class)
  public void getRecordedApiTime_WillThrowISE_WhenNotRecorded() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = false;
    recorder.getRecordedApiTime();
  }

  @Test
  public void getRecordedApiTime_WillReturnEmptyString_WhenNotSupported() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = true;
    recorder.apiTimeSupported = false;
    assertThat(recorder.getRecordedApiTime(), is(""));
  }

  @Test
  public void getRecordedApiTime_CanReturnApiTime() {
    WjrAppEngineRecorder recorder = new WjrAppEngineRecorder();
    recorder.recorded = true;
    recorder.apiTimeSupported = true;
    recorder.stopApiTime = 11200;
    recorder.startApiTime = 10000;
    recorder.quotaService = new QuotaServiceAdapter();

    // expected: (11200 - 10000) * 1000 / 1200d
    assertThat(recorder.getRecordedApiTime(), is("1000"));
  }

  private static class QuotaServiceAdapter implements QuotaService {
    @Override
    public long convertCpuSecondsToMegacycles(double cpuSeconds) {
      return (long) (cpuSeconds * 1200);
    }

    @Override
    public double convertMegacyclesToCpuSeconds(long megaCycles) {
      return (double) megaCycles / 1200d;
    }

    @Override
    public long getApiTimeInMegaCycles() {
      return 0;
    }

    @Override
    public long getCpuTimeInMegaCycles() {
      return 0;
    }

    @Override
    public boolean supports(DataType type) {
      return false;
    }
  }
}
