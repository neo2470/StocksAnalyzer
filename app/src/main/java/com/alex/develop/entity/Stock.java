package com.alex.develop.entity;

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
            }

            candlesticks.add(0,today);
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Candlestick> getCandlesticks() {
        return candlesticks;
    }

    public String getCloseString() {
        return today.getCloseString();
    }

    public String getIncreaseString() {
        return today.getIncreaseString();
    }

    public Candlestick getToday() {
        return today;
    }

    private String id;// 股票代码
    private String name;// 股票名称
    private Candlestick today;// 当天的股票数据
    private List<Candlestick> candlesticks;// 蜡烛线
}