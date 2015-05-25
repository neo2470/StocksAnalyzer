package com.alex.develop.settings;

/**
 * Created by alex on 15-5-24.
 * 股票数据查询接口处理
 */
public class StockDataAPI {

    /**
     * 生成查询当日股票行情的新浪API字符串
     * @param stockID
     * @return
     */
    public static String queryToday(String stockID) {
        StringBuilder builder = new StringBuilder();
        builder.append(QUERY_TODAY);

        String prefix = SINA_SZ_PREFIX;
        if(shOrsz(stockID)) {
            prefix = SINA_SH_PREFIX;
        }

        builder.append(prefix);
        builder.append(stockID);

        return builder.toString();
    }

    public static String queryToday2(String stockString) {

        StringBuilder builder = new StringBuilder();
        builder.append(QUERY_TODAY);
        builder.append(stockString);

        return builder.toString();
    }

    /**
     * 生成查询股票历史数据的雅虎API字符串
     * @param stockID
     * @return
     */
    public static String queryHistory(String stockID) {

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

    public final static String SINA_SH_PREFIX = "sh";
    public final static String SINA_SZ_PREFIX = "sz";
    public final static String SINA_CHARSET = "gb2312";

    public final static String YAHOO_SH_SUFFIX = ".ss";
    public final static String YAHOO_SZ_SUFFIX = ".sz";
    public final static String YAHOO_CHARSET = "utf-8";

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
