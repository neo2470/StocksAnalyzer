package com.alex.develop.stockanalyzer;

import android.app.Application;
import android.content.Context;

import com.alex.develop.entity.Stock;
import com.alex.develop.util.SQLiteHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-6-11.
 */
public class Analyzer extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }

    public List<Stock> getStockList() {
        return stockList;
    }

    public void setStockList(List<Stock> stockList) {
        this.stockList = stockList;
    }

    /**
     * 获取被收藏的股票列表(自选股)
     * @return
     */
    public List<Stock> getCollectStockList() {

        if(null == stockCollected) {
            stockCollected = new ArrayList<>();
        } else {
            return stockCollected;
        }

        for(Stock stock : stockList) {
            if(stock.isCollected()) {
                stockCollected.add(stock);
            }
        }

        return stockCollected;
    }

    /**
     * 获取被搜索的股票列表
     * @return
     */
    public List<Stock> getSearchStockList() {

        if(null == stockSearched) {
            stockSearched = new ArrayList<>();
        } else {
            return stockSearched;
        }

        for(Stock stock : stockList) {
            if(0 < stock.getSearch()) {
                stockSearched.add(stock);
            }
        }

        return stockSearched;
    }

    /**
     * 获取股票代码列表，用于搜索
     * @return
     */
    public String[] getStockCodeArray() {
        int size = stockList.size();
        String[] array = new String[size];

        int i = 0;
        for(Stock stock : stockList) {
            array[i] = stock.getCode();
            ++i;
        }

        return array;
    }

    private static Context context;

    private List<Stock> stockList;
    private List<Stock> stockCollected;
    private List<Stock> stockSearched;
}
