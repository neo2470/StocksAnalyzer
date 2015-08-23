package com.alex.develop.entity;

/**
 * Created by alex on 15-7-31.
 * 用于精确定位某个K线数据的游标
 */
public class Cursor {

    public Cursor() {}

    public Cursor copy() {
        Cursor result = new Cursor();
        result.node = node;
        result.candle = candle;
        return  result;
    }

    /**
     * CandleList中用于定位Node的索引
     */
    public int node;

    /**
     * Node中用于定位Candlestick的索引
     */
    public int candle;
}
