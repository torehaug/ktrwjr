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

/**
 * Configuration property class.
 * 
 * Loads the ktrwjr.properties in the resource. The only property is "timezone"
 * which is used for converting time of logs.
 * 
 * @author bufferings[at]gmail.com
 */
public class KtrWjrConfig {

  private static final Logger logger =
    Logger.getLogger(KtrWjrConfig.class.getSimpleName());

  protected static final String RESOURCE_FILE = "ktrwjr.properties";

  protected static final String KEY_TIMEZONE = "timezone";

  /**
   * The singleton instance.
   */
  protected static KtrWjrConfig instance;

  /**
   * Gets the singleton instance. If the instance is null, it will be
   * constructed.
   * 
   * @return The singleton instance.
   */
  protected static KtrWjrConfig get() {
    if (instance != null) {
      return instance;
    }

    KtrWjrConfig config = new KtrWjrConfig();
    Properties props = loadProperties(RESOURCE_FILE);
    if (props.containsKey(KEY_TIMEZONE)) {
      config.timezone = props.getProperty(KEY_TIMEZONE);
    }

    instance = config;
    logger.info("The configuration is " + instance.toString());
    return config;
  }

  /**
   * Loads the property from the property file.
   * 
   * @param resourceFile
   *          The resource file name.
   * @return Loaded properties.
   */
  protected static Properties loadProperties(String resourceFileName) {
    if (resourceFileName == null || resourceFileName.isEmpty()) {
      return new Properties();
    }

    InputStream is = null;
    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      is = cl.getResourceAsStream(resourceFileName);
      if (is != null) {
        try {
          Properties properties = new Properties();
          properties.load(is);
          return properties;
        } catch (IOException e) {
          logger.log(
            Level.WARNING,
            "Fails to load configuration resource file .(file="
              + resourceFileName
              + ")",
            e);
          return new Properties();
        }
      } else {
        logger.log(
          Level.WARNING,
          "Configuration resource file not found.(file="
            + resourceFileName
            + ")");
        return new Properties();
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
  }

  /**
   * Gets the timezone property for logs.
   * 
   * @return The timezone property for logs.
   */
  public static String getTimezone() {
    return get().timezone;
  }

  /**
   * The timezone property for logs. The default value is GMT.
   */
  protected String timezone = "GMT";

  /**
   * Closed for singleton.(Open for testing.)
   */
  protected KtrWjrConfig() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return KtrWjrConfig.class.getSimpleName() + "(timezone=" + timezone + ")";
  }

}
