package cc.meiwen.model;

/**
 * Created by abc on 2017/11/22.
 */

public class FriendsTimelineStatusesBO {

    public String created_at; // 微博创建时间
    public long id; // 微博ID
    public String text; // 微博信息内容
    public String thumbnail_pic; // 缩略图片地址，没有时不返回此字段
    public String bmiddle_pic; //  中等尺寸图片地址，没有时不返回此字段

    public String original_pic; // 原始图片地址，没有时不返回此字段

    public WeiBoUserBO user; // 微博作者的用户信息字段 详细

    @Override
    public String toString() {
        return "FriendsTimelineStatusesBO{" +
                "created_at='" + created_at + '\'' +
                ", id=" + id +
                ", text='" + text + '\'' +
                ", thumbnail_pic='" + thumbnail_pic + '\'' +
                ", bmiddle_pic='" + bmiddle_pic + '\'' +
                ", original_pic='" + original_pic + '\'' +
                ", user=" + user +
                '}';
    }
}
