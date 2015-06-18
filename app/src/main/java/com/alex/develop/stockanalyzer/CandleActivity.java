package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
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

        ActionBar actionBar = getActionBar();
        if(null != actionBar) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.candle_action_bar_layout);

            View view = actionBar.getCustomView();
            
        }

        // 请求历史数据
        new AsyncSohuStockHistory().execute("20150601", "20150603", Enum.Period.Day.toString());
    }

    public static final String ARG_STOCK_INDEX = "stockIndex";

    private Stock stock;
    private CandleView candleView;

    private class AsyncSohuStockHistory extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadView = (ImageView) findViewById(R.id.loading);
            Animation anim = AnimationUtils.loadAnimation(CandleActivity.this, R.anim.loading_data);
            loadView.setVisibility(View.VISIBLE);
            loadView.startAnimation(anim);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String url = StockDataAPIHelper.getSohuHistoryUrl(stock.getCode(), params[0], params[1], params[2]);
            Log.d("Print", url);
            String data = NetworkHelper.getWebContent(url, StockDataAPIHelper.SOHU_CHARSET);
            Log.d("Print", data);

            stock.formSohu(data);

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            loadView.setVisibility(View.GONE);
            loadView.clearAnimation();
            candleView.setCandles(stock.getCandlesticks());
        }

        private ImageView loadView;
    }
}
