package me.jtao.yjh.presenter;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.API;
import me.jtao.yjh.bean.AppMusic;
import me.jtao.yjh.bean.Music;
import me.jtao.yjh.bean.MusicItem;
import me.jtao.yjh.bean.ResMusicList;
import me.jtao.yjh.bean.SingerInfo;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.ui.view.IMusicView;
import me.jtao.yjh.util.PlayController;
import me.jtao.yjh.util.ToastUtil;
import me.jtao.yjh.util.VisualizerUtil;
import me.jtao.yjh.widget.VisualizerView;

/**
 * Created by jiangtao on 2016/8/10.
 */
public class MusicPresenter {


    private IMusicView view;
    private boolean isLoadMore;
    private PlayController controll;
    private VisualizerUtil visulizerUtil;
    private VisualizerView visualizerView;


    public MusicPresenter(IMusicView view) {
        this.view = view;
        controll = PlayController.getInstance();
        init();
    }

    private void init() {
        controll.setChangeListener(new PlayController.OnPlayStateChangeListener() {
            @Override
            public void playing(int duration) {
                view.prePlay(duration);
            }

            @Override
            public void onBufferingUpdate(int i) {
            }

            @Override
            public void onError(int what, int extra) {
                //ToastUtil.makeText("error!! what -" + what + ", extra -" + extra);
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onCompletion() {
                // 音乐播放完毕回调  关闭动画  ？？？？这里会什么会多次调用
                //view.pauseAnim();
                view.hiddenBarAnim();
                Log.d("Sea", "onCompletion");

            }
        });
    }

    private int page = 1;

    public void loadData(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        String url = API.ULL_APP_MUSIC + (isLoadMore ? ++page : 1);
        // 先加载服务器数据
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<ResMusicList>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText("加载music信息失败" + e.getMessage());
            }

            @Override
            public void onResponse(ResMusicList response) {
                getDetailInfo(response.data);
            }
        });
    }

    public int getDuration() {
        return controll.getDuration();
    }

    public boolean isPlaying() {
        return controll.isPlaying();
    }

    private void getDetailInfo(final List<AppMusic> list) {

        new MyAsyncTask().execute(list);

        //InfoResultCallback callback = new InfoResultCallback();
        /*for (int i = 0; i < list.size(); i++) {
            MusicItem item = new MusicItem();

            AppMusic appMusic = list.get(i);

            // 取出需要显示的数据
            item.author = appMusic.author;
            item.desc = appMusic.desc;
            // 放入集合
            // 异步请求MP3详细数据，包含歌曲地址
            OkHttpClientManager.getAsyncStr(MessageFormat.format(API.URL_APP_MUSIC_INFO, appMusic.songid)
                    , new InfoResultCallback().setItem(item, list.size()));
        }*/
    }

    private enum PlayerState{
        playing,pause,stop;
    }

    private PlayerState state = PlayerState.stop;

    public void play(List<String> songUrl, String lrclink) {

        if (controll.isPlaying() && songUrl.contains(controll.getUrl()) && state == PlayerState.playing) {
            // 暂停
            controll.pause();
            view.pauseAnim();
            state = PlayerState.pause;
            Log.d("Sea", "暂停");
            return;
        }

        if (!controll.isPlaying() && songUrl.contains(controll.getUrl()) && state == PlayerState.pause) {
            //继续
            controll.start();
            view.nextStart();
            state = PlayerState.playing;
            Log.d("Sea", "继续");

            // 加载歌词
            if (TextUtils.isEmpty(lrclink)) {
                view.lrcIsNull();
            } else {
                loadLrc(lrclink);
            }
            //view.startLrc();

            return;
        }

        if (controll.isPlaying() && state == PlayerState.playing) {
            controll.reset();
            // 用户点击了新歌曲
            view.nextStart();
            Log.d("Sea", "新歌曲");
            for (int i = 0; i < songUrl.size(); i++) {
                try {
                    controll.play(songUrl.get(i));
                    Log.d("Sea", "play " + songUrl.get(i));
                    // 只要没有抛异常，跳出for循环
                    break;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 加载歌词
            if (TextUtils.isEmpty(lrclink)) {
                view.lrcIsNull();
            } else {
                loadLrc(lrclink);
            }
//            view.startLrc();
            return;

        }

        if (!controll.isPlaying() && state == PlayerState.stop) {
            view.nextStart();
            if (!TextUtils.isEmpty(controll.getUrl())) {
                controll.reset();
            }
            if (visulizerUtil == null) {
                visulizerUtil = new VisualizerUtil(view.getActivity(), controll.getMediaPlayer(), visualizerView);
            }
            state = PlayerState.playing;
            for (int i = 0; i < songUrl.size(); i++) {
                try {
                    controll.play(songUrl.get(i));
                    Log.d("Sea", "play " + songUrl.get(i));
                    // 只要没有抛异常，跳出for循环
                    break;
                } catch (IOException e) {
                    ToastUtil.makeText("异常" + e.getMessage());
                    e.printStackTrace();
                }
            }

            // 加载歌词
            if (TextUtils.isEmpty(lrclink)) {
                view.lrcIsNull();
            } else {
                loadLrc(lrclink);
            }
            view.startLrc();


        }

    }

    public int getCurrentPos() {
        return controll.getCurrentPos();
    }

    public void seekTo(int i) {
        controll.seekTo(i);
    }

    private void loadLrc(String lrclink) {

        new AsyncTask<String, Void, InputStream>() {

            @Override
            protected InputStream doInBackground(String... strings) {

                try {
                    HttpURLConnection conn = (HttpURLConnection) new URL(strings[0]).openConnection();
                    // 返回流
                    return conn.getInputStream();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(InputStream inputStream) {
                // 填充歌词
                if (inputStream == null) {
                    view.lrcIsError();
                } else {
                    view.setFileInputStream(inputStream);
                    Log.d("Sea", "加载歌词成功");
                }
            }
        }.execute(lrclink);

    }

    public void pause() {
        controll.pause();
    }

    public void contact(VisualizerView visualizerView) {
        this.visualizerView = visualizerView;
    }

    private class MyAsyncTask extends AsyncTask<List<AppMusic>, Void, List<MusicItem>> {

        @Override
        protected List<MusicItem> doInBackground(List<AppMusic>... lists) {
            List<MusicItem> musicItems = new ArrayList<>();

            List<AppMusic> list = lists[0];

            for (int i = 0; i < list.size(); i++) {
                MusicItem item = new MusicItem();

                AppMusic appMusic = list.get(i);

                // 取出需要显示的数据
                item.author = appMusic.author;
                item.desc = appMusic.desc;
                item.time = appMusic.createtime;

                try {
                    String result = OkHttpClientManager.getAsString(MessageFormat.format(API.URL_APP_MUSIC_INFO,
                            appMusic.songid));

                    Gson gson = new Gson();
                    Music response = gson.fromJson(result, Music.class);

                    item.singer = response.songinfo.author;
                    item.title = response.songinfo.title;
                    // 这个图片链接可能没有，如果没有，获取歌手图片
                    item.artist = response.songinfo.artist_500_500;

                    // 歌词
                    item.lrclink = response.songinfo.lrclink;

                    if (TextUtils.isEmpty(item.artist)) {
                        Gson singerinfo = new Gson();
                        SingerInfo singer = singerinfo.fromJson(OkHttpClientManager.getAsString(MessageFormat.format(API.URL_APP_SINGER_INFO,
                                response.songinfo.ting_uid)), SingerInfo.class);
                        item.artist = singer.avatar_middle;
                    }

                    for (Music.SongurlBean.UrlBean url : response.songurl.url) {
                        item.songUrl.add(url.file_link);
                    }
                    musicItems.add(item);

                } catch (IOException e) {
                    e.printStackTrace();
                    ToastUtil.makeText("获取歌曲详细信息出错" + e.getMessage());
                }
            }
            return musicItems;
        }

        @Override
        protected void onPostExecute(List<MusicItem> musicItems) {
            super.onPostExecute(musicItems);
            dataComplete(musicItems);
        }
    }

    private void dataComplete(List<MusicItem> musicItems) {
        if (isLoadMore) {
            // 加载更多
            if (musicItems.size() > 0) {
                // 获取更多数据成功
                view.append(musicItems);
            } else {
                // 没有更多数据
                view.noMore();
            }
        } else {
            // 刷新视图
            view.refresh(musicItems);
            page = 1;
        }
    }

}
