package cc.meiwen.db;

import android.content.Context;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-12-16
 * Time: 17:24
 * Version 1.0
 */

public abstract class DBDao<T> {

    public Context mContext;
    public DatabaseHelper mHelper;

    public long limit = 20L;

    public DBDao(Context context){
        mContext = context;
        try{
            mHelper = new DatabaseHelper(mContext);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public abstract int count();

    public abstract void add(T t);

    public abstract void delete(T t);

    public abstract void update(T t);

    public abstract List<T> getDatas(int p);

    public abstract List<T> getAllDatas();
}
