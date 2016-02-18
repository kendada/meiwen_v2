package cc.meiwen.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 16:27
 * Version 1.0
 */

@DatabaseTable(tableName = "tab_user")
public class User extends BmobUser {

    @DatabaseField(generatedId = true)
    private int id;

    @DatabaseField
    private String userId;

    @DatabaseField
    private Integer age; //年龄

    @DatabaseField
    private String sex; //性别

    private String iconUrl; //用户头像地址

    private BmobFile icon; //用户头像

    @DatabaseField
    private String url;

    public User() {
        super();
    }

    public User(Integer age, String sex, String iconUrl, BmobFile icon) {
        super();
        this.age = age;
        this.sex = sex;
        this.iconUrl = iconUrl;
        this.icon = icon;

        setUserId(this.getObjectId());
        setIconUrl(icon.getUrl());
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public BmobFile getIcon() {
        return icon;
    }

    public void setIcon(BmobFile icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserId() {
        setUserId(this.getObjectId());
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
