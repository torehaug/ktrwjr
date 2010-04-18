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
package bufferings.ktr.wjr.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WjrConfig {

  private static final Logger logger =
    Logger.getLogger(WjrConfig.class.getSimpleName());

  protected static final String RESOURCE_FILE = "ktrwjr.properties";

  protected static final String KEY_TIMEZONE = "timezone";

  protected static WjrConfig instance;

  protected static WjrConfig get() {
    if (instance != null) {
      return instance;
    }

    WjrConfig config = new WjrConfig();
    Properties props = loadProperties();
    if (props.containsKey(KEY_TIMEZONE)) {
      config.timezone = props.getProperty(KEY_TIMEZONE);
    }

    instance = config;
    logger.info("The configuration is " + instance.toString());
    return config;
  }

  protected static Properties loadProperties() {
    Properties properties = new Properties();
    InputStream is = null;
    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      is = cl.getResourceAsStream(RESOURCE_FILE);
      if (is != null) {
        try {
          properties.load(is);
        } catch (IOException e) {
          logger.log(
            Level.WARNING,
            "Fails to load configuration resource file .("
              + RESOURCE_FILE
              + ")",
            e);
          e.printStackTrace();
        }
      } else {
        logger.log(Level.WARNING, "Configuration resource file not found.("
          + RESOURCE_FILE
          + ")");
      }
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (Exception e) {
          // ignore
        }
      }
    }
    return properties;
  }

  public static String getTimezone() {
    return get().timezone;
  }

  protected String timezone = "GMT";

  @Override
  public String toString() {
    return "WjrConfig(timezone=" + timezone + ")";
  }

}
