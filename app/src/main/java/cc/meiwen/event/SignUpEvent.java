package cc.meiwen.event;

/**
 * Created by abc on 2017/11/13.
 */

public class SignUpEvent {

    public final boolean isSuccess;

    public SignUpEvent(boolean isSuccess){
        this.isSuccess = isSuccess;
    }

}
