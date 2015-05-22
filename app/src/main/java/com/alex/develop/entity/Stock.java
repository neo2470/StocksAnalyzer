package com.alex.develop.entity;

import java.util.List;

/**
 * Created by alex on 15-5-22.
 * 一只股票
 */
public class Stock extends BaseObject {

    public Stock(String id, String name, float increase) {
        this.id = id;
        this.name = name;
        this.increase = increase;
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

    public float getIncrease() {
        return increase;
    }

    public void setIncrease(float increase) {
        this.increase = increase;
    }

    private String id;// 股票代码
    private String name;// 股票名称
    private float increase;// 当日涨幅
    private List<Candlestick> candlesticks;// 蜡烛线
}
