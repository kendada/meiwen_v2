package cc.meiwen.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

/**
 * Created by abc on 2017/11/2.
 */

public class RecommendPost extends BmobObject {

    public String title;

    public BmobFile imgFile;

    public String webUrl; // 不为空时，才可以点击
}
