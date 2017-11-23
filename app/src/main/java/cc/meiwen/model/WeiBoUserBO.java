package cc.meiwen.model;

/**
 * Created by abc on 2017/11/22.
 */

public class WeiBoUserBO {

    public long id;

    public String screen_name;

    public String description;

    public String profile_image_url;
    public String cover_image;
    public String cover_image_phone;

    public String created_at;

    @Override
    public String toString() {
        return "WeiBoUserBO{" +
                "id=" + id +
                ", screen_name='" + screen_name + '\'' +
                ", description='" + description + '\'' +
                ", profile_image_url='" + profile_image_url + '\'' +
                ", cover_image='" + cover_image + '\'' +
                ", cover_image_phone='" + cover_image_phone + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }
}
