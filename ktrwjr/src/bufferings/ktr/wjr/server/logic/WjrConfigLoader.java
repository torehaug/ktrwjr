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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import bufferings.ktr.wjr.shared.model.WjrConfig;

/**
 * The loader of the WjrConfig.
 * 
 * @author bufferings[at]gmail.com
 */
public class WjrConfigLoader {

  protected static final String CONFIG_RESOURCE_FILE = "ktrwjr.properties";

  protected static final String CONFIG_ITEM_CONFIGNAME = "configName";

  protected static final String CONFIG_ITEM_CPUMS_ENABLED = "cpumsEnabled";

  protected static final String CONFIG_ITEM_APIMS_ENABLED = "apimsEnabled";

  protected static final String CONFIG_ITEM_LOGHOOK_ENABLED = "logHookEnabled";

  protected static final String CONFIG_ITEM_LOGHOOK_TIMEZONE =
    "logHookTimezone";

  protected static final String CONFIG_ITEM_RETRYOVERQUOTA_ENABLED =
    "retryOverQuotaEnabled";

  protected static final String CONFIG_ITEM_RETRYOVERQUOTA_INTERVAL =
    "retryOverQuotaInterval";

  protected static final String CONFIG_ITEM_RETRYOVERQUOTA_MAXCOUNT =
    "retryOverQuotaMaxCount";

  private static final Logger logger = Logger.getLogger(WjrConfigLoader.class
    .getName());

  /**
   * Load the config file.
   * 
   * @param configId
   *          The id of the config to load.
   * @return The loaded config.
   */
  public WjrConfig loadWjrConfig(String configId) {
    WjrConfig config = new WjrConfig();
    config.setConfigId(configId);
    configId = config.getConfigId();

    Properties props = loadProperties();
    Enumeration<Object> propKeys = props.keys();
    while (propKeys.hasMoreElements()) {
      String propKey = (String) propKeys.nextElement();
      ConfigKeyInfo keyInfo = new ConfigKeyInfo(propKey);
      if (!configId.equals(keyInfo.getConfigKey())) {
        continue;
      }
      setConfigAttribute(config, keyInfo, props.getProperty(propKey));
    }
    return config;
  }

  /**
   * Sets a property to the config.
   * 
   * @param config
   *          The config.
   * @param keyInfo
   *          The key.
   * @param value
   *          The value to apply.
   */
  protected void setConfigAttribute(WjrConfig config, ConfigKeyInfo keyInfo,
      String value) {
    String configItemName = keyInfo.getConfigName();
    if (CONFIG_ITEM_CONFIGNAME.equals(configItemName)) {
      config.setConfigName(value);
    } else if (CONFIG_ITEM_CPUMS_ENABLED.equals(configItemName)) {
      config.setCpumsEnabled(value);
    } else if (CONFIG_ITEM_APIMS_ENABLED.equals(configItemName)) {
      config.setApimsEnabled(value);
    } else if (CONFIG_ITEM_LOGHOOK_ENABLED.equals(configItemName)) {
      config.setLogHookEnabled(value);
    } else if (CONFIG_ITEM_LOGHOOK_TIMEZONE.equals(configItemName)) {
      config.setLogHookTimezone(value);
    } else if (CONFIG_ITEM_RETRYOVERQUOTA_ENABLED.equals(configItemName)) {
      config.setRetryOverQuotaEnabled(value);
    } else if (CONFIG_ITEM_RETRYOVERQUOTA_INTERVAL.equals(configItemName)) {
      config.setRetryOverQuotaInterval(value);
    } else if (CONFIG_ITEM_RETRYOVERQUOTA_MAXCOUNT.equals(configItemName)) {
      config.setRetryOverQuotaMaxCount(value);
    } else {
      logger.warning("Unknown item name.(" + configItemName + ")");
    }
  }

  /**
   * Load the property file.
   * 
   * @return The loadeg properties.
   */
  protected Properties loadProperties() {
    Properties properties = new Properties();
    InputStream is = null;
    try {
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      is = cl.getResourceAsStream(CONFIG_RESOURCE_FILE);
      if (is != null) {
        try {
          properties.load(is);
        } catch (IOException e) {
          logger.log(
            Level.WARNING,
            "Fails to load configuration resource file .("
              + CONFIG_RESOURCE_FILE
              + ")",
            e);
        }
      } else {
        logger.warning("Configuration resource file not found.("
          + CONFIG_RESOURCE_FILE
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

  /**
   * Configuration key value object.
   * 
   * The keys in the config file are written in form of (config key).configName.
   * 
   * @author bufferings[at]gmail.com
   */
  public static class ConfigKeyInfo {

    protected static final String SEPARATOR = ".";

    protected static final String SEPARATOR_REGEX = "\\.";

    /** The config key. */
    protected String configKey;

    /** The config name. */
    protected String configName;

    /**
     * Acquire the storeKey and the itemName from the propKey.
     * 
     * @param propKey
     *          The key of the property value.
     */
    public ConfigKeyInfo(String propKey) {
      checkNotNull(propKey);
      checkArgument(propKey.length() > 0);
      checkArgument(propKey.contains(SEPARATOR));

      String[] splits = propKey.split(SEPARATOR_REGEX);
      checkArgument(splits.length == 2);

      configKey = splits[0];
      configName = splits[1];
    }

    /**
     * Gets the config key.
     * 
     * @return The config key.
     */
    public String getConfigKey() {
      return configKey;
    }

    /**
     * Gets the config name.
     * 
     * @return The config name.
     */
    public String getConfigName() {
      return configName;
    }
  }

}
