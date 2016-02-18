package cc.meiwen.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * User: 山野书生(1203596603@qq.com)
 * Date: 2015-11-27
 * Time: 15:21
 * Version 1.0
 */

public class StateFragment extends Fragment {

    private Bundle saveState;

    private String tag = StateFragment.class.getSimpleName();

    public StateFragment(){
        super();
        if(getArguments() == null){
            setArguments(new Bundle());
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(!restoreStateFromArguments()){
            onFirstTimeLaunched();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveStateToArguments();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        saveStateToArguments();
    }

    public void onFirstTimeLaunched(){

    }

    private void saveStateToArguments(){
        if(getView() != null){
            saveState = saveState();
        }
        if(saveState != null){
            Bundle b = getArguments();
            if(b!=null){
                b.putBundle("internalSavedViewState8954201239547", saveState);
            }
        }
    }

    private boolean restoreStateFromArguments(){
        Bundle b = getArguments();
        if(b == null) return false;
        saveState = b.getBundle("internalSavedViewState8954201239547");
        if(saveState != null){
            restoreState();
            return true;
        }
        return false;
    }

    /**
     * 恢复状态
     * */
    private void restoreState(){
        if(saveState != null){
            onRestoreState(saveState);
        }
    }

    /**
     * 子类重写该方法恢复状态
     * */
    public void onRestoreState(Bundle saveInstanceState){

    }

    /**
     * 保存状态
     * */
    private Bundle saveState(){
        Bundle state = new Bundle();
        //保存数据
        onSaveState(state);
        return state;
    }

    /**
     * 子类重写该方法实现状态保存
     * */
    public void onSaveState(Bundle outState){

    }

}
