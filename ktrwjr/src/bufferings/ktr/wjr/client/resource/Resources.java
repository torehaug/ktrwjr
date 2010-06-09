package bufferings.ktr.wjr.client.resource;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Resources used by the entire application.
 */
public interface Resources extends ClientBundle {

  @Source("ktrwjr_icon_16_16.png")
  ImageResource ktrwjr16();

  @Source("ktrwjr_loading.jpg")
  ImageResource ktrwjrLoading();
}