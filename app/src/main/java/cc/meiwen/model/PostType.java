package cc.meiwen.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-01
 * Time: 14:59
 * Version 1.0
 */

public class PostType extends BmobObject{

    private String postTypeId;

    private String type;

    private String title;

    private String iconUrl;

    private BmobFile typeBg;

    public PostType(){
        super();
    }

    public PostType(String type, String title, String iconUrl, BmobFile typeBg) {
        super();
        this.type = type;
        this.title = title;
        this.iconUrl = iconUrl;
        this.typeBg = typeBg;
    }

    /**
     * 克隆一个PostType供OrmLite数据库使用
     * */
    public PostType clone(){
        PostType postType = new PostType(type, title, iconUrl, typeBg);
        postType.setPostTypeId(this.getObjectId());
        return postType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public BmobFile getTypeBg() {
        return typeBg;
    }

    public void setTypeBg(BmobFile typeBg) {
        this.typeBg = typeBg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPostTypeId() {
        setPostTypeId(this.getObjectId());
        return postTypeId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    @Override
    public String toString() {
        return type;
    }
}
