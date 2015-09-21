package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alex.develop.adapter.NewsListAdapter;
import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-9-21.
 */
public class NewsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        ListView newsList = (ListView) view.findViewById(R.id.newsList);
        newsList.setAdapter(new NewsListAdapter());
        return view;
    }
}
