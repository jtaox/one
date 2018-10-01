package me.jtao.yjh.ui.view;

import java.util.List;

import me.jtao.yjh.bean.AppYjh;

/**
 * Created by jiangtao on 2016/8/2.
 */
public interface IYJView {
    void showErrorView();
    void refreView(List<AppYjh> datas);
    void updateImgDesc(String desc);

    void append(List<AppYjh> yjhs);
}
