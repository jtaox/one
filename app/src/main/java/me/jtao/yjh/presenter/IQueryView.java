package me.jtao.yjh.presenter;

import java.util.List;

import me.jtao.yjh.bean.QueryAll;
import me.jtao.yjh.bean.QueryResSimp;

/**
 * Created by jiangtao on 2016/8/22.
 */
public interface IQueryView {
    void refreshSimple(QueryResSimp response);

    void refreshDetailList(QueryAll response, boolean isMore);
}
