package com.alex.develop.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.stockanalyzer.R;

/**
 * Created by alex on 15-7-19.
 * 股票对应某日的行情数据
 */
public class StockHeader extends LinearLayout {

    public StockHeader(Context context, AttributeSet attrs) {
        super(context, attrs);

        int padding = (int) getContext().getResources().getDimension(R.dimen.stock_header_padding);
        setPadding(padding, padding, padding, padding);

        LayoutInflater.from(context).inflate(R.layout.stock_header_layout, this);

        if (null == holder) {
            holder = new ViewHolder();
        }

        holder.candlePrice = (TextView) findViewById(R.id.candlePrice);
        holder.candleOpen = (TextView) findViewById(R.id.candleOpen);
        holder.candleHigh = (TextView) findViewById(R.id.candleHigh);
        holder.candleVolume = (TextView) findViewById(R.id.candleVolume);

        holder.candleIncrease = (TextView) findViewById(R.id.candleIncrease);
        holder.candleTurnover = (TextView) findViewById(R.id.candleTurnover);
        holder.candleLow = (TextView) findViewById(R.id.candleLow);
        holder.candleMoney = (TextView) findViewById(R.id.candleMoney);
    }

    public void setStock(String name, String code) {
        TextView stockName = (TextView) findViewById(R.id.stockName);
        stockName.setText(name);
        TextView stockCode = (TextView) findViewById(R.id.stockCode);
        stockCode.setText(code);
    }

    public void updateHeaderInfo(Candlestick candlestick) {

        if(null == candlestick) {
            String data = getContext().getString(R.string.stock_default);

            holder.candlePrice.setText(String.format(getContext().getString(R.string.candle_price), data));
            holder.candleOpen.setText(String.format(getContext().getString(R.string.candle_open), data));
            holder.candleHigh.setText(String.format(getContext().getString(R.string.candle_high), data));
            holder.candleVolume.setText(String.format(getContext().getString(R.string.candle_volume), data));

            holder.candleIncrease.setText(String.format(getContext().getString(R.string.candle_increase), data));
            holder.candleTurnover.setText(String.format(getContext().getString(R.string.candle_turnover), data));
            holder.candleLow.setText(String.format(getContext().getString(R.string.candle_low), data));
            holder.candleMoney.setText(String.format(getContext().getString(R.string.candle_money), data));

            return;
        }

        // 最新
        String price = String.format(getContext().getString(R.string.candle_price), candlestick.getCloseString());
        holder.candlePrice.setText(price);

        // 今开
        String open = String.format(getContext().getString(R.string.candle_open), candlestick.getOpenString());
        holder.candleOpen.setText(open);

        // 最高
        String high = String.format(getContext().getString(R.string.candle_high), candlestick.getHighString());
        holder.candleHigh.setText(high);

        // 成交量
        String volume = String.format(getContext().getString(R.string.candle_volume), candlestick.getVolumeString());
        holder.candleVolume.setText(volume);

        // 涨幅
        String increase = String.format(getContext().getString(R.string.candle_increase), candlestick.getIncreaseString());
        holder.candleIncrease.setText(increase);

        // 换手
        String turnover = String.format(getContext().getString(R.string.candle_turnover), candlestick.getTurnoverString());
        holder.candleTurnover.setText(turnover);

        // 最低
        String low = String.format(getContext().getString(R.string.candle_low), candlestick.getLowString());
        holder.candleLow.setText(low);

        // 成交额
        String money = String.format(getContext().getString(R.string.candle_money), candlestick.getMoneyString());
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

    private ViewHolder holder;
}
