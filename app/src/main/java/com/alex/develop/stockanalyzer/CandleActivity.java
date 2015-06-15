package com.alex.develop.stockanalyzer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alex.develop.entity.Stock;
import com.alex.develop.ui.CandleView;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

/**
 * Created by alex on 15-6-15.
 */
public class CandleActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_activity);

        candleView = (CandleView) findViewById(R.id.candleView);
        int index = getIntent().getExtras().getInt(ARG_STOCK_INDEX);
        Analyzer analyzer = (Analyzer) getApplication();
        stock = analyzer.getStockList().get(index);

        // 请求历史数据
        new AsyncStockHistory().execute();
    }

    public static final String ARG_STOCK_INDEX = "stockIndex";

    private Stock stock;
    private CandleView candleView;

    private class AsyncStockHistory extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadView = (ImageView) findViewById(R.id.loading);
            Animation anim = AnimationUtils.loadAnimation(CandleActivity.this, R.anim.loading_data);
            loadView.setVisibility(View.VISIBLE);
            loadView.startAnimation(anim);
        }

        @Override
        protected Void doInBackground(Void... params) {
            float[] data = NetworkHelper.queryHistory(stock, StockDataAPIHelper.YAHOO_HISTORY_START);
            Log.d("Print", data[0] + ", " + data[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadView.setVisibility(View.GONE);
            loadView.clearAnimation();
            candleView.setCandles(stock.getCandlesticks());
        }

        private ImageView loadView;
    }
}
