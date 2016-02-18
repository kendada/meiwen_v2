package cc.meiwen.model;

import cn.bmob.v3.BmobObject;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-06
 * Time: 14:49
 * Version 1.0
 */

public class Feedback extends BmobObject {

    private String content;

    private User user;

    public Feedback() {
        super();
    }

    public Feedback(String content, User user) {
        this.content = content;
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
