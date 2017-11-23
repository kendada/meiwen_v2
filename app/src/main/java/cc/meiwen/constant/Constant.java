package cc.meiwen.constant;

/**
 * Created by abc on 2017/11/16.
 */

public interface Constant {

    interface URL {
        String url = "https://api.weibo.com/2/";
    }

    interface WeiBo {
        String APP_KEY = "350257351";
        String REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
        String SCOPE = "email,direct_messages_read,direct_messages_write,"
                        + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                        + "follow_app_official_microblog," + "invitation_write";
        String LOGIN_WEIBO = "WEIBO";
    }

    interface ShareKey {
        String OBJECT_ID = "object_id";
    }
}
