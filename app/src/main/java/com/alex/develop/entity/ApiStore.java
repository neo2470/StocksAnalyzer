package com.alex.develop.entity;

import com.alex.develop.util.NetworkHelper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by alex on 15-9-7.
 * 汇聚各种使用到的API
 */
public class ApiStore {

    public static final String SH_EXCHANGE = "XSHG";// 上海证券交易所
    public static final String SZ_EXCHANGE = "XSHE";// 深圳证券交易所

    public static final char SBL_DOT = '.';
    public static final char SBL_EQL = '=';
    public static final char SBL_AND = '&';
    public static final char SBL_CMA = ',';

    public static String getStockInfoUrl(Stock... stocks) {

        StringBuilder secIdBuilder = new StringBuilder();
        secIdBuilder.append(StockInfo.KEY_SECID);
        secIdBuilder.append(SBL_EQL);

        StringBuilder tickerBuilder = new StringBuilder();
        tickerBuilder.append(StockInfo.KEY_TICKER);
        tickerBuilder.append(SBL_EQL);

        for(Stock stock : stocks) {

            secIdBuilder.append(stock.getCode());
            secIdBuilder.append(SBL_DOT);
            tickerBuilder.append(stock.getCode());

            if(shOrsz(stock)) {
                secIdBuilder.append(SH_EXCHANGE);
            } else {
                secIdBuilder.append(SZ_EXCHANGE);
            }

            secIdBuilder.append(SBL_CMA);
            tickerBuilder.append(SBL_CMA);
        }

        secIdBuilder.deleteCharAt(secIdBuilder.length() - 1);
        tickerBuilder.deleteCharAt(tickerBuilder.length() - 1);

        // equTypeCD 可以省略
//        tickerBuilder.append(SBL_AND);
//        tickerBuilder.append(StockInfo.KEY_EQUTYPECD);
//        tickerBuilder.append(SBL_EQL);
//        tickerBuilder.append(StockInfo.VALUE_EQUTYPECD);

        // field
        tickerBuilder.append(SBL_AND);
        tickerBuilder.append(StockInfo.KEY_FIELD);
        tickerBuilder.append(SBL_EQL);
        tickerBuilder.append(StockInfo.JSON_LIST_DATE);

        // ...
        tickerBuilder.append(SBL_CMA);
        tickerBuilder.append(StockInfo.JSON_TICKER);

        return StockInfo.API_URL + secIdBuilder + SBL_AND + tickerBuilder;
    }

    /**
     * 判断股票代码是上海A股还是深圳A股
     * @param stock 股票
     * @return 上海A股，true; 深圳A股，false
     */
    private static boolean shOrsz(Stock stock) {
        boolean flag = false;
        if(stock.getCode().startsWith("600")) {
            flag = true;
        }
        return flag;
    }

    /**
     * 使用[京东万象数据]提供的API查询股票基本信息
     * 网址：http://apistore.baidu.com/apiworks/servicedetail/1033.html
     * EG-SH：http://apis.baidu.com/wxlink/getequ/getequ?secID=601919.XSHG&ticker=601919&field=primeOperating
     * EG-SZ：http://apis.baidu.com/wxlink/getequ/getequ?secID=000751.XSHE&ticker=000751&field=primeOperating
     */
    class StockInfo {

        private static final String KEY_API_KEY = "apikey";
        private static final String VALUE_API_KEY = "7099530a107f136565aa4e1dafc3f74f";

        private static final String KEY_SECID = "secID";
        private static final String KEY_TICKER = "ticker";
//        private static final String KEY_EQUTYPECD = "equTypeCD";
//        private static final String VALUE_EQUTYPECD = "A";
        private static final String KEY_FIELD = "field";

        private static final String JSON_LIST_DATE = "listDate";
        private static final String JSON_TICKER = KEY_TICKER;

        private static final String CHARTSET = "UTF-8";

        private static final String API_URL = "http://apis.baidu.com/wxlink/getequ/getequ?";
    }


    /**
     * @param urlAll
     *            :请求接口
     * @param httpArg
     *            :参数
     * @return 返回结果
     */
    public static String request(Stock... stocks) {

        String httpUrl = getStockInfoUrl(stocks);
        HashMap<String, String> header = new HashMap<>();
        header.put(StockInfo.KEY_API_KEY, StockInfo.VALUE_API_KEY);

        return NetworkHelper.getWebContent(httpUrl, header, StockInfo.CHARTSET);
    }
}
