package com.alex.develop.stockanalyzer;

import android.app.Application;

import com.alex.develop.entity.Stock;

import java.util.List;

/**
 * Created by alex on 15-6-11.
 */
public class Analyzer extends Application {

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    private List<Stock> stockList;
}
