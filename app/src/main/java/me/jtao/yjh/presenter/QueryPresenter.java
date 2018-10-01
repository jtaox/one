package me.jtao.yjh.presenter;

import android.content.ComponentName;
import android.util.Log;

import com.squareup.okhttp.Request;

import java.text.MessageFormat;

import me.jtao.yjh.API;
import me.jtao.yjh.R;
import me.jtao.yjh.bean.QueryAll;
import me.jtao.yjh.bean.QueryResSimp;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QueryPresenter {
    private IQueryView view;
    private String name;

    public QueryPresenter(IQueryView view){
        this.view = view;
    }

    public void querySimple(String content){
        OkHttpClientManager.getAsyn(API.URL_APP_QUERY_SIMPLE + content, new OkHttpClientManager.ResultCallback<QueryResSimp>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText(e.getMessage());
            }

            @Override
            public void onResponse(QueryResSimp response) {
                view.refreshSimple(response);
            }
        });
    }

    int index =1;
    public void queryDetail(String name, final boolean isMore) {
        if(name == null){
            // 加载更多
        } else {
            this.name = name;
        }
        String url =MessageFormat.format(API.URL_APP_QUERY_DETAIL, this.name, isMore?++index : index);
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<QueryAll>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText(e.getMessage());
            }

            @Override
            public void onResponse(QueryAll response) {
                view.refreshDetailList(response, isMore);
            }
        });
    }
}
