package me.jtao.yjh.util;

import android.media.MediaPlayer;

import java.io.IOException;
import java.io.LineNumberReader;
import java.util.EmptyStackException;

/**
 * Created by jiangtao on 2016/8/11.
 */
public class PlayController {
    private static PlayController controller;
    private MediaPlayer mediaPlayer;

    private OnPlayStateChangeListener changeListener;

    public static final int MEDIA_STATE_PLAYING = 101;
    public static final int MEDIA_STATE_STOP = 102;
    public static final int MEDIA_STATE_PAUSE = 103;

    private int currentMediaState = MEDIA_STATE_STOP;

    private String url;

    private PlayController() {
        mediaPlayer = new MediaPlayer();
        initPlayer();
    }

    public void setChangeListener(OnPlayStateChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void seekTo(int i){
        mediaPlayer.seekTo(i);
    }

    public String getUrl() {
        return url;
    }

    private void initPlayer() {
        mediaPlayer.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
                if (changeListener != null)
                    changeListener.onBufferingUpdate(i);
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                currentMediaState = MEDIA_STATE_PLAYING;
                if(changeListener != null)
                    changeListener.playing(mediaPlayer.getDuration());
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int what, int extra) {
                if(changeListener != null)
                    changeListener.onError(what, extra);
                return false;
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if(changeListener != null)
                    changeListener.onCompletion();
            }
        });

    }

    public synchronized static PlayController getInstance() {
        if (controller == null) {
            controller = new PlayController();
        }
        return controller;
    }

    public void play(String url) throws IOException {
        //mediaPlayer.release();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
        this.url = url;
    }

    public void pause(){
        mediaPlayer.pause();
        if(changeListener != null)
            changeListener.onPause();
    }


    public void reset() {
        //mediaPlayer.release();
       mediaPlayer.reset();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void start() {
        mediaPlayer.start();
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public int getDuration(){
        return mediaPlayer.getDuration();
    }

    public static interface OnPlayStateChangeListener {
        void playing(int duration);
        void onBufferingUpdate(int i);
        void onError(int what, int extra);
        void onPause();
        void onCompletion();
    }

    public int getCurrentPos(){
        return mediaPlayer.getCurrentPosition();
    }


}
