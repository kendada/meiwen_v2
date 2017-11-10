package cc.meiwen.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 17:21
 * Version 1.0
 */

public class Post extends BmobObject {

    private int id;

    private String postId;

    private String title;        //帖子标题

    private String content;      // 帖子内容

    private User user;         //帖子的发布者，这里体现的是一对一的关系，该帖子属于某个用户

    private String userId;

    private BmobFile conImg;      //帖子图片

    private String conImgUrl;

    private BmobRelation likes;  //多对多关系：用于存储喜欢该帖子的所有用户

    private PostType postType;

    private String postTypeId;

    private String contentImg;

    private boolean isFavo; //当前登录用户是否已经收藏

    private String favoId;

    private boolean isShow; //用户发表帖子是否通过审核

    private String createdAtDate; //创建时间

    private String tag = Post.class.getSimpleName();

    public Post(){
        super();
    }

    public Post(String title, String content, User user, BmobFile conImg,
                BmobRelation likes, PostType postType, String contentImg, boolean isFavo, String favoId, boolean isShow) {
        super();
        this.title = title;
        this.content = content;
        this.user = user;
        this.conImg = conImg;
        this.likes = likes;
        this.postType = postType;
        this.contentImg = contentImg;
        this.isFavo = isFavo;
        this.favoId = favoId;
        this.isShow = isShow;

    }

    /**
     * 克隆一个Post
     * */
    public Post clone(){
        Post post = new Post(title, content, user,
                conImg, likes, postType, contentImg, isFavo, favoId, isShow);
        post.setPostId(this.getObjectId());
        post.setUserId(user.getObjectId());
        post.setPostTypeId(postType.getObjectId());
        post.setConImgUrl(conImg.getUrl());
        post.setCreatedAtDate(getCreatedAt());
        return post;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BmobFile getConImg() {
        return conImg;
    }

    public void setConImg(BmobFile conImg) {
        this.conImg = conImg;
    }

    public BmobRelation getLikes() {
        return likes;
    }

    public void setLikes(BmobRelation likes) {
        this.likes = likes;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public String getContentImg() {
        return contentImg;
    }

    public void setContentImg(String contentImg) {
        this.contentImg = contentImg;
    }

    public boolean isFavo() {
        return isFavo;
    }

    public void setIsFavo(boolean isFavo) {
        this.isFavo = isFavo;
    }

    public String getFavoId() {
        return favoId;
    }

    public void setFavoId(String favoId) {
        this.favoId = favoId;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setIsShow(boolean isShow) {
        this.isShow = isShow;
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

    public String getConImgUrl() {
        return conImgUrl;
    }

    public void setConImgUrl(String conImgUrl) {
        this.conImgUrl = conImgUrl;
    }

    public String getPostTypeId() {
        return postTypeId;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setPostTypeId(String postTypeId) {
        this.postTypeId = postTypeId;
    }

    public String getCreatedAtDate() {
        return createdAtDate;
    }

    public void setCreatedAtDate(String createdAtDate) {
        this.createdAtDate = createdAtDate;
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", postId='" + postId + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", user=" + user +
                ", userId='" + userId + '\'' +
                ", conImg=" + conImg +
                ", conImgUrl='" + conImgUrl + '\'' +
                ", likes=" + likes +
                ", postType=" + postType +
                ", postTypeId='" + postTypeId + '\'' +
                ", contentImg='" + contentImg + '\'' +
                ", isFavo=" + isFavo +
                ", favoId='" + favoId + '\'' +
                ", isShow=" + isShow +
                ", createdAtDate='" + createdAtDate + '\'' +
                ", tag='" + tag + '\'' +
                '}';
    }
}
