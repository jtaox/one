package me.jtao.yjh.bean;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by jiangtao on 2016/8/3.
 * 抽屉的图片描述
 * {
     "state": 200,
     "data": {
         "c": "5",
         "content": "aaa",
         "content2": "bbb",
         "author": "ab",
         "createtime": "1470207484"
         }
     }
 */
public class ImgDesc {
    public int id;
    public String content;
    public String content2;
    public String author;
    public long createtime;
    public static ImgDesc getInstance(JSONObject jo){
        try {
            ImgDesc desc = new ImgDesc();
            desc.id = jo.getInt("id");
            desc.content = jo.getString("content");
            desc.content2 = jo.getString("content2");
            desc.author = jo.getString("author");
            desc.createtime = jo.getLong("createtime");
            return desc;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
