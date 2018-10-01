package me.jtao.yjh.ui.fragment;

import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.adapter.TPMeizhiAdapter;
import me.jtao.yjh.bean.AppMeizhi;
import me.jtao.yjh.presenter.TPPresenter;
import me.jtao.yjh.ui.activity.PictureActivity;
import me.jtao.yjh.ui.view.ITPView;
import me.jtao.yjh.util.DividerGridItemDecoration;

/**
 * Created by jiangtao on 2016/7/25.
 */
public class TPFragmentA extends ABaseFragment implements ITPView {

    private Toolbar toolbar;
    private XRecyclerView recyclerView;
    private TPPresenter presenter;
    private int page = 1;
    private TPMeizhiAdapter adapter;

    @Override
    protected View contentView() {
        return inflater.inflate(R.layout.fragment_tp, null);
    }

    @Override
    protected void init() {
        super.init();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("图片");
        toolbar.setTitleTextColor(Color.WHITE);

        recyclerView = (XRecyclerView) findViewById(R.id.rv_tp);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.addItemDecoration(new DividerGridItemDecoration(activity));
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(1, false);
                page=1;
            }

            @Override
            public void onLoadMore() {
                page++;
                presenter.loadData(page, true);
            }
        });
        presenter = new TPPresenter(this);
        presenter.loadData(page, false);
    }

    @Override
    public void refreView(List<AppMeizhi> results) {
        recyclerView.refreshComplete();
        if(adapter == null){
            adapter = new TPMeizhiAdapter(results);
            adapter.setOnItemClickListener(new TPMeizhiAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, String data) {
                    startActivity(PictureActivity.newIntent(activity, data));
                   // activity.overridePendingTransition(R.anim.scale_in, R.anim.scale_out);
                }
            });
            recyclerView.setAdapter(adapter);
        } else {
            adapter.setList(results);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void moreData(List<AppMeizhi> results) {
        if(adapter == null)
            return;
        adapter.append(results);
        recyclerView.loadMoreComplete();
    }

    @Override
    public void noMore() {
        recyclerView.loadMoreComplete();
        Snackbar.make(getView(), "没有更多数据了", Snackbar.LENGTH_SHORT)
                .setAction("知道了", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .show();
    }
}
