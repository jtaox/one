package me.jtao.yjh.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.BaseAdapter;

import me.jtao.yjh.API;
import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.bean.Music;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class SPUtil{
    private SharedPreferences sp;
    private static SPUtil util;
    private SPUtil(){
        sp = BaseApplication.sContext.getSharedPreferences("config", Context.MODE_PRIVATE);
    }

    public static synchronized SPUtil getInstance(){
        if(util == null)
            util = new SPUtil();
        return util;
    }

    public String getNickname(){
        return sp.getString("nickname", API.DEFAULT_NICKNAME);
    }

    public void setNickname(String name){
        sp.edit().putString("nickname", name).apply();
    }

    public void setYjhContent(String content){
        sp.edit().putString("yjh_content", content).apply();
    }

    public String getYjhContent(){
        return sp.getString("yjh_content", "");
    }

    public void setMusicDesc(String musicDesc) {
        sp.edit().putString("music_desc", musicDesc).apply();
    }

    public String getMusicDesc() {
        return sp.getString("music_desc", "");
    }
}
