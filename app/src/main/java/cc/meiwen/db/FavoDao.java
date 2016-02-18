package cc.meiwen.db;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import java.util.List;

import cc.meiwen.model.Favo;
import cn.bmob.v3.BmobObject;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-17
 * Time: 14:34
 * Version 1.0
 */

public class FavoDao extends DBDao<Favo> {

    private Dao<Favo, Integer> favoDaoPeo;

    private String tag = FavoDao.class.getSimpleName();

    public FavoDao(Context context) {
        super(context);

        try {
            favoDaoPeo = mHelper.getDao(Favo.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int count() {
        int count = 0;
        try {
            count = (int)favoDaoPeo.countOf();
        } catch (Exception e){
            e.printStackTrace();
        }
        return count;
    }

    @Override
    public void add(Favo favo) {
        try {
            favoDaoPeo.create(favo.clone());
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Favo favo) {
        favo = favo.clone();
        try{
            DeleteBuilder<Favo, Integer> deleteBuilder =  favoDaoPeo.deleteBuilder();
            Where<Favo, Integer> where = deleteBuilder.where();
            where.eq("favoId", favo.getFavoId()); // 设置删除条件
            deleteBuilder.setWhere(where);
            int code = deleteBuilder.delete();
            Log.i(tag, "取消了" + code + "个收藏");
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 从服务器获取已经收藏的Favo，缓存到本地数据库
     * */
    public void saveAllFavos(List<Favo> favos){
        for(Favo favo:favos){
            add(favo);
        }
    }

    @Override
    public void update(Favo favo) {

    }

    @Override
    public List<Favo> getDatas(int p) {
        List<Favo> favos = null;
        try{
            QueryBuilder<Favo, Integer> queryBuilder = favoDaoPeo.queryBuilder();
            queryBuilder.limit(limit);
            queryBuilder.offset(limit*p);
            favos = queryBuilder.query();
        } catch (Exception e){
            e.printStackTrace();
        }
        return favos;
    }

    @Override
    public List<Favo> getAllDatas() {
        List<Favo> favos = null;
        try{
            favos = favoDaoPeo.queryForAll();
        } catch (Exception e){
            e.printStackTrace();
        }
        return favos;
    }

}
