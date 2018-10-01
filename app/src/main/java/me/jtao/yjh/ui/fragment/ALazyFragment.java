package me.jtao.yjh.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiangtao on 2016/8/16.
 */
public abstract class ALazyFragment extends ABaseFragment {
    private boolean isVisible;
    private boolean isPrepared;
    private boolean isFirstVisible = true;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        isPrepared = true;
        onVisible();
        return view;
    }

    private void firstVisible() {
        onFirstVisible();
        isFirstVisible = false;
    }

    /**
     * fragment第一次启动
     */
    protected void onFirstVisible(){};

    /**
     * Fragment可见，onCreateView之前执行
     */
    protected void onVisible(){

        if(isPrepared && isVisible)
            lazyLoad();
        if(isFirstVisible && isPrepared && isVisible)
            firstVisible();

    }

    /**
     * Fragment可见，onCreateView之后执行
     */
    protected  void lazyLoad(){}

    protected void onInvisible(){}
}
