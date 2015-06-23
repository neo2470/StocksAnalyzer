package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.ui.CandleView;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

/**
 * Created by alex on 15-6-15.
 */
public class CandleActivity extends BaseActivity implements CandleView.onCandlestickSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_activity);

        candleView = (CandleView) findViewById(R.id.candleView);
        int index = getIntent().getExtras().getInt(ARG_STOCK_INDEX);
        Analyzer analyzer = (Analyzer) getApplication();
        stock = analyzer.getStockList().get(index);

        // 自定义ActionBar内容
        createActionBarView();

        // 请求历史数据
        new AsyncSohuStockHistory().execute("20150601", "20150603", Enum.Period.Day.toString());
    }

    @Override
    public void onSelected(Candlestick candlestick) {

    }

    private void createActionBarView() {

        ActionBar actionBar = getActionBar();
        if(null != actionBar) {
            View view = LayoutInflater.from(this).inflate(R.layout.candle_action_bar_layout, null);

            TextView stockName = (TextView) view.findViewById(R.id.stockName);
            stockName.setText(stock.getName());
            TextView stockCode = (TextView) view.findViewById(R.id.stockCode);
            stockCode.setText(stock.getCode());

            int orientation = getResources().getConfiguration().orientation;
            if(Configuration.ORIENTATION_LANDSCAPE == orientation) {


                if (null == holder) {
                    holder = new ViewHolder();
                }

                holder.candlePrice = (TextView) view.findViewById(R.id.candlePrice);
                holder.candleOpen = (TextView) view.findViewById(R.id.candleOpen);
                holder.candleHigh = (TextView) view.findViewById(R.id.candleHigh);
                holder.candleVolume = (TextView) view.findViewById(R.id.candleVolume);

                holder.candleIncrease = (TextView) view.findViewById(R.id.candleIncrease);
                holder.candleTurnover = (TextView) view.findViewById(R.id.candleTurnover);
                holder.candleLow = (TextView) view.findViewById(R.id.candleLow);
                holder.candleMoney = (TextView) view.findViewById(R.id.candleMoney);
            }

            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(view);
        }
    }

    private void updateHeaderInfo(Candlestick candlestick) {

        int orientation = getResources().getConfiguration().orientation;
        if(Configuration.ORIENTATION_PORTRAIT == orientation) {
            return;
        }

        // 最新
        String price = String.format(getString(R.string.candle_price), candlestick.getCloseString());
        holder.candlePrice.setText(price);

        // 今开
        String open = String.format(getString(R.string.candle_open), candlestick.getOpenString());
        holder.candleOpen.setText(open);

        // 最高
        String high = String.format(getString(R.string.candle_high), candlestick.getHighString());
        holder.candleHigh.setText(high);

        // 成交量
        String volume = String.format(getString(R.string.candle_volume), candlestick.getVolumeString());
        holder.candleVolume.setText(volume);

        // 涨幅
        String increase = String.format(getString(R.string.candle_increase), candlestick.getIncreaseString());
        holder.candleIncrease.setText(increase);

        // 换手
        String turnover = String.format(getString(R.string.candle_turnover), candlestick.getTurnoverString());
        holder.candleTurnover.setText(turnover);

        // 最低
        String low = String.format(getString(R.string.candle_low), candlestick.getLowString());
        holder.candleLow.setText(low);

        // 成交额
        String money = String.format(getString(R.string.candle_money), candlestick.getMoneyString());
        holder.candleMoney.setText(money);

    }

    public static final String ARG_STOCK_INDEX = "stockIndex";

    private Stock stock;
    private CandleView candleView;
    private ViewHolder holder;
    private class ViewHolder {

        TextView candlePrice;
        TextView candleOpen;
        TextView candleHigh;
        TextView candleVolume;

        TextView candleIncrease;
        TextView candleTurnover;
        TextView candleLow;
        TextView candleMoney;
    }

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

            Candlestick candlestick = stock.getCandlesticks().get(0);
            updateHeaderInfo(candlestick);
        }

        private ImageView loadView;
    }
}
