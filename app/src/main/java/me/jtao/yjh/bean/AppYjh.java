package me.jtao.yjh.bean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangtao on 2016/8/5.
 */
public class AppYjh {
    public int id;
    public String content;
    public String author;
    public int isShow;
    public int isCheck;
    public long createtime;
    public String imgpath;

    public static List<AppYjh> getInstances(JSONArray array) throws JSONException {
        List<AppYjh> list = new ArrayList<>();
        for (int i = 0; i< array.length(); i++){
            JSONObject object = array.getJSONObject(i);
            AppYjh yjh = new AppYjh();
            yjh.id = object.getInt("id");
            yjh.author = object.getString("author");
            yjh.content = object.getString("content");
            yjh.isCheck = object.getInt("ischeck");
            yjh.isShow = object.getInt("isShow");
            yjh.createtime = object.getInt("createtime");
            yjh.imgpath = object.getString("imgpath");
            list.add(yjh);
        }
        return list;
    }
}
