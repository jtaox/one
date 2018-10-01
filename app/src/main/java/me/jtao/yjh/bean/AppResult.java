package me.jtao.yjh.bean;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class AppResult {

    /**
     * state : 401
     * data : {"result":"参数有误"}
     */

    public String state;
    /**
     * result : 参数有误
     */

    public DataBean data;

    public static class DataBean {
        public String result;
    }
}
