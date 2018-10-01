package me.jtao.yjh.ui.view;

import java.util.List;

import me.jtao.yjh.bean.AppMeizhi;

/**
 * Created by jiangtao on 2016/8/9.
 */
public interface ITPView {
    void refreView(List<AppMeizhi> results);

    void moreData(List<AppMeizhi> results);

    void noMore();
}
