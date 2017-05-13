package com.whatisjava.training;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by whatisjava on 15/9/10.
 */
public class MainAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<String> mItems;

    public MainAdapter(Context context) {
        super();
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mItems == null ? 0 : mItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder orderHolder;
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.item_main, parent, false);
            orderHolder = new ViewHolder();
            orderHolder.textView = (TextView) convertView.findViewById(R.id.text_view);
            orderHolder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            convertView.setTag(orderHolder);
        } else {
            orderHolder = (ViewHolder) convertView.getTag();
        }
        String str = (String) getItem(position);
        orderHolder.textView.setText(str);
        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }

    public void assign(ArrayList<String> items) {
        mItems = items;
        notifyDataSetChanged();
    }

}
