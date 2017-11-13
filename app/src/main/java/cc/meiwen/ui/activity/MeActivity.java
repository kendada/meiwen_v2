package cc.meiwen.ui.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cc.meiwen.R;
import cc.meiwen.ui.fragment.MeFragment;

/**
 * Created by abc on 2017/11/11.
 */

public class MeActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_layout);

        onFragmentCommit(new MeFragment());
    }

    private void onFragmentCommit(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }

}
