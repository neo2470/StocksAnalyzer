package com.alex.develop.stockanalyzer;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.alex.develop.entity.Stock;
import com.alex.develop.util.SQLiteHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by alex on 15-6-11.
 * 存储全局数据
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

    public static List<Stock> getStockList() {
        return stockList;
    }

    public static void setStockList(List<Stock> stockList) {
        Analyzer.stockList = stockList;
    }

    /**
     * 获取被收藏的股票列表(自选股)
     * @return 自选股列表
     */
    public static List<Stock> getCollectStockList() {

        List<Stock> stockCollected = new ArrayList<>();

        for(Stock stock : stockList) {
            if(stock.isCollected()) {
                stockCollected.add(stock);
            }
        }

        // 按照收藏的先后顺序排列
        Collections.sort(stockCollected, new Comparator<Stock>() {
            @Override
            public int compare(Stock lhs, Stock rhs) {

                int result;
                long flag = lhs.getCollectStamp() - rhs.getCollectStamp();

                if(0 > flag) {
                    result = -1;
                } else if(0 < flag) {
                    result = 1;
                } else {
                    result = 0;
                }

                return -result;
            }
        });

        for(Stock stock : stockCollected) {
            Log.d("Print", stock.getName() + ", " + stock.getCollectStamp());
        }

        return stockCollected;
    }

    /**
     * 获取被搜索的股票列表
     * @return
     */
    public static List<Stock> getSearchStockList() {

        List<Stock> stockSearched = new ArrayList<>();

        for(Stock stock : stockList) {
            if(0 < stock.getSearch()) {
                stockSearched.add(stock);
            }
        }

        return stockSearched;
    }

    private static Context context;
    private static List<Stock> stockList;
}
