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

import org.junit.Test;

import com.google.appengine.api.quota.QuotaService;

public class WjrGAEQuotaRecorderTest {

  @Test(expected = IllegalStateException.class)
  public void startRecording_WillThrowISE_WhenRecording() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recording = true;
    recorder.startRecording();
  }

  @Test
  public void startRecording_CanStartRecording() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder() {
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
    recorder.startRecording();

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.cpuTimeSupported, is(false));
    assertThat(recorder.apiTimeSupported, is(false));
    assertThat(recorder.startCpuTime, is(0L));
    assertThat(recorder.startApiTime, is(0L));
  }

  @Test
  public void startRecording_CanStartRecording_WithTimeSupported() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder() {
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
    recorder.startRecording();

    assertThat(recorder.recording, is(true));
    assertThat(recorder.recorded, is(false));
    assertThat(recorder.cpuTimeSupported, is(true));
    assertThat(recorder.apiTimeSupported, is(true));
    assertThat(recorder.startCpuTime, is(2L));
    assertThat(recorder.startApiTime, is(1L));
  }

  @Test(expected = IllegalStateException.class)
  public void stopRecording_WillThrowISE_WhenNotRecording() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recording = false;
    recorder.stopRecording();
  }

  @Test
  public void stopRecording_CanStopRecording() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
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
    assertThat(recorder.stopCpuTime, is(0L));
    assertThat(recorder.stopApiTime, is(0L));
  }

  @Test
  public void stopRecording_CanStopRecording_WithTimeSupported() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
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
    assertThat(recorder.stopCpuTime, is(2L));
    assertThat(recorder.stopApiTime, is(1L));
  }

  @Test(expected = IllegalStateException.class)
  public void getRecordedCpuTime_WillThrowISE_WhenNotRecorded() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recorded = false;
    recorder.getRecordedCpuTime();
  }

  @Test
  public void getRecordedCpuTime_WillReturnEmptyString_WhenNotSupported() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recorded = true;
    recorder.cpuTimeSupported = false;
    assertThat(recorder.getRecordedCpuTime(), is(""));
  }

  @Test
  public void getRecordedCpuTime_CanReturnCpuTime() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
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
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recorded = false;
    recorder.getRecordedApiTime();
  }

  @Test
  public void getRecordedApiTime_WillReturnEmptyString_WhenNotSupported() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recorded = true;
    recorder.apiTimeSupported = false;
    assertThat(recorder.getRecordedApiTime(), is(""));
  }

  @Test
  public void getRecordedApiTime_CanReturnApiTime() {
    WjrGAEQuotaRecorder recorder = new WjrGAEQuotaRecorder();
    recorder.recorded = true;
    recorder.apiTimeSupported = true;
    recorder.stopApiTime = 11200;
    recorder.startApiTime = 10000;
    recorder.quotaService = new QuotaServiceAdapter();

    // expected: (11200 - 10000) * 1000 / 1200d
    assertThat(recorder.getRecordedApiTime(), is("1000"));
  }

  private static class QuotaServiceAdapter implements QuotaService {
    public long convertCpuSecondsToMegacycles(double cpuSeconds) {
      return (long) (cpuSeconds * 1200);
    }

    public double convertMegacyclesToCpuSeconds(long megaCycles) {
      return (double) megaCycles / 1200d;
    }

    public long getApiTimeInMegaCycles() {
      return 0;
    }

    public long getCpuTimeInMegaCycles() {
      return 0;
    }

    public boolean supports(DataType type) {
      return false;
    }
  }
}
