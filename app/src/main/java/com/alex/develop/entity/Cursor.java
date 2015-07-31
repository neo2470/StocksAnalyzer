package com.alex.develop.entity;

/**
 * Created by alex on 15-7-31.
 * 用于精确定位某个K线数据的游标
 */
public class Cursor {

    /**
     * CandleList中用于定位Node的索引
     */
    public int node;

    /**
     * Node中用于定位Candlestick的索引
     */
    public int candle;
}
