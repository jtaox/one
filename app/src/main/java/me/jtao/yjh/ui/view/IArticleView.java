package me.jtao.yjh.ui.view;

import java.util.List;

import me.jtao.yjh.bean.AppArticle;

/**
 * Created by jiangtao on 2016/8/16.
 */
public interface IArticleView {
    void append(List<AppArticle> articleItem);

    void noMore();

    void refresh(List<AppArticle> articleItem);
}
