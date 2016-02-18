package cc.meiwen.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.ArrayList;
import java.util.List;

import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-17
 * Time: 15:57
 * Version 1.0
 */

public class PostDao extends DBDao<Post> {

    private Dao<Post, Integer> postDaoPeo;

    private FavoDao favoDao; //测试
    private PostTypeDao postTypeDao; //测试

    private String tag = PostDao.class.getSimpleName();

    public PostDao(Context context) {
        super(context);

        try {
            postDaoPeo = mHelper.getDao(Post.class);

            postTypeDao = new PostTypeDao(context);
            favoDao = new FavoDao(context);
        } catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public int count() {
        int count = 0;
        try {
            count = (int)postDaoPeo.countOf();
        } catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void add(Post post) {
        try{
            postDaoPeo.create(post);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Post post) {
        try{
            postDaoPeo.delete(post);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void update(Post post) {
        try{
            UpdateBuilder<Post, Integer> updateBuilder = postDaoPeo.updateBuilder();
            Where<Post, Integer> where = updateBuilder.where();
            where.eq("postId", post.getPostId());
            updateBuilder.setWhere(where);
            updateBuilder.update();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public List<Post> getDatas(int p) {
        List<Post> list = new ArrayList<>();
        try {
            QueryBuilder<Post, Integer> queryBuilder = postDaoPeo.queryBuilder();
            queryBuilder.limit(limit);
            queryBuilder.offset(limit * p);

            List<Post> tmp = queryBuilder.query();
            for(Post post:tmp){
                PostType postType = postTypeDao.searchPostType(post.getPostTypeId());
                post.setPostType(postType);
                list.add(post);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

    /**
     * 批量保存Post
     * */
    public void savePosts(List<Post> posts){
        for(Post post:posts){
            add(post.clone());
        }
    }

    @Override
    public List<Post> getAllDatas() {
        List<Post> list = null;
        try {
            list = postDaoPeo.queryForAll();
        } catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }


    /**
     * 获取收藏的Post
     * */
    public List<Post> getFavoPost(){
        List<Post> list = new ArrayList<>();
        try {
            List<Favo> favos = favoDao.getAllDatas();
            for(Favo favo:favos){
                Post post = new Post();
                post.setFavoId(favo.getFavoId());
                post.setObjectId(favo.getPostId());
                list.add(post);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
