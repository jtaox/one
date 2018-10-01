package me.jtao.yjh.adapter;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.QueryAll;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QueryDetailAdapter extends BaseAdapter {


    private List<QueryAll.SongListBean> song;

    public QueryDetailAdapter(List<QueryAll.SongListBean> song, boolean isMore) {
        this.song = song;
    }

    @Override
    public int getCount() {
        return song.size();
    }

    @Override
    public Object getItem(int i) {
        return song.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        QueryAll.SongListBean songBean = song.get(i);
        ViewHolder holder;

        if(view == null){
            view = LayoutInflater.from(viewGroup.getContext())
                   .inflate(R.layout.item_detail_query, viewGroup, false);
            holder = new ViewHolder();
            holder.tv_music_name = (TextView) view.findViewById(R.id.tv_music_name);
            holder.tv_singer = (TextView) view.findViewById(R.id.tv_singer);
            view.setTag(holder);
        } else{
            holder = (ViewHolder) view.getTag();
        }
        holder.tv_music_name.setText(Html.fromHtml(songBean.title));
        holder.tv_singer.setText(Html.fromHtml(songBean.author));
        return view;
    }

    public void addData(List<QueryAll.SongListBean> song_list) {
        this.song.addAll(song_list);
        notifyDataSetChanged();
    }

    public void setList(List<QueryAll.SongListBean> list) {
        this.song = list;
        notifyDataSetChanged();
    }

    private class ViewHolder{
        public TextView tv_music_name, tv_singer;
    }

}
