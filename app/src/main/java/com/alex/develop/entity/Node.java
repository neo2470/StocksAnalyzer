package com.alex.develop.entity;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 存储一次查询获取的K线数据集合
 */
public class Node {

    public Node() {
        low = 1000000.0f;
        high = 0.0f;
        candlesticks = new ArrayList<>();
    }

    public ArrayList<Candlestick> getCandlesticks() {
        return candlesticks;
    }

    /**
     * 在添加完数据之后调用，目的是节省内存空间
     */
    public void trim2Size() {
        candlesticks.trimToSize();
    }

    public int size() {
        return candlesticks.size();
    }

    public void add(Candlestick candle) {
        low = low > candle.getLow() ? candle.getLow() : low;
        high = high < candle.getHigh() ? candle.getHigh() : high;
        volume = volume < candle.getVolume() ? candle.getVolume() : volume;

        candlesticks.add(candle);
    }

    public Candlestick get(int index) {
        return candlesticks.get(index);
    }

    /**
     * 计算指定区域的最低价格和最高价格
     * @param start 起始位置
     * @param stop 结束位置
     * @return 0，最低价格;1，最高价格
     */
    public float[] getLowAndHigh(int start, int stop) {
        float[] data = new float[2];

        // 指定区域为整个数据块的时候，无须计算
        if(0 == start && size() == stop + 1) {
            data[0] = low;
            data[1] = high;
            return data;
        }

        for (int i = start; i<=stop; ++i) {
            Candlestick candle = get(i);
            data[0] = data[0] > candle.getLow() ? candle.getLow() : data[0];
            data[1] = data[1] < candle.getHigh() ? candle.getHigh() : data[1];
        }

        return data;
    }

    public float getLow() {
        return low;
    }

    public float getHigh() {
        return high;
    }

    public long getVolume() {
        return volume;
    }

    /**
     * 存储本数据集合中的股票的最低价格
     */
    private float low;

    /**
     * 存储本数据集合中的股票的最高价格
     */
    private float high;

    /**
     * 存储本数据集合中的股票成交量的最大值
     */
    private long volume;

    private ArrayList<Candlestick> candlesticks;
}

