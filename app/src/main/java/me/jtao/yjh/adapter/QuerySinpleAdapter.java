package me.jtao.yjh.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import me.jtao.yjh.R;
import me.jtao.yjh.bean.QueryResSimp;

/**
 * Created by jiangtao on 2016/8/22.
 */
public class QuerySinpleAdapter extends ArrayAdapter<String> {
    public QuerySinpleAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_simple_query, parent, false);
            TextView tv = (TextView) convertView.findViewById(R.id.textview);
            tv.setText(getItem(position));
        } else {
            TextView tv = (TextView) convertView.findViewById(R.id.textview);
            tv.setText(getItem(position));
        }
        return convertView;
    }
}
