package me.jtao.yjh.presenter;


import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.API;
import me.jtao.yjh.bean.AppArticle;
import me.jtao.yjh.bean.AppMusic;
import me.jtao.yjh.bean.Music;
import me.jtao.yjh.bean.MusicItem;
import me.jtao.yjh.bean.ResArticle;
import me.jtao.yjh.bean.ResMusicList;
import me.jtao.yjh.bean.SingerInfo;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.ui.view.IArticleView;
import me.jtao.yjh.ui.view.IMusicView;
import me.jtao.yjh.util.PlayController;
import me.jtao.yjh.util.ToastUtil;
import me.jtao.yjh.util.VisualizerUtil;
import me.jtao.yjh.widget.VisualizerView;

/**
 * Created by jiangtao on 2016/8/10.
 */
public class ArticlePresenter {

    private IArticleView view;
    private boolean isLoadMore;



    public ArticlePresenter(IArticleView view) {
        this.view = view;
    }


    private int page = 1;

    public void loadData(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        String url = API.URL_APP_ARTICLE + (isLoadMore ? page+=1 : 1);

       /* OkHttpClientManager.getAsyncStr(url, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                Log.d("Sea", "-->" + response);
            }
        });*/

        // 先加载服务器数据
        OkHttpClientManager.getAsyn(url, new OkHttpClientManager.ResultCallback<ResArticle>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.makeText("加载music信息失败" + e.getMessage());
            }
            @Override
            public void onResponse(ResArticle response) {
                //getDetailInfo(response.data);
                if(response.state == 200)
                    dataComplete(response.data);
                else  // TODO: 2016/8/16
                    ToastUtil.makeText("服务器异常");
            }
        });
    }

    private void dataComplete(List<AppArticle> articleItem) {
        if (isLoadMore) {
            // 加载更多
            if (articleItem.size() > 0) {
                // 获取更多数据成功
                view.append(articleItem);
            } else {
                // 没有更多数据
                view.noMore();
            }
        } else {
            // 刷新视图
            view.refresh(articleItem);
            page = 1;
        }
    }

}
