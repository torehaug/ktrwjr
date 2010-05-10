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

import com.google.appengine.api.quota.QuotaService;
import com.google.appengine.api.quota.QuotaServiceFactory;

/**
 * The recorder of the GAE quota info.
 * 
 * This class records the cpu time and api cpu time.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrGAEQuotaRecorder {

  /**
   * The quota service of GAE.
   */
  protected QuotaService quotaService;

  /**
   * Whether on recording or not.
   */
  protected boolean recording = false;

  /**
   * Whether recorded or not.
   */
  protected boolean recorded = false;

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
   * Start recording.
   * 
   * @throws IllegalStateException
   *           The recording has been already started.
   */
  public void startRecording() {
    checkState(!recording, "Recording has been already started.");
    recording = true;
    recorded = false;

    quotaService = getQuotaService();
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
   * 
   * @throws IllegalStateException
   *           The recording hasn't been started.
   */
  public void stopRecording() {
    checkState(recording, "Recording hasn't been started.");

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
}
