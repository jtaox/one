package me.jtao.yjh.bean;

import java.util.List;

/**
 * Created by jiangtao on 2016/8/10.
 * 用户上传的每首歌曲的具体信息
 */
public class Music {
    public SongurlBean songurl;

    public int error_code;

    public SonginfoBean songinfo;

    public static class SongurlBean {


        public List<UrlBean> url;

        public static class UrlBean {
            public int file_size;
            public String file_extension;
            public String file_link;
        }
    }

    public static class SonginfoBean {
        public String album_1000_1000;
        public String album_500_500;
        public String artist_500_500;
        public String pic_radio;
        public String language;
        public String lrclink;
        public String pic_big;
        public String pic_premium;
        public String artist_480_800;
        public String country;
        public String artist_1000_1000;
        public String artist_640_1136;
        public String publishtime;
        public String pic_small;
        public String album_no;
        public String song_id;
        public String author;
        public String title;
        public String ting_uid;
    }
}


