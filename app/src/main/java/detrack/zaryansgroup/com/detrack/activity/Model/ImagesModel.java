package detrack.zaryansgroup.com.detrack.activity.Model;

/**
 * Created by jawad on 8/2/2016.
 */
public class ImagesModel {
int image_id,image_order_id;
    String image_name,image_tag;
    String imag_is_synced;

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getImage_order_id() {
        return image_order_id;
    }

    public void setImage_order_id(int image_order_id) {
        this.image_order_id = image_order_id;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getImage_tag() {
        return image_tag;
    }

    public void setImage_tag(String image_tag) {
        this.image_tag = image_tag;
    }

    public String getImag_is_synced() {
        return imag_is_synced;
    }

    public void setImag_is_synced(String imag_is_synced) {
        this.imag_is_synced = imag_is_synced;
    }
}
