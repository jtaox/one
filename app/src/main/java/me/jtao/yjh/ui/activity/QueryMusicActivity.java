package me.jtao.yjh.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.adapter.QueryDetailAdapter;
import me.jtao.yjh.adapter.QuerySinpleAdapter;
import me.jtao.yjh.bean.QueryAll;
import me.jtao.yjh.bean.QueryResSimp;
import me.jtao.yjh.presenter.IQueryView;
import me.jtao.yjh.presenter.QueryPresenter;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QueryMusicActivity extends AppCompatActivity implements IQueryView {

    private ListView lvQueryResultDetail;
    private ListView lvQueryResultSimple;
    private EditText etInput;
    private ImageView ivQuery;
    private ImageView ivClose;

    private View loadMoreView;
    private View simpleHeadView;

    private QueryPresenter presenter;
    private List<String> simpList;
    private List<QueryAll.SongListBean> detailList;
    private QueryDetailAdapter adapter;
    private QueryAll response;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_music);
        findView();
        init();
        initListener();

    }

    private void init() {
        presenter = new QueryPresenter(this);
        loadMoreView = View.inflate(this, R.layout.layout_loadmore, null);
        simpleHeadView = View.inflate(this, R.layout.layout_simple_head, null);
        lvQueryResultDetail.addFooterView(loadMoreView);
        lvQueryResultSimple.addHeaderView(simpleHeadView);
        loadMoreView.setVisibility(View.GONE);
    }


    private boolean isLoadMore = false;

    private void initListener() {
        etInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String str = editable.toString();
                presenter.querySimple(str);

            }
        });

        lvQueryResultSimple.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                i = i - lvQueryResultSimple.getHeaderViewsCount();
                etInput.setText(simpList.get(i));
                presenter.queryDetail(simpList.get(i), false);
            }
        });

        lvQueryResultDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                QueryAll.SongListBean songListBean = detailList.get(i);
                Intent intent = new Intent();
                intent.putExtra("song_list", songListBean);
                setResult(Activity.RESULT_OK, intent);
                onBackPressed();
            }
        });

        lvQueryResultDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                            //防止多次加载
                            if (!isLoadMore) {
                                isLoadMore = true;
                                // 滑动到底部  获取数据
                                // 如果在setadapter方法后调用会抛异常
                                //lvQueryResultDetail.addFooterView(loadMoreView);
                                loadMoreView.setVisibility(View.VISIBLE);
                                presenter.queryDetail(null, true);
                            }
                        }

                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
            }

        });


        ivQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = etInput.getText().toString();
                if (TextUtils.isEmpty(query)) {
                    ToastUtil.makeText("请输入要查询的内容");
                    return;
                }
                presenter.queryDetail(query, false);
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etInput.setText("");
            }
        });


    }

    private void findView() {
        lvQueryResultDetail = (ListView) findViewById(R.id.lv_query_result_detil);
        lvQueryResultSimple = (ListView) findViewById(R.id.lv_query_result_simple);
        etInput = (EditText) findViewById(R.id.et_input);
        ivQuery = (ImageView) findViewById(R.id.iv_query);
        ivClose = (ImageView) findViewById(R.id.iv_close);
    }

    @Override
    public void refreshSimple(QueryResSimp response) {
        if (response == null || response.song == null)
            return;
        if (lvQueryResultSimple.getVisibility() == View.GONE)
            lvQueryResultSimple.setVisibility(View.VISIBLE);
        if (lvQueryResultDetail.getVisibility() == View.VISIBLE)
            lvQueryResultDetail.setVisibility(View.GONE);

        List<String> list = new ArrayList<>();
        for (QueryResSimp.SongBean bean : response.song) {
            String name = bean.songname;
            list.add(name);
        }
        this.simpList = list;
        lvQueryResultSimple.setAdapter(new QuerySinpleAdapter(this, 0, list));

    }

    @Override
    public void refreshDetailList(QueryAll response, boolean isMore) {
        if (response == null)
            return;
        this.response = response;
        List<QueryAll.SongListBean> song_list = response.song_list;
        if (lvQueryResultSimple.getVisibility() == View.VISIBLE)
            lvQueryResultSimple.setVisibility(View.GONE);
        if (lvQueryResultDetail.getVisibility() == View.GONE)
            lvQueryResultDetail.setVisibility(View.VISIBLE);
        if (isMore) {
            /*if (lvQueryResultDetail.getFooterViewsCount() > 0) {
                lvQueryResultDetail.removeFooterView(loadMoreView);
            }*/
            //adapter.addData(song_list);
            loadMoreView.setVisibility(View.GONE);
            this.detailList.addAll(song_list);
            // 恢复标志
            isLoadMore = false;
        } else {
            detailList = song_list;
            //lvQueryResultDetail.setAdapter(new QueryDetailAdapter(song_list, isMore));
            if(adapter == null){
                adapter = new QueryDetailAdapter(song_list, isMore);
                lvQueryResultDetail.setAdapter(adapter);
            } else {
                adapter.setList(song_list);
            }
        }
    }

    /*@Override
    public void refreshDetailList(List<QueryAll.SongBean> song) {
        if(song == null)
            return;
        if(lvQueryResultSimple.getVisibility() == View.VISIBLE)
            lvQueryResultSimple.setVisibility(View.GONE);
        if(lvQueryResultDetail.getVisibility() == View.GONE)
            lvQueryResultDetail.setVisibility(View.VISIBLE);
        this.detailList = song;
        lvQueryResultDetail.setAdapter(new QueryDetailAdapter(song));

    }*/
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
