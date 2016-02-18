package cc.meiwen.ui.fragment;

import android.view.View;

import java.util.List;

import cc.meiwen.model.Post;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-10-30
 * Time: 13:42
 * Version 1.0
 */

public abstract class BaseFragment extends StateFragment {

    public abstract void initViews(View view);

    public abstract void initData();

    public List<Post> getNewList(List<Post> list, List<Post> temp){
        if(temp==null) return list;
        if(temp.size()<=0) return list;

        if(list==null) return list;
        if(list.size()<=0) return list;

        for(Post post:temp){
            for(Post pt:list){
                if(post.getObjectId().equals(pt.getObjectId())){
                    pt.setIsFavo(true);
                    pt.setFavoId(post.getFavoId());
                }
            }
        }
        return list;
    }

}
