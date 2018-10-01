/*
package me.jtao.yjh.ui.fragment.yj;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.adapter.YSMusicAdapter;
import me.jtao.yjh.bean.MusicItem;
import me.jtao.yjh.presenter.MusicPresenter;
import me.jtao.yjh.ui.fragment.ABaseFragment;
import me.jtao.yjh.ui.view.IMusicView;
import me.jtao.yjh.widget.VisualizerView;

*/
/**
 * Created by jiangtao on 2016/8/1.
 *//*

public class YSFragmentOld extends ABaseFragment implements IMusicView, YSMusicAdapter.OnControllClickListener, SwipeRefreshLayout.OnRefreshListener {

    private XRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;

    private LinearLayout llControllBar;

    private VisualizerView visualizerView;

    private MusicPresenter presenter;
    private YSMusicAdapter adapter;
    private int position;
    private LinearLayoutManager linearLayoutManager;

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
//                visualizerView.scrollBy(0, -visualizerView.getHeight());
            }
        });

        recyclerView = (XRecyclerView) findViewById(R.id.rv_yj);
        linearLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                */
/*if(newState == RecyclerView.SCROLL_STATE_DRAGGING){
                    ToastUtil.makeText("拖拽");
                } else if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    ToastUtil.makeText("空闲");
                } else if(newState == RecyclerView.SCROLL_STATE_SETTLING){
                    ToastUtil.makeText("松手 滑动");
                }*//*


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
        refreshLayout.setOnRefreshListener(this);
        presenter = new MusicPresenter(this);
        presenter.loadData(false);
        // 关联
        presenter.contact(visualizerView);
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
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setList(data);
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void startAnim() {
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        holder.startAnim();
    }

    @Override
    public void pauseAnim() {
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        // 暂停动画
        holder.stopAnim();
    }

    @Override
    public void nextStart() {
        // 貌似XRecyclerView 没有处理getChildAt这个方法
        YSMusicAdapter.MyViewHolder holder = (YSMusicAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        holder.stopAnim();
        holder.startAnim();
    }

    @Override
    public void hiddenBarAnim() {
        if (!isHidden) {
            controllbarAnim();
        }
    }

    @Override
    public void onClick(View view, int position) {
        MusicItem item = (MusicItem) view.getTag();
        this.position = position;
        presenter.play(item.songUrl);
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
}
*/
