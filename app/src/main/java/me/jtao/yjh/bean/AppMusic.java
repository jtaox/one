package me.jtao.yjh.bean;

import com.google.gson.JsonObject;

import java.util.List;

/**
 * Created by jiangtao on 2016/8/10.
 * 用户上传的信息，包含songid
 */
public class AppMusic {
    public int id;
    public String author;
    public String  songid;
    public String desc;
    public int isshow;
    public int ischeck;
    public long createtime;

    //public static List<AppMusic> newInstance(JsonObject response) {

        /*for(int i = 0; i< response.)
        return null;*/
    //}
}
