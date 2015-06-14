package com.alex.develop.entity;

import android.util.Log;

/**
 * Created by alex on 15-5-22.
 * 蜡烛线（K线）
 */
public class Candlestick extends BaseObject {

    public Candlestick() {}

    public Candlestick(String[] yahoo) {
        fromYahoo(yahoo);
    }

    public float getOpen() {
        return open;
    }

    public void setOpen(float open) {
        this.open = open;
    }

    public void setClose(float close) {
        this.close = close;
    }

    public float getLastClose() {
        return lastClose;
    }

    public String getLastCloseString() {
        return String.format("%.2f", lastClose);
    }

    public void setLastClose(float lastClose) {
        this.lastClose = lastClose;
    }

    public float getHigh() {
        return high;
    }

    public void setHigh(float high) {
        this.high = high;
    }

    public float getLow() {
        return low;
    }

    public void setLow(float low) {
        this.low = low;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public float getClose() {
        return close;
    }

    public String getCloseString() {
        return String.format("%.2f", close);
    }

    public float getIncrease() {
        return increase = 100 * (close - lastClose) / lastClose;
    }

    public String getIncreaseString() {
        increase = 100 * (close - lastClose) / lastClose;
        return String.format("%.2f", increase) + "%";
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void fromYahoo(String[] data) {
        date = data[0];// 日期
        open = Float.valueOf(data[1]);// 开盘价
        high = Float.valueOf(data[2]);// 最高价
        low = Float.valueOf(data[3]);// 最低价
        close = Float.valueOf(data[4]);// 收盘价
        volume = Long.valueOf(data[5]);// 成交量
        adjClose = Float.valueOf(data[6]);// Adj Close

        Log.d("Print", date + ", " + open + ", " + high + ", " + low + ", " + close + ", " + volume);
    }

    private float open;// 开盘价
    private float close;// 收盘价
    private float adjClose;// Adj Close
    private float lastClose;// 昨日收盘价
    private float high;// 最高价
    private float low;// 最低价
    private float increase;// 当日涨幅
    private long volume;// 成交量
    private float money;// 成交额
    private String date;// 日期
}
