package me.jtao.yjh.presenter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.Request;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.net.Proxy;

import me.jtao.yjh.API;
import me.jtao.yjh.bean.AppResult;
import me.jtao.yjh.bean.AppYjh;
import me.jtao.yjh.manage.OkHttpClientManager;
import me.jtao.yjh.ui.view.IUserView;
import me.jtao.yjh.util.ToastUtil;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class SendDataPresenter {
    private IUserView view;

    public SendDataPresenter(IUserView view) {
        this.view = view;
    }

    public void sendYJH(AppYjh yjh) {
        // 发送数据前调用
        view.preSendData();
        try {

            // 判断是否要发送图片
            if(!TextUtils.isEmpty(yjh.imgpath))
                OkHttpClientManager.postAsyn(API.URL_APP_SEND_YJH, new OkHttpClientManager.ResultCallback<String>() {

                        @Override
                        public void onError(Request request, Exception e) {
                            ToastUtil.makeText("发送异常-" + e.getMessage());
                            view.addDataFailed(null);
                        }

                        @Override
                        public void onResponse(String response) {
                            Gson res = new Gson();
                            AppResult appResult = res.fromJson(response, AppResult.class);
                            if(appResult.state.equals("200")){
                                view.addDataSuccess();
                            } else {
                                ToastUtil.makeText(appResult.data.result);
                                view.addDataFailed(appResult);
                            }

                        }
                    }, new File(yjh.imgpath), "mFile",
                    new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_CONTENT, yjh.content),
                    new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_AUTHOR, yjh.author));
            else
                OkHttpClientManager.postAsyn(API.URL_APP_SEND_YJH, new OkHttpClientManager.ResultCallback<AppResult>() {
                    @Override
                    public void onError(Request request, Exception e) {
                        ToastUtil.makeText("发送异常-" + e.getMessage());
                        view.addDataFailed(null);
                    }

                    @Override
                    public void onResponse(AppResult appResult) {
                        if(appResult.state.equals("200")){
                            view.addDataSuccess();
                        } else {
                            ToastUtil.makeText(appResult.data.result);
                            view.addDataFailed(appResult);
                        }
                    }
                }, new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_CONTENT, yjh.content),
                        new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_AUTHOR, yjh.author));

        } catch (IOException e) {
            e.printStackTrace();
            ToastUtil.makeText(e.getMessage());
        }
    }

/*
        OkHttpClientManager.postAsyn(API.URL_APP_SEND_YJH, new OkHttpClientManager.ResultCallback<AppResult>() {
            @Override
            public void onError(Request request, Exception e) {
                // TODO: 2016/8/22
                view.addDataFailed(null);
                ToastUtil.makeText(e.getMessage());
            }

            @Override
            public void onResponse(AppResult response) {
                if(response.state.equals("200")){
                    //ToastUtil.makeText(response.data.result);
                    // 添加成功
                    view.addDataSuccess();
                } else {
                    view.addDataFailed(response);
                }
            }
        }, new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_CONTENT, yjh.content),
                new OkHttpClientManager.Param(API.URL_APP_SEND_PARAM_AUTHOR, yjh.author));

*/

    public String getMimeType(String filePath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        String mime = "text/plain";
        if (filePath != null) {
            try {
                mmr.setDataSource(filePath);
                mime = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
            } catch (IllegalStateException e) {
                return mime;
            } catch (IllegalArgumentException e) {
                return mime;
            } catch (RuntimeException e) {
                return mime;
            }
        }
        return mime;
    }
}

