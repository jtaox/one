package me.jtao.yjh.presenter;

import android.util.Log;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import me.jtao.yjh.API;
import me.jtao.yjh.bean.AppMeizhi;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.ui.view.ITPView;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/9.
 */
public class TPPresenter {
    private ITPView view;

    public TPPresenter(ITPView view) {
        this.view = view;
    }

    /**
     * 加载数据
     * @param page 页
     * @param isMore 是否为加载更多，如果是，根据根据页数加载数据，否则重新加载第一页
     */
    public void loadData(int page, final boolean isMore) {
        OkHttpClientManager.getAsyn(API.URL_APP_MEIZHI + page, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error").equals("false")) {
                        List<AppMeizhi> results = AppMeizhi.getInstance(obj.getJSONArray("results"));
                        if (isMore) {
                            if (results.size() == 0) {
                                // 没有更多数据
                                Log.d("Sea", "没有更多数据" + results.size());
                                view.noMore();
                            } else {
                                Log.d("Sea", "加载更多数据" + results.size());
                                view.moreData(results);
                            }
                        } else {
                            view.refreView(results);
                        }

                    } else {
                        // TODO: 2016/8/9
                        ToastUtil.makeText("服务器相应成功，error");
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 2016/8/9
                    ToastUtil.makeText("解析错误");
                }
            }
        });
    }
}
