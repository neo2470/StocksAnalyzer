package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alex.develop.adapter.NewsListAdapter;
import com.alex.develop.entity.News;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.task.QueryLatestNews;

import java.util.ArrayList;

/**
 * Created by alex on 15-9-21.
 */
public class NewsFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news_fragment, container, false);

        if(null == news) {
            news = new ArrayList<>();
        } else {
            news.clear();
        }

        ListView newsList = (ListView) view.findViewById(R.id.newsList);
        final NewsListAdapter adapter = new NewsListAdapter(news);
        newsList.setAdapter(adapter);

        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(act, position+"", Toast.LENGTH_SHORT).show();
            }
        });

        new QueryLatestNews(news){
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                adapter.notifyDataSetChanged();
            }
        }.execute(1);

        return view;
    }

    private ArrayList<News> news;
}
