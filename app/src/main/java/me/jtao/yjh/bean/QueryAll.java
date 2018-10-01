package me.jtao.yjh.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QueryAll {

    public String query;
    public int is_artist;
    public int is_album;
    public String rs_words;
    /**
     * total : 119
     * rn_num : 30
     */

    public PagesBean pages;

    public List<SongListBean> song_list;

    public static class PagesBean {
        public String total;
        public String rn_num;
    }

    public static class SongListBean implements Parcelable{
        public String title;
        public String song_id;
        public String author;
        public String artist_id;
        public String all_artist_id;
        public String album_title;
        public String appendix;
        public String album_id;
        public String lrclink;
        public String resource_type;
        public String content;
        public int relate_status;
        public int havehigh;
        public String copy_type;
        public String all_rate;
        public int has_mv;
        public int has_mv_mobile;
        public String mv_provider;
        public int charge;
        public String toneid;
        public String info;
        public int data_source;
        public int learn;

        protected SongListBean(Parcel in) {
            title = in.readString();
            song_id = in.readString();
            author = in.readString();
            artist_id = in.readString();
            all_artist_id = in.readString();
            album_title = in.readString();
            appendix = in.readString();
            album_id = in.readString();
            lrclink = in.readString();
            resource_type = in.readString();
            content = in.readString();
            relate_status = in.readInt();
            havehigh = in.readInt();
            copy_type = in.readString();
            all_rate = in.readString();
            has_mv = in.readInt();
            has_mv_mobile = in.readInt();
            mv_provider = in.readString();
            charge = in.readInt();
            toneid = in.readString();
            info = in.readString();
            data_source = in.readInt();
            learn = in.readInt();
        }

        public static final Creator<SongListBean> CREATOR = new Creator<SongListBean>() {
            @Override
            public SongListBean createFromParcel(Parcel in) {
                return new SongListBean(in);
            }

            @Override
            public SongListBean[] newArray(int size) {
                return new SongListBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(title);
            parcel.writeString(song_id);
            parcel.writeString(author);
            parcel.writeString(artist_id);
            parcel.writeString(all_artist_id);
            parcel.writeString(album_title);
            parcel.writeString(appendix);
            parcel.writeString(album_id);
            parcel.writeString(lrclink);
            parcel.writeString(resource_type);
            parcel.writeString(content);
            parcel.writeInt(relate_status);
            parcel.writeInt(havehigh);
            parcel.writeString(copy_type);
            parcel.writeString(all_rate);
            parcel.writeInt(has_mv);
            parcel.writeInt(has_mv_mobile);
            parcel.writeString(mv_provider);
            parcel.writeInt(charge);
            parcel.writeString(toneid);
            parcel.writeString(info);
            parcel.writeInt(data_source);
            parcel.writeInt(learn);
        }
    }
}
