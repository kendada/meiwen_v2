package cc.meiwen.model;

import cn.bmob.v3.BmobObject;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 17:25
 * Version 1.0
 */

public class Comment extends BmobObject {

    private String content;  //评论内容

    private User user;       //评论的用户，Pointer类型，一对一关系

    private Post post;       //所评论的帖子，这里体现的是一对多的关系，一个评论只能属于一个微博

    private String postId;

    public Comment(String content, User user, Post post, String postId) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.postId = postId;
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

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }
}
