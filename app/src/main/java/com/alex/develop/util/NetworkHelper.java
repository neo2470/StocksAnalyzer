package com.alex.develop.util;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.entity.Stock;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-5-24.
 * 读取网络数据
 */
public class NetworkHelper {

    /**
     * 读取一张网页的内容
     * @param webUrl 网页对应的URL
     * @return 网页内容字符串
     */
    public static String getWebContent(String webUrl) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));

            String line;
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
     * @param stocks 股票列表
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
                stockList[i] = s.getCode();
                ++i;
            }

            String queryUrl = StockDataAPIHelper.getTodayUrl(stockList);
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPIHelper.SINA_CHARSET));

            String line;
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
     * @param stock 股票
     * @param startDate 历史数据的开始时间(将读取从startDate到上个交易日的所有历史数据，如果startDate为空，则读取自股票上市到上个交易日的所有数据)
     */
    public static float[] queryHistory(Stock stock, String startDate) {

        float[] floats = new float[2];
        floats[0] = Float.MIN_VALUE;
        floats[1] = Float.MAX_VALUE;
        HttpURLConnection urlConnection = null;
        try {

            String queryUrl = StockDataAPIHelper.getHistoryUrl(stock.getCode());
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPIHelper.YAHOO_CHARSET));

            String line;
            boolean firstLine = true;// 第一行是列名称，可以忽略
            while (null != (line=bufferedReader.readLine())) {
                if(firstLine) {
                    firstLine = false;
                    continue;
                }

                String[] data = line.split(StockDataAPIHelper.YAHOO_PARSE_SPLIT);
                StockDataAPIHelper.yahooParser(stock, data);

                float high = Float.valueOf(data[2]);
                float low = Float.valueOf(data[3]);
                floats[0] = floats[0] < high ? high : floats[0];
                floats[1] = floats[1] > low ? low : floats[1];

                // 历史行情的起始日期
                if(data[0].startsWith(startDate)) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }

        return floats;
    }

    private NetworkHelper(){}
}
