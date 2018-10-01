package me.jtao.yjh.ui.fragment.yj;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.io.InputStream;
import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.adapter.YJViewPagerAdapter;
import me.jtao.yjh.adapter.YSMusicAdapter;
import me.jtao.yjh.bean.MusicItem;
import me.jtao.yjh.manage.LyricManager;
import me.jtao.yjh.presenter.MusicPresenter;
import me.jtao.yjh.ui.fragment.ALazyFragment;
import me.jtao.yjh.ui.view.IMusicView;
import me.jtao.yjh.util.RecycleViewDivider;
import me.jtao.yjh.util.UIUtils;
import me.jtao.yjh.widget.LyricView;
import me.jtao.yjh.widget.StereoView;
import me.jtao.yjh.widget.VisualizerView;

/**
 * Created by jiangtao on 2016/8/1.
 */
public class YSFragment extends ALazyFragment implements IMusicView, YSMusicAdapter.OnControllClickListener, SwipeRefreshLayout.OnRefreshListener, LyricManager.OnProgressChangedListener, YSMusicAdapter.OnPlayViewVisableListener {

    private XRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private LinearLayout llControllBar;

    private VisualizerView visualizerView;
    private LyricView lyricView;
    private TextView lyriNoti;

    private MusicPresenter presenter;
    private YSMusicAdapter adapter;
    private int position;
    private LinearLayoutManager linearLayoutManager;
    private LyricManager lyricManager;
    private Handler handler;
    private YSMusicAdapter.MyViewHolder holder;

    @Override
    protected View contentView() {
        View view = inflater.inflate(R.layout.fragment_yj_ys, null);
        return view;
    }

    @Override
    protected void init() {
        llControllBar = (LinearLayout) findViewById(R.id.ll_controllbar);
        visualizerView = (VisualizerView) findViewById(R.id.vv_visualizer);
        visualizerView.post(new Runnable() {
            @Override
            public void run() {
                controllbarAnim();
            }
        });

        lyricView = (LyricView) findViewById(R.id.lyric_view);
        lyriNoti = (TextView) findViewById(R.id.tv_lrc_noti);

        recyclerView = (XRecyclerView) findViewById(R.id.rv_yj);
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setPullRefreshEnabled(false);
        // 分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                /*if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    ToastUtil.makeText("拖拽");
                } else if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    ToastUtil.makeText("空闲");
                } else if(newState == RecyclerView.SCROLL_STATE_SETTLING){
                    ToastUtil.makeText("松手 滑动");
                }*/
                //
                if (presenter.isPlaying()) {
                    if (position >= linearLayoutManager.findFirstCompletelyVisibleItemPosition() &&
                            position <= linearLayoutManager.findLastVisibleItemPosition()) {
                        // 不显示
                        //llControllBar.setVisibility(View.INVISIBLE);
                        if (!isHidden)
                            controllbarAnim();
                    } else {
                        // 显示
//                        llControllBar.setVisibility(View.VISIBLE);
                        if (isHidden)
                            controllbarAnim();
                    }
                }

            }
        });

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {
                presenter.loadData(true);
            }
        });

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        refreshLayout.setOnRefreshListener(this);

    }

    @Override
    protected void onFirstVisible() {
        presenter = new MusicPresenter(this);
        presenter.loadData(false);
        // 关联
        presenter.contact(visualizerView);
        lyricManager = LyricManager.getInstance(activity);
        lyricManager.setOnProgressChangedListener(this);

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what){
                    case 0:
                        // 刷新
                        lyricManager.setCurrentTimeMillis(presenter.getCurrentPos());
                        if(holder != null){
                            holder.tvProgress.setText(UIUtils.getProgressText(presenter.getCurrentPos()));
                            // TODO: 2016/9/7 每次都会刷新，暂时放这
                            holder.tvMax.setText(UIUtils.getProgressText(presenter.getDuration()));
                            holder.seekBar.setProgress(presenter.getCurrentPos());
                            Log.d("Sea", "refresh" + presenter.getCurrentPos());
                        } else {
                            Log.d("Sea", "holder is null");
                        }
                        handler.sendEmptyMessageDelayed(0, 200);
                        break;
                }
            }
        };

    }

    @Override
    public void append(List<MusicItem> data) {
        if (adapter == null)
            return;
        adapter.append(data);
        recyclerView.loadMoreComplete();
    }

    @Override
    public void noMore() {
        Snackbar.make(getView(), "没有更多数据了", Snackbar.LENGTH_SHORT)
                .show();
        recyclerView.loadMoreComplete();
        recyclerView.setLoadingMoreEnabled(false);
    }

    @Override
    public void refresh(List<MusicItem> data) {
        if (adapter == null) {
            adapter = new YSMusicAdapter(data, recyclerView);
            adapter.setListener(this);
            adapter.setVisableListener(this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setList(data);
        }
        refreshLayout.setRefreshing(false);
        recyclerView.setLoadingMoreEnabled(true);
    }

   /* @Override
    public void startAnim() {
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        holder.startAnim();
        adapter.setIsPlayPos(position);
    }*/

    @Override
    public void pauseAnim() {
        Log.d("Sea", "pauseAnim");
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        // 暂停动画
        if(holder != null)
            holder.stopAnim();
        adapter.setIsPlayPos(-1);
    }

    @Override
    public void nextStart() {
        // 貌似XRecyclerView 没有处理getChildAt这个方法
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        holder.startAnim();
        adapter.setIsPlayPos(position);
    }

    @Override
    public void hiddenBarAnim() {
        if (!isHidden) {
            controllbarAnim();
        }
    }

    @Override
    public void lrcIsNull() {
        if(lyriNoti.getVisibility() == View.GONE)
            lyriNoti.setVisibility(View.VISIBLE);
        lyriNoti.setText("没有找到这首歌的歌词哦~");
        lyricView.setVisibility(View.GONE);
    }

    @Override
    public void setFileInputStream(InputStream inputStream) {
        if(lyricView.getVisibility() == View.GONE)
            lyricView.setVisibility(View.VISIBLE);
        lyricManager.setFileStream(inputStream);
    }

    @Override
    public void lrcIsError() {

    }

    @Override
    public void startLrc() {
        // 开启歌词
        handler.sendEmptyMessage(0);
    }

    @Override
    public void prePlay(int duration) {
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(this.position);
        if(holder != null) {
            holder.tvMax.setText(UIUtils.getProgressText(duration));
            holder.seekBar.setMax(duration);
        }
    }

    @Override
    public void onClick(View view, int position) {
        MusicItem item = (MusicItem) view.getTag();
        Log.d("Sea", "点击了 " + position);
        if (presenter.isPlaying()) {  // 如果正在播放，需要暂停上一个动画
            YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(this.position);
            if (holder != null) {
                // 停止动画
                holder.stopAnim();
                // 返回主page
                holder.sv_stereo.setItem(YSMusicAdapter.ITEM_PATE_MAIN);
                //adapter.notifyItemChanged(position);
            }
        }
        this.position = position;
        presenter.play(item.songUrl, item.lrclink);
        // 暂时测试
        adapter.setCurrentPage(YSMusicAdapter.ITEM_PATE_CONTROLL);
        ((StereoView)view.findViewById(R.id.sv_stereo)).setItem(2);
        holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(this.position);
//        holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForLayoutPosition(this.position);
    }

    @Override
    public void onSeekBarProgressChangedListener(SeekBar seekBar, int i, boolean b, int layoutPosition) {
        if(b) { // b  是否用户操作
            presenter.seekTo(i);
        }
    }


    private boolean isHidden = false;

    private void controllbarAnim() {
        llControllBar.animate()
                .translationY(isHidden ? 0 : -llControllBar.getHeight())
                .setInterpolator(new DecelerateInterpolator(2))
                .start();
        isHidden = !isHidden;
    }

    @Override
    public void onRefresh() {
        presenter.loadData(false);
    }


    @Override
    public void onProgressChanged(String singleLine, boolean refresh) {

    }

    @Override
    public void onProgressChanged(SpannableStringBuilder stringBuilder, int lineNumber, boolean refresh) {
        lyricView.setText(stringBuilder);
        lyricView.setCurrentPosition(lineNumber);
    }

    @Override
    public void onPlayViewVisableListener(YSMusicAdapter.MyViewHolder holder) {
        this.holder = holder;
    }
}
