package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alex.develop.entity.*;
import com.alex.develop.ui.CandleView;

/**
 * Created by alex on 15-6-15.
 * 绘制蜡烛图（K线图）
 */
public class CandleActivity extends BaseActivity implements CandleView.onCandlestickSelectedListener {

    public static void start(Context context, int index, int from) {
        Intent intent = new Intent();
        intent.setClass(context, CandleActivity.class);
        intent.putExtra(CandleActivity.INDEX, index);
        intent.putExtra(CandleActivity.FROM, from);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_activity);
        Analyzer.setLoadView(findViewById(R.id.loading));

        Bundle bundle = getIntent().getExtras();
        int index = bundle.getInt(INDEX);
        int from = bundle.getInt(FROM);

        if(COLLECT_LIST == from) {
            stock = Analyzer.getCollectStockList(false).get(index);
        } else {
            stock = Analyzer.getStockList().get(index);
        }

        ActionBar actionBar = getActionBar();
        if(null != actionBar) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.stock_header_layout);
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (null == holder) {
                holder = new ViewHolder();
            }

            View view = actionBar.getCustomView();

            holder.candlePrice = (TextView) view.findViewById(R.id.candlePrice);
            holder.candleOpen = (TextView) view.findViewById(R.id.candleOpen);
            holder.candleHigh = (TextView) view.findViewById(R.id.candleHigh);
            holder.candleVolume = (TextView) view.findViewById(R.id.candleVolume);

            holder.candleIncrease = (TextView) view.findViewById(R.id.candleIncrease);
            holder.candleTurnover = (TextView) view.findViewById(R.id.candleTurnover);
            holder.candleLow = (TextView) view.findViewById(R.id.candleLow);
            holder.candleMoney = (TextView) view.findViewById(R.id.candleMoney);

            TextView stockName = (TextView) view.findViewById(R.id.stockName);
            stockName.setText(stock.getName());
            TextView stockCode = (TextView) view.findViewById(R.id.stockCode);
            stockCode.setText(stock.getCode());

            updateHeaderInfo(stock.getToday());
        }

        candleView = (CandleView) findViewById(R.id.candleView);
        candleView.setOnCandlestickSelectedListener(this);
        candleView.setStock(stock);
    }

    @Override
    public void onSelected(Candlestick candlestick) {
        updateHeaderInfo(candlestick);
    }

    public void updateHeaderInfo(Candlestick candlestick) {

        if(null == candlestick) {
            String data = getString(R.string.stock_default);

            holder.candlePrice.setText(String.format(getString(R.string.candle_price), data));
            holder.candleOpen.setText(String.format(getString(R.string.candle_open), data));
            holder.candleHigh.setText(String.format(getString(R.string.candle_high), data));
            holder.candleVolume.setText(String.format(getString(R.string.candle_volume), data));

            holder.candleIncrease.setText(String.format(getString(R.string.candle_increase), data));
            holder.candleTurnover.setText(String.format(getString(R.string.candle_turnover), data));
            holder.candleLow.setText(String.format(getString(R.string.candle_low), data));
            holder.candleMoney.setText(String.format(getString(R.string.candle_money), data));

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

    public static final String INDEX = "stockIndex";
    public static final String FROM = "from";
    public static final int STOCK_LIST = 0;
    public static final int COLLECT_LIST = 1;
    public static final int OTHER_LIST = 2;

    private Stock stock;
    private ViewHolder holder;
    private CandleView candleView;
}
