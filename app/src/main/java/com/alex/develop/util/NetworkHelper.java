package com.alex.develop.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.entity.Remote;
import com.alex.develop.entity.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-5-24.
 * 读取网络数据
 */
public class NetworkHelper {

    public static void init(Context context) {
        NetworkHelper.context = context;
    }

    /**
     *
     * @param webUrl
     * @return
     */
    public static String readWebUrl(String webUrl) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            String line = null;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return builder.toString();
    }

    /**
     * 获取当日行情数据
     * @param stocks
     * @return
     */
    public static void queryToday(Stock... stocks) {

        if(0 == stocks.length) {
            return;
        }

        HttpURLConnection urlConnection = null;
        try {

            // 生成股票代码数组
            String[] stockList = new String[stocks.length];
            int i = 0;
            for(Stock s : stocks) {
                stockList[i] = s.getId();
                ++i;
            }

            String queryUrl = StockDataAPIHelper.getTodayUrl(stockList);
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPIHelper.SINA_CHARSET));

            String line = null;
            int index = 0;
            while (null != (line=bufferedReader.readLine())) {
                StockDataAPIHelper.sinaParser(stocks[index], line);
                ++index;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * 获取历史行情数据
     * @param stock
     * @param startDate 历史数据的开始时间(将读取从startDate到上个交易日的所有历史数据，如果startDate为空，则读取自股票上市到上个交易日的所有数据)
     */
    public static void queryHistory(Stock stock, String startDate) {

        HttpURLConnection urlConnection = null;
        try {

            String queryUrl = StockDataAPIHelper.getHistoryUrl(stock.getId());
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPIHelper.YAHOO_CHARSET));

            String line = null;
            boolean firstLine = true;// 第一行是列名称，可以忽略
            List<String> close = new ArrayList();
            while (null != (line=bufferedReader.readLine())) {
                if(firstLine) {
                    firstLine = false;
                } else {
                    String[] data = line.split(StockDataAPIHelper.YAHOO_PARSE_SPLIT);
                    if(data[0].startsWith(startDate)) {
                        break;
                    }
                    StockDataAPIHelper.yahooParser(stock, data);
                    close.add(data[2]);// 记录收盘价
                }
            }

            // 解析昨日收盘价
            int i = 1;
            int size = close.size();
            for(Candlestick cs : stock.getCandlesticks()) {

                if(i<size) {
                    cs.setLastClose(Float.valueOf(close.get(i)));
                }

                ++i;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
    }

    private NetworkHelper(){};
    private static Context context;
}
