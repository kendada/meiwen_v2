package cc.meiwen.adapter.base;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 11:10
 * Version 1.0
 */

public class AdapterHolder {

    private final int mPosition;

    private final View mConvertView;

    private final Map<Integer, View> mViews;

    private AdapterHolder(int position, int layoutId, ViewGroup parent){
        mPosition = position;
        mViews = new HashMap<>();
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
    }

    public static AdapterHolder get(int position, View convertView, ViewGroup parent, int layoutId){
        if(convertView == null){
            return new AdapterHolder(position, layoutId, parent);
        } else {
            return (AdapterHolder) convertView.getTag();
        }
    }

    public <T extends View> T getView(int viewId){
        View view = mViews.get(viewId);
        if(view == null){
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T)view;
    }

    public View getConvertView(){
        return mConvertView;
    }

    public int getPosition(){
        return mPosition;
    }

}
