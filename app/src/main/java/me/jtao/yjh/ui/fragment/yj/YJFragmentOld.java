package me.jtao.yjh.ui.fragment.yj;

import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.presenter.YJPresenter;
import me.jtao.yjh.ui.activity.MainActivity;
import me.jtao.yjh.ui.fragment.ABaseFragment;
import me.jtao.yjh.ui.view.IYJView;
import me.jtao.yjh.util.SystemUtils;

/**
 * Created by jiangtao on 2016/8/1.
 */
public class YJFragmentOld extends ABaseFragment implements IYJView {
    private XRecyclerView recyclerView;
    private YJPresenter presenter;
    private SwipeRefreshLayout refreshLayout;

    private Myadapter myadapter;

    @Override
    protected View contentView() {
        return inflater.inflate(R.layout.fragment_yj_yj, null);
    }

    @Override
    protected void init() {

        recyclerView = (XRecyclerView) findViewById(R.id.rv_yj);
        recyclerView.setLayoutManager(new LinearLayoutManager(activity));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.setPullRefreshEnabled(false);
        recyclerView.setLoadingMoreEnabled(true);
        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {}
            @Override
            public void onLoadMore() {
                presenter.loadData(true);
            }
        });


        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(false);
            }
        });

        presenter = new YJPresenter(this);
        //presenter.loadData();
        presenter.loadData(false);

    }

    @Override
    public void showErrorView() {
        findViewById(R.id.rl_error_view).setVisibility(View.VISIBLE);
    }

    @Override
    public void refreView(List<AppYjh> datas) {
        if(myadapter == null){
            myadapter = new Myadapter(datas);
            recyclerView.setAdapter(myadapter);
        } else {
            myadapter.setList(datas);
            myadapter.notifyDataSetChanged();
        }
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void updateImgDesc(String desc) {
        ((MainActivity)getActivity()).setTxtDesc(desc);
    }

    @Override
    public void append(List<AppYjh> yjhs) {
        if(myadapter == null)
            return;
        myadapter.append(yjhs);
    }

    class Myadapter extends RecyclerView.Adapter<Myholder>{

        private List<AppYjh> list;

        public Myadapter(List<AppYjh> list){
            this.list = list;
        }

        public void setList(List<AppYjh> list) {
            this.list = list;
        }

        @Override
        public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = (View) inflater.inflate(R.layout.item_yj_fragment, null);
            return new Myholder(view);
        }

        @Override
        public void onBindViewHolder(Myholder holder, int position) {
            holder.setData(list.get(position));
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void append(List<AppYjh> yjhs) {
            recyclerView.loadMoreComplete();
            if(yjhs.size() == 0){
                Snackbar.make(getView(), "没有数据了", Snackbar.LENGTH_SHORT)
                .show();
                recyclerView.setLoadingMoreEnabled(false);
                return;
            }
            list.addAll(yjhs);
            notifyDataSetChanged();
        }
    }

    class Myholder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView tvContent;
        private TextView tvAuthor;
        private TextView tvTime;
        public Myholder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            tvContent = (TextView) itemView.findViewById(R.id.tv_text);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
        }

        @Override
        public boolean onLongClick(View view) {
            SystemUtils.setClipboard(tvContent.getText().toString(), getView());
            return false;
        }

        public void setData(AppYjh data) {
            refreshData(data);
        }

        private void refreshData(AppYjh data) {
            tvContent.setText(data.content);
            tvAuthor.setText(data.author);
            tvTime.setText(SystemUtils.unix2Str(data.createtime));
        }

    }


}
