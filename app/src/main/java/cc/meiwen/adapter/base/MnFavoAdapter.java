package cc.meiwen.adapter.base;

import android.content.Context;

import java.util.List;

import cc.meiwen.model.Favo;
import cc.meiwen.model.Post;
import cc.meiwen.util.MWShare;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/20.
 */

public abstract class MnFavoAdapter extends MnBaseAdapter<Favo>{

    private MWShare mwShare;

    public MnFavoAdapter(Context context, List<Favo> datas) {
        super(context, datas);
        mwShare = new MWShare();
    }

    @Override
    public void shareWeiBo(Favo favo) {
        if (favo == null) return;
        Post post = favo.getPost();
        if(post == null) return;
        if(mwShare != null){
            mwShare.setText(post.getContent());
            BmobFile bmobFile = post.getConImg();
            if(bmobFile != null){
                mwShare.setImageUrl(bmobFile.getFileUrl());
            }
            mwShare.onShare(MWShare.SHARE_TYPE_0);
        }
    }

    @Override
    public void shareWeChatMoment(Favo favo) {
        if (favo == null) return;
        Post post = favo.getPost();
        if(post == null) return;
        if(mwShare != null){
            mwShare.setTitle(post.getContent());
            mwShare.setText(post.getContent());
            BmobFile bmobFile = post.getConImg();
            if(bmobFile != null){
                mwShare.setImageUrl(bmobFile.getFileUrl());
            }
            mwShare.onShare(MWShare.SHARE_TYPE_2);
        }
    }

    @Override
    public void shareWeChatFriend(Favo favo) {
        if (favo == null) return;
        Post post = favo.getPost();
        if(post == null) return;
        if(mwShare != null){
            mwShare.setText(post.getContent());
            mwShare.setTitle(post.getContent());
            BmobFile bmobFile = post.getConImg();
            if(bmobFile != null){
                mwShare.setImageUrl(bmobFile.getFileUrl());
            }
            mwShare.onShare(MWShare.SHARE_TYPE_1);
        }
    }

    @Override
    public void shareShortMessage(Favo favo) {
        if (favo == null) return;
        Post post = favo.getPost();
        if(post == null) return;
        if(mwShare != null){
            mwShare.setText(post.getContent());
            mwShare.onShare(MWShare.SHARE_TYPE_5);
        }
    }

    @Override
    public void saveImage(Favo favo) {
        if (favo == null) return;
        Post post = favo.getPost();
        if(post == null) return;
    }
}
