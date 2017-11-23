package cc.meiwen.model;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 16:27
 * Version 1.0
 */

public class User extends BmobUser {

    private int id;

    private String userId;

    private Integer age; //年龄

    private String sex; //性别

    private String iconUrl; //用户头像地址

    private BmobFile icon; //用户头像

    private String url;

    private String userInfo; // 用户签名

    private String loginType; // 登录平台
    private String snsUserUrl; // 地址

    private boolean isAdmin; // 是否是管理员，管理员可以参与审核帖子

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

    public String getSnsUserUrl() {
        return snsUserUrl;
    }

    public void setSnsUserUrl(String snsUserUrl) {
        this.snsUserUrl = snsUserUrl;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
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

    public String getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(String userInfo) {
        this.userInfo = userInfo;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    @Override
    public String toString() {
        return this.getUsername();
    }
}
