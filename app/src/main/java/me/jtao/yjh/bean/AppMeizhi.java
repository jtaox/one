package me.jtao.yjh.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtao on 2016/8/9.
 */
public class AppMeizhi {
    public String url;
    public String publishedAt;
    public static List<AppMeizhi> getInstance(JSONArray array) throws JSONException {
        List<AppMeizhi> list = new ArrayList<>();
        for(int i=0; i<array.length();i++){
            AppMeizhi meizhi = new AppMeizhi();
            JSONObject object = array.getJSONObject(i);
            meizhi.url = object.getString("url");
            meizhi.publishedAt = object.getString("publishedAt");
            list.add(meizhi);
        }
        return list;
    }
}
