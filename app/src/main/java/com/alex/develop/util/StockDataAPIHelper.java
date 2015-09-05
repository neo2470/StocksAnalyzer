package com.alex.develop.util;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;

import java.util.List;

/**
 * Created by alex on 15-5-24.
 * 股票数据查询接口处理
 */
public class StockDataAPIHelper {

    /**
     * 生成查询当日股票行情的新浪API字符串
     * @param stockIDs 股票代码列表
     * @return
     */
    public static String getSinaTodayUrl(String... stockIDs) {
        StringBuilder builder = new StringBuilder();

        for(String id : stockIDs) {

            String prefix = SINA_SZ_PREFIX;
            if(shOrsz(id)) {
                prefix = SINA_SH_PREFIX;
            }

            builder.append(",");
            builder.append(prefix);
            builder.append(id);
        }

        builder.delete(0, 1);
        builder.insert(0, SINA_TODAY);

        return builder.toString();
    }

    /**
     * 解析新浪股票行情API
     * @param stock
     * @param data
     */
    public static void sinaParser(Stock stock, String data) {

        if(data.endsWith("=\"\";")) {
            return ;
        }

        String[] temp = data.substring(11).split("=");
        temp[0] = temp[0].substring(2);
        temp[1] = temp[1].substring(1, temp[1].length()-2);
        String id = temp[0];
        String[] info = temp[1].split(SINA_PARSE_SPLIT);

        if (id.equals(stock.getCode())) {
            stock.fromSina(info);
        }
    }

    /**
     * 生成查询股票历史数据的雅虎API字符串
     * @param stockID
     * @return
     */
    public static String getYahooHistoryUrl(String stockID) {

        StringBuilder builder = new StringBuilder();
        builder.append(YAHOO_HISTORY);

        String suffix = YAHOO_SZ_SUFFIX;
        if(shOrsz(stockID)) {
            suffix = YAHOO_SH_SUFFIX;
        }

        builder.append(stockID);
        builder.append(suffix);

        return builder.toString();
    }

    /**
     * 解析雅虎股票历史数据API
     * @param stock
     * @param data
     */
    public static void yahooParser(Stock stock, String[] data) {
//        List<Candlestick> candlesticks = stock.getCandlesticks();
//
//        if(null != candlesticks) {
//            candlesticks.add(new Candlestick(data));
//        }
    }

    /**
     * 判断股票代码是上海A股还是深圳A股
     * @param stockID 股票代码
     * @return 上海A股，true; 深圳A股，false
     */
    public static boolean shOrsz(String stockID) {
        boolean flag = false;
        if(String.valueOf(stockID).startsWith("6")) {
            flag = true;
        }

        return flag;
    }

    public static String getSohuHistoryUrl(String stockCode, String start, String end) {
        return getSohuHistoryUrl(stockCode, start, end, false, Enum.Order.ASC, Enum.Period.Day);
    }

    public static String getSohuHistoryUrl(String stockCode, String start, String end, Enum.Period period) {
        return getSohuHistoryUrl(stockCode, start, end, false, Enum.Order.ASC, period);
    }

    public static String getSohuHistoryUrl(String stockCode, String start, String end, boolean statistics, Enum.Order order, Enum.Period period) {
        StringBuilder builder = new StringBuilder();
        builder.append(SOHU_HISTORY);

        if(Constant.ZS_SZZS_CODE.equals(stockCode)) {// 上证指数
            builder.append("zs_000001");
        } else if(Constant.ZS_SZCZ_CODE.equals(stockCode)) {// 深证成指
            builder.append("zs_399001");
        } else {// 股票代码
            builder.append("cn_");
            builder.append(stockCode);
        }

        // 起始日期
        builder.append("&start=");
        builder.append(start);

        // 结束日期
        builder.append("&end=");
        builder.append(end);

        // 是否统计
        if(statistics) {
            builder.append("&stat=1");
        }

        // 排序方式
        builder.append("&order=");
        builder.append(order);

        // 查询周期
        builder.append("&period=");
        builder.append(period);

        return builder.toString();
    }

    // 新浪API
    public final static int SINA_REFRESH_INTERVAL = 5000;// 5秒刷新间隔
    public final static int SINA_ENTRUST_LEVEL = 5;// 5档委托数据
    public final static String SINA_SH_PREFIX = "sh";// 上海股票前缀
    public final static String SINA_SZ_PREFIX = "sz";// 深圳股票前缀
    public final static String SINA_CHARSET = "gb2312";// 字符编码
    public final static String SINA_PARSE_SPLIT = ",";// 数据分割字符
    public final static String SINA_DATE_DIVIDER = "-";
    public final static String SINA_SUSPEND = "03";// 停牌状态码

    // 雅虎API
    public final static String YAHOO_SH_SUFFIX = ".ss";// 上海股票后缀
    public final static String YAHOO_SZ_SUFFIX = ".sz";// 深圳股票后缀
    public final static String YAHOO_CHARSET = "utf-8";// 字符编码
    public final static String YAHOO_PARSE_SPLIT = ",";// 数据分隔字符
    public final static String YAHOO_SUSPEND_VOLUME = "000";// 停牌时的成交量
    public final static String YAHOO_HISTORY_START = "2014-01-01";//

    // 搜狐API
    public final static String SOHU_CHARSET = "gbk";
    public final static String SOHU_JSON_STATUS = "status";
    public final static String SOHU_JSON_HQ = "hq";
    public final static String SOHU_JSON_CODE = "code";
    public final static int SOHU_JSON_STATUS_OK = 0;
    public final static String SOHU_DATE_DIVIDER = "-";


    /**
     * 新浪的股票行情实时接口
     * EG-SH : http://hq.sinajs.cn/list=sh601919（上海A股）
     * EG-SZ : http://hq.sinajs.cn/list=sz000783（深圳A股）
     *  MORE : http://hq.sinajs.cn/list=sz000783,sz000698,sh601919
     */
    private final static String SINA_TODAY = "http://hq.sinajs.cn/list=";

    /**
     * 雅虎的股票历史数据接口
     * EG-SS : http://table.finance.yahoo.com/table.csv?s=600000.ss
     * EG-SZ : http://table.finance.yahoo.com/table.csv?s=000001.sz
     */
    private final static String YAHOO_HISTORY = "http://table.finance.yahoo.com/table.csv?s=";

    /**
     * 搜狐的股票历史数据接口
     * http://q.stock.sohu.com/hisHq?code=cn_600966&start=20100930&end=20131231&stat=1&order=D&period=w
     * http://q.stock.sohu.com/hisHq?code=zs_399001&start=20100930&end=20131231&stat=1&order=D&period=w
     */
    private final static String SOHU_HISTORY = "http://q.stock.sohu.com/hisHq?code=";

}
