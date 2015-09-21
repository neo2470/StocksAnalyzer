package com.alex.develop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15年9月21日.
 */
public class NewsListAdapter extends BaseAdapter {
    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(Analyzer.getContext());
            convertView = inflater.inflate(R.layout.news_item, null);

            ViewHolder holder = new ViewHolder();
            holder.newsImg = (ImageView) convertView.findViewById(R.id.newsImg);
            holder.newsTitle = (TextView) convertView.findViewById(R.id.newsTitle);
            holder.newsDesc = (TextView) convertView.findViewById(R.id.newsDesc);
            holder.newsTimeASource = (TextView) convertView.findViewById(R.id.newsTimeASource);
            convertView.setTag(holder);
        }

        return convertView;
    }

    private static  class ViewHolder {
        ImageView newsImg;
        TextView newsTitle;
        TextView newsDesc;
        TextView newsTimeASource;
    }
}
