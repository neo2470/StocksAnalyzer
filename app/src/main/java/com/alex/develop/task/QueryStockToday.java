package com.alex.develop.task;

import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.NetworkHelper;

/**
 * Created by alex on 15-7-17.
 * 查询一支或多支股票当日行情
 */

public class QueryStockToday extends AsyncTask<Stock, Void, Void> {

    public QueryStockToday(View load, BaseAdapter adapter) {
        this.load = load;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        Animation anim = AnimationUtils.loadAnimation(Analyzer.getContext(), R.anim.loading_data);
        load.setVisibility(View.VISIBLE);
        load.startAnimation(anim);
    }

    @Override
    protected Void doInBackground(Stock... params) {
        NetworkHelper.querySinaToday(params);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        load.setVisibility(View.GONE);
        load.clearAnimation();
        adapter.notifyDataSetChanged();
    }

    /**
     * 数据更新指示器，当查询股票行情时会使用该View播放动画
     * 以通知用户正在加在数据
     */
    private View load;

    /**
     * 相应ListView的Adapter，当查询到股票行情数据时，通过Adapter
     * 的notifyDataSetChanged()方法通知ListView刷新
     */
    private BaseAdapter adapter;
}
