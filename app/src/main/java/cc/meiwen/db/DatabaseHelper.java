package cc.meiwen.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.model.PostType;
import cc.meiwen.model.User;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-17
 * Time: 14:21
 * Version 1.0
 */

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private final static String  DB_NAME = "db_meiwen.db";

    private Map<String, Dao> daos = new HashMap<>();

    private String tag = DatabaseHelper.class.getSimpleName();


    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try{
            // 创建表
            TableUtils.createTable(connectionSource, Favo.class);
            TableUtils.createTable(connectionSource, User.class);
            TableUtils.createTable(connectionSource, PostType.class);
            TableUtils.createTable(connectionSource, Post.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i1) {
        //数据库升级：删除字段，添加字段
    }

    public synchronized Dao getDao(Class aClass) throws SQLException {
        Dao dao = null;
        String className = aClass.getSimpleName();
        if(daos.containsKey(className)){
            dao = daos.get(className);
        }

        if(dao == null){
            dao = super.getDao(aClass);
            daos.put(className, dao);
        }

        return dao;
    }

    @Override
    public void close() {
        super.close();
        for(String key:daos.keySet()){
            Dao dao = daos.get(key);
            dao = null;
        }
    }

}
