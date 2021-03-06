package me.jtao.yjh.ui.fragment.yj;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.presenter.YJPresenter;
import me.jtao.yjh.ui.activity.MainActivity;
import me.jtao.yjh.ui.fragment.ABaseFragment;
import me.jtao.yjh.ui.fragment.ALazyFragment;
import me.jtao.yjh.ui.view.IYJView;
import me.jtao.yjh.util.RecycleViewDivider;
import me.jtao.yjh.util.SystemUtils;
import me.jtao.yjh.util.ToastUtil;
import me.jtao.yjh.util.UIUtils;

/**
 * Created by jiangtao on 2016/8/1.
 */
public class YJFragment extends ALazyFragment implements IYJView {
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
        // 分割线
        recyclerView.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL));

        recyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {}
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
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.loadData(false);
            }
        });

    }

    @Override
    protected void onFirstVisible() {
        presenter = new YJPresenter(this);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            // 刷新数据
            refreshLayout.setRefreshing(true);
            presenter.loadData(false);
        }
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
            View view = inflater.inflate(R.layout.item_yj_fragment, null);
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
        private ImageView ivImage;
        public Myholder(View itemView) {
            super(itemView);
            itemView.setOnLongClickListener(this);
            tvContent = (TextView) itemView.findViewById(R.id.tv_text);
            tvAuthor = (TextView) itemView.findViewById(R.id.tv_author);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_img);
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
            recyclerView.setLoadingMoreEnabled(true);
            tvContent.setText(data.content);
            tvAuthor.setText(data.author);
            tvTime.setText(SystemUtils.unix2Str(data.createtime));
            if(TextUtils.isEmpty(data.imgpath)){
                ivImage.setVisibility(View.GONE);
            } else {
                ivImage.setVisibility(View.VISIBLE);
                Log.d("Sea", "path " + data.imgpath);
                Picasso.with(activity)
                        .load(data.imgpath)
                        .resize((int)UIUtils.dip2px(250),(int)UIUtils.dip2px(250))
                        .centerCrop()
                        .into(ivImage);
            }
        }

    }


}
