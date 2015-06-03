package com.alex.develop.settings;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.entity.Stock;

import java.util.List;

/**
 * Created by alex on 15-5-24.
 * 股票数据查询接口处理
 */
public class StockDataAPI {

    /**
     * 生成查询当日股票行情的新浪API字符串
     * @param stockIDs 股票代码列表
     * @return
     */
    public static String getTodayUrl(String... stockIDs) {
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
        builder.insert(0, QUERY_TODAY);

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

        if (id.equals(stock.getId())) {
            stock.fromSina(info);
        }
    }

    /**
     * 生成查询股票历史数据的雅虎API字符串
     * @param stockID
     * @return
     */
    public static String getHistoryUrl(String stockID) {

        StringBuilder builder = new StringBuilder();
        builder.append(QUERY_HISTORY);

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
        List<Candlestick> candlesticks = stock.getCandlesticks();

        if(null != candlesticks) {
            candlesticks.add(new Candlestick(data));
        }
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

    // 新浪API
    public final static int SINA_REFRESH_INTERVAL = 5000;// 5秒刷新间隔
    public final static int SINA_ENTRUST_LEVEL = 5;// 5档委托数据
    public final static String SINA_SH_PREFIX = "sh";// 上海股票前缀
    public final static String SINA_SZ_PREFIX = "sz";// 深圳股票前缀
    public final static String SINA_CHARSET = "gb2312";// 字符编码
    public final static String SINA_PARSE_SPLIT = ",";// 数据分割字符
    public final static String SINA_SUSPEND_VOLUME = "0";// 停牌时的成交量

    // 雅虎API
    public final static String YAHOO_SH_SUFFIX = ".ss";// 上海股票后缀
    public final static String YAHOO_SZ_SUFFIX = ".sz";// 深圳股票后缀
    public final static String YAHOO_CHARSET = "utf-8";// 字符编码
    public final static String YAHOO_PARSE_SPLIT = ",";// 数据分隔字符
    public final static String YAHOO_SUSPEND_VOLUME = "000";// 停牌时的成交量

    /**
     * 新浪的股票行情实时接口
     * EG-SH : http://hq.sinajs.cn/list=sh601919（上海A股）
     * EG-SZ : http://hq.sinajs.cn/list=sz000783（深圳A股）
     *  MORE : http://hq.sinajs.cn/list=sz000783,sz000698,sh601919
     */
    private final static String QUERY_TODAY = "http://hq.sinajs.cn/list=";

    /**
     * 雅虎的股票历史数据接口
     * EG-SS : http://table.finance.yahoo.com/table.csv?s=600000.ss
     * EG-SZ : http://table.finance.yahoo.com/table.csv?s=000001.sz
     */
    private final static String QUERY_HISTORY = "http://table.finance.yahoo.com/table.csv?s=";

}
