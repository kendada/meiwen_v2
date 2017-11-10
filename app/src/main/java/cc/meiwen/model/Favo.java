package cc.meiwen.model;

import cn.bmob.v3.BmobObject;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-02
 * Time: 09:56
 * Version 1.0
 */

public class Favo extends BmobObject {

    private int id;

    private User user;

    private String userId;

    private Post post;

    private String postId;

    private String favoId;

    public Favo(){
        super();
    }

    public Favo(User user, Post post) {
        super();
        this.user = user;
        this.post = post;
    }

    /**
     * 克隆一个Favo
     * */
    public Favo clone(){
        Favo favo = new Favo(user, post);
        favo.setUserId(user.getObjectId());
        favo.setPostId(post.getObjectId());
        favo.setFavoId(this.getObjectId());
        return favo;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getFavoId() {
        return favoId;
    }

    public void setFavoId(String favoId) {
        this.favoId = favoId;
    }

    @Override
    public String toString() {
        if(user!=null && post!=null){
            return user.getUsername()+"----"+post+getObjectId();
        }
        return super.toString();
    }
}
