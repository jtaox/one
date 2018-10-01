package me.jtao.yjh.ui.view;

import me.jtao.yjh.bean.AppResult;

/**
 * Created by jiangtao on 2016/8/22.
 */
public interface IUserView {
    void addDataSuccess();

    void addDataFailed(AppResult response);

    void preSendData();

    void unknow(String s);
}
