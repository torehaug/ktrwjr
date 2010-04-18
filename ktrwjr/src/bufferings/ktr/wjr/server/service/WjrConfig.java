package bufferings.ktr.wjr.server.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.allen_sauer.gwt.log.client.Log;

public class WjrConfig {

  protected static final String RESOURCE_FILE = "ktrwjr.properties";

  protected static final String KEY_TIMEZONE = "timezone";
  
  protected static WjrConfig instance;

  protected static WjrConfig get() {
    if(instance != null){
      return instance;
    }
    
    WjrConfig config = new WjrConfig();
    Properties props = loadProperties();
    if (props.containsKey(KEY_TIMEZONE)) {
      config.timezone = props.getProperty(KEY_TIMEZONE);
    }

    instance = config;
    Log.info("The configuration is " + instance.toString());
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
          Log.warn("Fails to load configuration resource file .("
            + RESOURCE_FILE
            + ")", e);
          e.printStackTrace();
        }
      } else {
        Log.warn("Configuration resource file not found.("
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
