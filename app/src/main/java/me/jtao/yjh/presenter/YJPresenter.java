package me.jtao.yjh.presenter;

import android.text.TextUtils;
import android.util.Log;

import com.squareup.okhttp.Request;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.API;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.bean.ImgDesc;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.ui.view.IYJView;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/2.
 */
public class YJPresenter {
    private IYJView view;
    public YJPresenter(IYJView view){
        this.view = view;
        initImgDesc();
    }

    private void initImgDesc() {
        OkHttpClientManager.getAsyn(API.URL_DRAWER_DESC, new OkHttpClientManager.ResultCallback<String>(){

            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText("获取数据失败," + e.toString());
            }
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if(jo.getInt("state") == 200){
                        ImgDesc desc = ImgDesc.getInstance(jo.getJSONObject("data"));
                        if(desc != null){
                            view.updateImgDesc(desc.content + (TextUtils.isEmpty(desc.content2) ? "" : ","+desc.content2));
                        } else{
                            // TODO: 2016/8/3 数据解析错误
                            ToastUtil.makeText("数据解析错误");
                        }
                    } else {
                        // TODO: 2016/8/3   查询数据有误，服务器返回错误信息，应该进行处理
                        ToastUtil.makeText("数据查询错误");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    ToastUtil.makeText("数据解析错误，返回数据不是json格式或者格式有误" + response);
                }
            }
        });
    }
    private int page = 1;
    public void showError(){
        view.showErrorView();
    }

    public void loadData(final boolean isLoadMore){
        String url = isLoadMore ? API.URL_APP_YJH + (++page) : API.URL_APP_YJH + 1;
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                view.showErrorView();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jo = new JSONObject(response);
                    if(jo.getInt("state") == 200){
                        List<AppYjh> yjhs = AppYjh.getInstances(jo.getJSONArray("data"));
                        if(isLoadMore){
                            // 添加到原有集合
                            view.append(yjhs);
                        } else {
                            view.refreView(yjhs);
                        }
                    } else {
                        // TODO: 2016/8/5
                        view.showErrorView();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // TODO: 2016/8/5
                    view.showErrorView();
                }
            }
        });
    }

}
