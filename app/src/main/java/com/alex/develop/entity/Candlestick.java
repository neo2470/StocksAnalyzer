package com.alex.develop.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

import org.json.JSONArray;

/**
 * Created by alex on 15-5-22.
 * 蜡烛线（K线）
 */
public class Candlestick extends BaseObject {

    public Candlestick() {
    }

    public Candlestick(String[] yahoo) {
        fromYahoo(yahoo);
    }

    public Candlestick(JSONArray data) {
        fromSohu(data);
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

    public void setLastClose(float lastClose) {
        this.lastClose = lastClose;
    }

    public float getLastClose() {
        return lastClose;
    }

    public String getLastCloseString() {
        return String.format("%.2f", lastClose);
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
        return increase;
    }

    public String getIncreaseString() {
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

    public void initialize() {
        increase = 100 * (close - lastClose) / lastClose;
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

    public void fromSohu(JSONArray data) {
        date = data.optString(0);// 日期
        open = (float) data.optDouble(1);// 开盘价
        close = (float) data.optDouble(2);// 收盘价
        vary = (float) data.optDouble(3);// 涨跌

        // 涨幅(%)
        String inStr = data.optString(4);
        inStr = inStr.substring(0, inStr.length() - 1);
        increase = Float.valueOf(inStr);

        low = (float) data.optDouble(5);// 最低价
        high = (float) data.optDouble(6);// 最高价
        volume = data.optInt(7);// 成交量(手)
        money = (float) data.optDouble(8);// 成交额(万元)

        // 换手率(%)
        String exStr = data.optString(9);
        exStr = exStr.substring(0, exStr.length() - 1);
        turnover = Float.valueOf(exStr);

        Log.d("Print", date + ", " + open + ", " + high + ", " + low + ", " + close + ", " + volume + "," + money + ", " + getIncreaseString() + ", " + getTurnoverString());

    }

    public void draw(float x, Canvas canvas, Paint pen) {
        if (0 <= increase) {
            pen.setColor(Color.parseColor("#1ABE5B"));
        } else {
            pen.setColor(Color.parseColor("#EE4952"));
        }

        canvas.drawRect(x, 20, x + 10, 40, pen);
    }

    public float getTurnover() {
        return turnover;
    }

    public String getTurnoverString() {
        return turnover + "%";
    }

    public float getVary() {
        return vary;
    }

    public static final float WIDTH = 10.0f;// K线宽度
    public static final float SPACE = 5.0f;// K线间距

    private float open;// 开盘价
    private float close;// 收盘价
    private float vary;// 涨跌变化 (Sohu)
    private float adjClose;// Adj Close (Yahoo)
    private float lastClose;// 昨日收盘价
    private float high;// 最高价
    private float low;// 最低价
    private float increase;// 当日涨幅
    private long volume;// 成交量
    private float money;// 成交额
    private float turnover;// 换手率
    private String date;// 日期
}
