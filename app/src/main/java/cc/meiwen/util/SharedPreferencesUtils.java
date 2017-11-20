package cc.meiwen.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Created by hightkwt on 2016/1/12.
 */
public class SharedPreferencesUtils {

    private static final String SHARED_NAME = "Meiwen";
    private static SharedPreferences sf = AppUtils.getCtx().getSharedPreferences(SHARED_NAME, Context.MODE_PRIVATE);

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回空字符串
     */
    public static String getString(String name){
        return sf.getString(name, "");
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回false
     */
    public static boolean getBooleanDefaultFalse(String name){
        return sf.getBoolean(name, false);
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回false
     */
    public static boolean getBooleanDefaultTrue(String name){
        return sf.getBoolean(name, true);
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回-1f
     */
    public static float getFloat(String name){
        return sf.getFloat(name, -1f);
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回-1
     */
    public static int getInt(String name){
        return sf.getInt(name, -1);
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回-1l
     */
    public static long getLong(String name){
        return sf.getLong(name, -1L);
    }

    /**
     * 用于获取引用名"name"的值
     * @param name  引用名
     * @return  返回的是引用名的值，默认返回null
     */
    public static Set<String> getStringSet(String name){
        return sf.getStringSet(name, null);
    }

    /**
     * 用于获取preperences的所有引用
     * @return  返回的是所用引用的map集合 应用名/引用值
     */
    public static Map<String, ?> getAll(){
        return sf.getAll();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putString(String name, String value){
        sf.edit().putString(name,value).apply();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putBoolean(String name, boolean value){
        sf.edit().putBoolean(name, value).apply();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putFloat(String name, float value){
        sf.edit().putFloat(name, value).apply();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putInt(String name, int value){
        sf.edit().putInt(name, value).apply();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putLong(String name, long value){
        sf.edit().putLong(name, value).apply();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static boolean putLongCommit(String name, long value){
        return sf.edit().putLong(name, value).commit();
    }

    /**
     * 用于设置引用名和引用值
     * @param name  引用名
     * @param value 引用值
     * @return  是否提交成功
     */
    public static void putStringSet(String name, Set<String> value){
        sf.edit().putStringSet(name, value).apply();
    }

    public static void remove(String name) {
        sf.edit().remove(name).apply();
    }

    public static void clearAllData(){
        SharedPreferences.Editor mEditor = sf.edit();
        mEditor.clear();
        mEditor.commit();
    }

}
