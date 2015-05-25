package com.alex.develop.util;

import android.content.Context;

import com.alex.develop.entity.Stock;
import com.alex.develop.settings.StockDataAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alex on 15-5-24.
 * 读取网络数据
 */
public class NetworkHelper {

    public static void init(Context context) {
        NetworkHelper.context = context;
    }


    /**
     * 获取当日行情数据
     * @param stock
     * @return
     */
    public static void getToday(Stock stock) {

        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {

            String queryUrl = StockDataAPI.queryToday(stock.getId());
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPI.SINA_CHARSET));

            String line = null;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
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
     */
    public static void getHistory(Stock stock) {

        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {

            String queryUrl = StockDataAPI.queryHistory(stock.getId());
            URL url = new URL(queryUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StockDataAPI.YAHOO_CHARSET));

            String line = null;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
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
