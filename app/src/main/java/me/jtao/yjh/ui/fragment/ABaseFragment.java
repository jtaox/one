package me.jtao.yjh.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;

import me.jtao.yjh.R;
import me.jtao.yjh.ui.activity.MainActivity;

/**
 * Created by Administrator on 2016/7/25.
 * 基类Fragment，所有Fragment继承本类
 * 提供基础layout：ll_base 所有子类可以在此layout上添加布局。也可以复写onCreateView重写
 */
public abstract class ABaseFragment extends Fragment {
    protected View rootView;
    protected LayoutInflater inflater;
    protected MainActivity activity;
    private String pageTitle;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.inflater = inflater;
        activity = (MainActivity) getActivity();
        rootView = contentView();
        init();
        return rootView;
    }

    /**
     * inflate view之后执行，可以执行获取view对象操作
     */
    protected void init() {
    }


    /**
     * Fragment视图
     * @return
     */
    protected abstract View contentView();


    protected View findViewById(int id){
        return rootView.findViewById(id);
    }

    /*protected void startToolbarHideAnim(long duration){
        activity.startToolbarHideAnim(duration);
    }

    protected void startToolbarVisibileAnim(long duration){
        activity.startToolbarVisibileAnim(duration);
    }*/

    public String getPageTitle() {
        return pageTitle;
    }

    public ABaseFragment setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
        return this;
    }

}
