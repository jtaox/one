package me.jtao.yjh.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jiangtao on 2016/8/16.
 */
public class AppArticle implements Parcelable{
    public int id;
    public String author;
    public String title;
    public String des;
    public String content;
    public int isshow;
    public int ischeck;
    public long createtime;

    protected AppArticle(Parcel in) {
        id = in.readInt();
        author = in.readString();
        title = in.readString();
        des = in.readString();
        content = in.readString();
        isshow = in.readInt();
        ischeck = in.readInt();
        createtime = in.readLong();
    }

    public static final Creator<AppArticle> CREATOR = new Creator<AppArticle>() {
        @Override
        public AppArticle createFromParcel(Parcel in) {
            return new AppArticle(in);
        }

        @Override
        public AppArticle[] newArray(int size) {
            return new AppArticle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(author);
        parcel.writeString(title);
        parcel.writeString(des);
        parcel.writeString(content);
        parcel.writeInt(isshow);
        parcel.writeInt(ischeck);
        parcel.writeLong(createtime);
    }
}
