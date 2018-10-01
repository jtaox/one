package me.jtao.yjh.util;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jiangtao on 2016/8/10.
 */
public class HeaderAndFooterWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private SparseArray<View> headerViews = new SparseArray<>();
    private SparseArray<View> footViews = new SparseArray<>();


    private int BASE_HEADER = 10000;
    private int BASE_FOOTER = 20000;

    private RecyclerView.Adapter innerAdapter;

    public HeaderAndFooterWrapper(RecyclerView.Adapter innerAdapter) {
        this.innerAdapter = innerAdapter;
    }

    private boolean isHeaderViewPos(int position){
        return position < getHeadersCount();
    }

    private boolean isFooterViewPos(int position){
        return position >= getHeadersCount() + getRealItemCount();
    }

    public void addHeaderView(View view){
        headerViews.put(headerViews.size() + BASE_HEADER, view);
    }

    public void addFooterView(View view){
        footViews.put(footViews.size() + BASE_FOOTER, view);
    }

    public int getHeadersCount(){
        return headerViews.size();
    }

    public int getFooterCount(){
        return footViews.size();
    }

    public int getRealItemCount(){
        return innerAdapter.getItemCount();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(headerViews.get(viewType) != null){
           // RecyclerView.ViewHolder viewHolder = RecyclerView.ViewHolder.
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return getRealItemCount() + getHeadersCount() + getFooterCount();
    }

    @Override
    public int getItemViewType(int position) {
        if(isHeaderViewPos(position)){
            return headerViews.keyAt(position);
        } else if(isFooterViewPos(position)){
            return footViews.keyAt(position);
        }
        return -1;
    }
}
