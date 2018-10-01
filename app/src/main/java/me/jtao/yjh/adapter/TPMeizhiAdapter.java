package me.jtao.yjh.adapter;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.List;

import me.jtao.yjh.BaseApplication;
import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppMeizhi;
import me.jtao.yjh.util.UIUtils;
import me.jtao.yjh.widget.ScaleImageView;

/**
 * Created by jiangtao on 2016/8/9.
 */
public class TPMeizhiAdapter extends RecyclerView.Adapter<TPMeizhiAdapter.MyViewHolder> implements View.OnClickListener {

    private List<AppMeizhi> list;
    private OnRecyclerViewItemClickListener listener;

    public TPMeizhiAdapter(List<AppMeizhi> list) {
        this.list = list;
    }

    public void append(List<AppMeizhi> list){
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }

    @Override
    public TPMeizhiAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = UIUtils.inflate(R.layout.item_tp_fragment);
        view.setOnClickListener(this);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TPMeizhiAdapter.MyViewHolder holder, int position) {
        holder.setIvMeizhi(list.get(position).url);
        holder.view.setTag(list.get(position).url);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppMeizhi> list) {
        this.list = list;
    }

    @Override
    public void onClick(View view) {
        if(listener != null)
            listener.onItemClick(view, (String) view.getTag());
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        public ImageView ivMeizhi;
        private View view;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            ivMeizhi = (ImageView) itemView.findViewById(R.id.iv_meizhi);
        }

        public void setIvMeizhi(String url){
                Picasso.with(BaseApplication.sContext)
                        .load(url)
                        .error(R.mipmap.detail_content_temp_icon)
                        .placeholder(R.mipmap.detail_content_temp_icon)
                        .into(ivMeizhi);

        }

    }
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(View view , String data);

    }

}

