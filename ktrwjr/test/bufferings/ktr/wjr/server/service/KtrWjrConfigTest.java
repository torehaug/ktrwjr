package bufferings.ktr.wjr.server.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Properties;

import org.junit.Test;

public class KtrWjrConfigTest {

  @Test
  public void get_CanGetSameInscance() {
    assertThat(KtrWjrConfig.get(), is(not(nullValue())));
    assertThat(KtrWjrConfig.get(), is(KtrWjrConfig.get()));
  }

  @Test
  public void loadProperties_CanLoadProperties() {
    Properties properties =
      KtrWjrConfig.loadProperties(KtrWjrConfig.RESOURCE_FILE);
    assertThat((String) properties.get(KtrWjrConfig.KEY_TIMEZONE), is("JST"));
  }

  @Test
  public void loadProperties_WillReturnEmptyProperties_WhenNullFileName() {
    Properties properties = KtrWjrConfig.loadProperties(null);
    assertThat(properties.size(), is(0));
  }

  @Test
  public void loadProperties_WillReturnEmptyProperties_WhenEmptyFileName() {
    Properties properties = KtrWjrConfig.loadProperties("");
    assertThat(properties.size(), is(0));
  }

  @Test
  public void loadProperties_WillReturnEmptyProperties_WhenInvalidFile() {
    Properties properties = KtrWjrConfig.loadProperties("notexist");
    assertThat(properties.size(), is(0));
  }

  @Test
  public void getTimezone_WillReturnTimezone() {
    assertThat(KtrWjrConfig.getTimezone(), is("JST"));
  }

  @Test
  public void toString_WillRepresentTheClassInformation() {
    assertThat(KtrWjrConfig.get().toString(), is("KtrWjrConfig(timezone=JST)"));
  }
}
