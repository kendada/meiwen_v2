package cc.meiwen.http;

/**
 * Created by abc on 2017/7/21.
 * 统一错误处理
 */

public class OkHttpException extends RuntimeException {

    public int code=-100;
    private String url;

    public OkHttpException(String msg, int code){
        this(msg, code, null);
    }

    /**
     * @param msg
     * @param code
     * @param url 发生http系统异常时的URL，比如303、404
     * */
    public OkHttpException(String msg, int code, String url){
        super(msg);
        this.code = code;
        this.url = url;
    }

    /**
     * 显示后台给定的提示--使用
     * */
    public String getMessageTips(){
        return super.getMessage();
    }

    /**
     * 在HTTP发生异常时，比如404等返回码时，不为空
     * */
    public String getUrl(){
        return url;
    }

    @Override
    public String getMessage() {
        return super.getMessage()+" code = "+code;
    }

    @Override
    public String toString() {
        return super.toString()+" code = "+code;
    }

}
