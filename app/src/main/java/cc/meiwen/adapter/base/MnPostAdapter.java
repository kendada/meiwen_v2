package cc.meiwen.adapter.base;

import android.content.Context;

import java.util.List;

import cc.meiwen.model.Post;
import cc.meiwen.util.MWShare;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/20.
 */

public abstract class MnPostAdapter extends MnBaseAdapter<Post>{

    private MWShare mwShare;

    public MnPostAdapter(Context context, List<Post> datas) {
        super(context, datas);
        mwShare = new MWShare();
    }

    @Override
    public void shareWeiBo(Post post) {
        if (post == null) return;
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
    public void shareWeChatMoment(Post post) {
        if (post == null) return;
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
    public void shareWeChatFriend(Post post) {
        if (post == null) return;
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
    public void shareShortMessage(Post post) {
        if (post == null) return;
        if(mwShare != null){
            mwShare.setText(post.getContent());
            mwShare.onShare(MWShare.SHARE_TYPE_5);
        }
    }

    @Override
    public void saveImage(Post post) {
        if (post == null) return;
        BmobFile bmobFile = post.getConImg();
        String imgUrl = null;
        if(bmobFile != null){
            imgUrl = bmobFile.getUrl();
        } else {
            imgUrl = post.getConImgUrl();
        }
        saveImageToSDCard(imgUrl);
    }
}
