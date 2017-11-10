package cc.meiwen.ui.presenter;

import android.content.Context;
import android.content.Intent;

/**
 * Created by abc on 2017/11/2.
 */

public interface MvpView {

    Context getContext();

    void startActivity(Intent intent);

}
