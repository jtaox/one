package me.jtao.yjh.adapter;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.AppArticle;
import me.jtao.yjh.util.SystemUtils;

/**
 * Created by jiangtao on 2016/8/16.
 */
public class YPArticleAdapter extends RecyclerView.Adapter<YPArticleAdapter.MyViewHolder> {
    private List<AppArticle> list;
    private TPMeizhiAdapter.OnRecyclerViewItemClickListener listener;
    public YPArticleAdapter(List<AppArticle> list) {
        this.list = list;
    }

    public void setListener(TPMeizhiAdapter.OnRecyclerViewItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public YPArticleAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_yp_fragment, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.tvDesc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View clickView) {
                if(listener != null)
                    listener.onItemClick(view, null);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(YPArticleAdapter.MyViewHolder holder, int position) {
        AppArticle appArticle = list.get(position);
        holder.tvAuthor.setText(appArticle.author);
        holder.tvTime.setText(SystemUtils.unix2Str(appArticle.createtime));
        holder.tvDesc.setText(TextUtils.isEmpty(appArticle.des) ? appArticle.content : appArticle.des);
        holder.tvTitle.setText(appArticle.title);
        holder.view.setTag(appArticle);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setList(List<AppArticle> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void append(List<AppArticle> articleItem) {
        list.addAll(articleItem);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public View view;
        public TextView tvAuthor, tvTitle, tvDesc, tvTime;
        public MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            init();
        }

        private void init() {
            tvAuthor = (TextView) view.findViewById(R.id.tv_author);
            tvTitle = (TextView) view.findViewById(R.id.tv_article_title);
            tvDesc = (TextView) view.findViewById(R.id.tv_article_desc);
            tvTime = (TextView) view.findViewById(R.id.tv_article_time);
        }

    }

}
