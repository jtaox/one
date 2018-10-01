package me.jtao.yjh.util;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.audiofx.Equalizer;
import android.media.audiofx.Visualizer;

import me.jtao.yjh.widget.VisualizerView;

/**
 * Created by jiangtao on 2016/8/15.
 * 参考：http://www.apkbus.com/thread-182057-1-1.html
 */
public class VisualizerUtil {
    private static final float VISUALIZER_HEIGHT_DIP = 100f;//频谱View高度
    private VisualizerView view;

    private MediaPlayer mMediaPlayer;//音频
    private Visualizer mVisualizer;//频谱器
    private Equalizer mEqualizer; //均衡器

    public VisualizerUtil(Activity activity, MediaPlayer mediaPlayer, VisualizerView view) {
        this.view = view;
        this.mMediaPlayer = mediaPlayer;
        ///设置音频流 - STREAM_MUSIC：音乐回放即媒体音量
        activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        setupVisualizer();//添加频谱到界面
        //setupPlayButton();//添加按钮到界面
        mVisualizer.setEnabled(true);//false 则不显示s
        view.setEnabled(true);

    }

    /**
     * 生成一个VisualizerView对象，使音频频谱的波段能够反映到 VisualizerView上
     */
    private void setupVisualizer() {
        //实例化Visualizer，参数SessionId可以通过MediaPlayer的对象获得
        mVisualizer = new Visualizer(mMediaPlayer.getAudioSessionId());
        //采样 - 参数内必须是2的位数 - 如64,128,256,512,1024
        mVisualizer.setCaptureSize(Visualizer.getCaptureSizeRange()[1]);
        mEqualizer = new Equalizer(0, mMediaPlayer.getAudioSessionId());
        mEqualizer.setEnabled(true);
        //设置允许波形表示，并且捕获它
        view.setVisualizer(mVisualizer);
    }

    public void release() {
        mVisualizer.release();
        mEqualizer.release();
    }

}
