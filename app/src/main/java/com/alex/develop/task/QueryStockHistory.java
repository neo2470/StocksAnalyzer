package com.alex.develop.task;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

/**
 * Created by alex on 15-7-18.
 * 查询某支股票历史行情数据
 */
public class QueryStockHistory extends AsyncTask<String, Void, Void> {

    public QueryStockHistory(Stock stock) {
        this.stock = stock;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Animation anim = AnimationUtils.loadAnimation(Analyzer.getContext(), R.anim.loading_data);
        Analyzer.getLoadView().setVisibility(View.VISIBLE);
        Analyzer.getLoadView().startAnimation(anim);
    }

    @Override
    protected Void doInBackground(String... params) {
        String url = StockDataAPIHelper.getSohuHistoryUrl(stock.getCode(), params[0], params[1], params[2]);
        String data = NetworkHelper.getWebContent(url, StockDataAPIHelper.SOHU_CHARSET);
        int result = stock.formSohu(data);
        Log.d("Print", result+"");
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

        Analyzer.getLoadView().setVisibility(View.GONE);
        Analyzer.getLoadView().clearAnimation();
    }

    private Stock stock;
}
