package me.jtao.yjh.ui.fragment.yj;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.adapter.TPMeizhiAdapter;
import me.jtao.yjh.adapter.YPArticleAdapter;
import me.jtao.yjh.bean.AppArticle;
import me.jtao.yjh.presenter.ArticlePresenter;
import me.jtao.yjh.ui.activity.ArticleDetailActivity;
import me.jtao.yjh.ui.fragment.ABaseFragment;
import me.jtao.yjh.ui.fragment.ALazyFragment;
import me.jtao.yjh.ui.view.IArticleView;
import me.jtao.yjh.util.RecycleViewDivider;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by jiangtao on 2016/8/1.
 */
public class YPFragment extends ALazyFragment implements IArticleView {

    private ArticlePresenter presenter;
    private XRecyclerView recyclerView;
    private SwipeRefreshLayout refreshLayout;
    private YPArticleAdapter adapter;
    @Override
    protected View contentView() {
        View view = inflater.inflate(R.layout.fragment_yj_yp, null);
        return view;
    }

    @Override
    protected void init() {
        recyclerView = (XRecyclerView) findViewById(R.id.rv_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL));
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);

    }

    @Override
    protected void onFirstVisible() {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(true);
            }
        });
        presenter = new ArticlePresenter(this);
        presenter.loadData(false);
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    public void append(List<AppArticle> articleItem) {
        if(adapter == null)
            return;
        adapter.append(articleItem);
    }

    @Override
    public void noMore() {
        Snackbar.make(getView(), "没有更多数据了", Snackbar.LENGTH_SHORT)
                .show();
    }

    @Override
    public void refresh(List<AppArticle> articleItem) {
        if(adapter == null){
            adapter = new YPArticleAdapter(articleItem);
            adapter.setListener(new TPMeizhiAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, String data) {
                    Intent intent = new Intent(activity, ArticleDetailActivity.class);
                    intent.putExtra("article", (AppArticle)view.getTag());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        } else{
            adapter.setList(articleItem);
        }
        if(refreshLayout.isRefreshing())
            refreshLayout.setRefreshing(false);
    }
}
