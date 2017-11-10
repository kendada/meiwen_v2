package cc.meiwen.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-18
 * Time: 09:26
 * Version 1.0
 */

public class Artcile extends BmobObject {

    private String title;

    private String content;

    private String original_url; //原文链接

    private String abstrac; //摘要

    private BmobFile thumb; //缩略图

    private String thumbUrl; //缩略图链接

    public Artcile() {
        super();
    }

    public Artcile clone(){
        Artcile artcile = new Artcile(title, content, original_url, abstrac, thumb);
        artcile.setThumbUrl(thumb.getUrl());
        return artcile;
    }

    public Artcile(String title, String content, String original_url) {
        super();
        this.title = title;
        this.content = content;
        this.original_url = original_url;
    }

    public Artcile(String title, String content, String original_url, String abstrac, BmobFile thumbBmobFile) {
        this.title = title;
        this.content = content;
        this.original_url = original_url;
        this.abstrac = abstrac;
        this.thumb = thumbBmobFile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getOriginal_url() {
        return original_url;
    }

    public void setOriginal_url(String original_url) {
        this.original_url = original_url;
    }

    public String getAbstrac() {
        return abstrac;
    }

    public void setAbstrac(String abstrac) {
        this.abstrac = abstrac;
    }

    public BmobFile getThumb() {
        return thumb;
    }

    public void setThumb(BmobFile thumbBmobFile) {
        this.thumb = thumbBmobFile;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public void setThumbUrl(String thumbUrl) {
        this.thumbUrl = thumbUrl;
    }

    @Override
    public String toString() {
        return "Artcile{" +
                "title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", original_url='" + original_url + '\'' +
                ", abstrac='" + abstrac + '\'' +
                ", thumb=" + thumb +
                ", thumbUrl='" + thumbUrl + '\'' +
                '}';
    }
}
