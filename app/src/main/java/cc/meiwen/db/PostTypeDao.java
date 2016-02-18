package cc.meiwen.db;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.List;

import cc.meiwen.model.PostType;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-18
 * Time: 16:20
 * Version 1.0
 */

public class PostTypeDao extends DBDao<PostType> {

    private Dao<PostType, Integer> postTypePeo;

    public PostTypeDao(Context context) {
        super(context);

        try {
            postTypePeo = mHelper.getDao(PostType.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        int count = 0;
        try {
            count = (int)postTypePeo.countOf();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void add(PostType postType) {
        try{
            postTypePeo.create(postType.clone());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 查找操作
     * */
    public PostType searchPostType(String objectId){
        PostType postType = null;
        try{
            QueryBuilder<PostType, Integer> queryBuilder = postTypePeo.queryBuilder();
            Where<PostType, Integer> where = queryBuilder.where();
            where.eq("postTypeId", objectId);
            queryBuilder.setWhere(where);

            postType = queryBuilder.queryForFirst();

        } catch (Exception e){
            e.printStackTrace();
        }
        return postType;
    }

    /**
     * 批量保存
     * */
    public void addPostTypes(List<PostType> postTypes){
        for(PostType postType:postTypes){
            add(postType);
        }
    }

    /**
     * 全部清除
     * */
    @Override
    public void delete(PostType postType) {
        try {
            DeleteBuilder<PostType, Integer> deleteBuilder = postTypePeo.deleteBuilder();
            deleteBuilder.reset(); //全部清除
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(PostType postType) {

    }

    @Override
    public List<PostType> getDatas(int p) {
        return null;
    }

    @Override
    public List<PostType> getAllDatas() {
        List<PostType> postTypes = null;
        try {
            postTypes = postTypePeo.queryForAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return postTypes;
    }
}
