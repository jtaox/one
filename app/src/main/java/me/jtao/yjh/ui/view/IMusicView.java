package me.jtao.yjh.ui.view;

import android.app.Activity;

import java.io.InputStream;
import java.util.List;

import me.jtao.yjh.bean.AppMusic;
import me.jtao.yjh.bean.MusicItem;

/**
 * Created by jiangtao on 2016/8/10.
 */
public interface IMusicView {
    void append(List<MusicItem> data);

    void noMore();

    void refresh(List<MusicItem> data);

    /**
     * 暂停动画
     */
    void pauseAnim();

    /**
     * 启动新的view动画
     */
    void nextStart();

    Activity getActivity();

    void hiddenBarAnim();

    void lrcIsNull();

    void setFileInputStream(InputStream inputStream);

    void lrcIsError();

    /**  */
    void startLrc();
    /** 开始播放前，设置进度条 */
    void prePlay(int duration);
}
