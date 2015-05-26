package com.alex.develop.entity;

import android.util.Log;

import com.alex.develop.settings.StockDataAPI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-5-22.
 * 一只股票
 */
public class Stock extends BaseObject {

    public Stock(String id, String name) {
        this.id = id;
        this.name = name;

        if(null == candlesticks) {
            candlesticks = new ArrayList();

            if(null == today) {
                today = new Candlestick();
                candlesticks.add(today);
            }
        }

        if(null == salePrice) {
            salePrice = new float[StockDataAPI.SINA_ENTRUST_LEVEL];
        }

        if(null == saleVolume) {
            saleVolume = new long[StockDataAPI.SINA_ENTRUST_LEVEL];
        }

        if(null == buyPrice) {
            buyPrice = new float[StockDataAPI.SINA_ENTRUST_LEVEL];
        }

        if(null == buyVolume) {
            buyVolume = new long[StockDataAPI.SINA_ENTRUST_LEVEL];
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public float[] getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float[] salePrice) {
        this.salePrice = salePrice;
    }

    public long[] getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(long[] saleVolume) {
        this.saleVolume = saleVolume;
    }

    public float[] getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float[] buyPrice) {
        this.buyPrice = buyPrice;
    }

    public long[] getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(long[] buyVolume) {
        this.buyVolume = buyVolume;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSuspended() {
        return suspend;
    }

    public Candlestick getToday() {
        return today;
    }

    public List<Candlestick> getCandlesticks() {
        return candlesticks;
    }

    public void fromSina(String[] data) {
        today.setOpen(Float.valueOf(data[1]));// 开盘价
        today.setLastClose(Float.valueOf(data[2])); // 昨日收盘价
        today.setClose(Float.valueOf(data[3]));// 当前价格
        today.setHigh(Float.valueOf(data[4]));// 今日最高价
        today.setLow(Float.valueOf(data[5]));// 今日最低价
        today.setVolume(Long.valueOf(data[8]));// 成交量(单位：股)
        today.setMoney(Float.valueOf(data[9]));// 成交额(单位：元)

        for(int i=10,j=11,m=20,n=21,k=0;k<StockDataAPI.SINA_ENTRUST_LEVEL;i+=2,j+=2,m+=2,n+=2,++k) {

            // 委买
            buyVolume[k] = Long.valueOf(data[i]);// 买k+1数量(单位：股)
            buyPrice[k] = Float.valueOf(data[j]);// 买k+1报价(单位：元)

            // 委卖
            saleVolume[k] = Long.valueOf(data[m]);// 卖k+1数量(单位：股)
            salePrice[k] = Float.valueOf(data[n]);// 卖k+1报价（单位：元）
        }

        // 是否停牌
        if("0.00".equals(data[1])) {
            suspend = true;
        }

        today.setDate(data[30]);
        time = data[31];
    }

    private String id;  // 股票代码
    private String name;// 股票名称
    private float[] salePrice;// 委卖价格（单位：元）
    private long[] saleVolume;// 委卖数量(单位：手)
    private float[] buyPrice;// 委买价格（单位：元）
    private long[] buyVolume;// 委买数量(单位：手)
    private String time;// 时间
    private boolean suspend;// 是否停牌

    private Candlestick today;// 今日行情
    private List<Candlestick> candlesticks;// 蜡烛线
}