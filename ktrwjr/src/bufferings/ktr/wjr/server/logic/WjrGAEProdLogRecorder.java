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

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.ApiConfig;
import com.google.apphosting.api.ApiProxy.ApiProxyException;
import com.google.apphosting.api.ApiProxy.Delegate;
import com.google.apphosting.api.ApiProxy.Environment;
import com.google.apphosting.api.ApiProxy.LogRecord;

/**
 * The recorder of the GAE Production Server log.
 * 
 * This class records the log, cpu time and api cpu time.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrGAEProdLogRecorder extends WjrGAELogRecorder {

  protected Delegate<Environment> originalDelegate;

  /**
   * {@inheritDoc}
   */
  @Override
  @SuppressWarnings("unchecked")
  protected void startBare() {
    originalDelegate = ApiProxy.getDelegate();
    ApiProxy.setDelegate(this.new LogHookDelegate());
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void stopBare() {
    ApiProxy.setDelegate(originalDelegate);
    originalDelegate = null;
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
    return formatLog(millis, logRecord.getLevel().name(), logRecord
      .getMessage());
  }

  /**
   * The delegate class which hooks the logging.
   * 
   * This class uses the instance variable of {@link WjrGAEProdLogRecorder}
   * instance.
   * 
   * @author bufferings[at]gmail.com
   */
  protected class LogHookDelegate implements Delegate<Environment> {

    /**
     * {@inheritDoc}
     */
    public void log(Environment arg0, LogRecord arg1) {
      log.append(formatLog(arg1));
      originalDelegate.log(arg0, arg1);
    }

    /**
     * {@inheritDoc}
     */
    public Future<byte[]> makeAsyncCall(Environment arg0, String arg1,
        String arg2, byte[] arg3, ApiConfig arg4) {
      return originalDelegate.makeAsyncCall(arg0, arg1, arg2, arg3, arg4);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] makeSyncCall(Environment arg0, String arg1, String arg2,
        byte[] arg3) throws ApiProxyException {
      return originalDelegate.makeSyncCall(arg0, arg1, arg2, arg3);
    }
  }
}
