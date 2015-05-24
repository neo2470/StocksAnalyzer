package com.alex.develop.settings;

/**
 * Created by alex on 15-5-24.
 */
public class Config {

    /**
     * 新浪的股票行情实时接口
     * EG-SH : http://hq.sinajs.cn/list=sh601919（上海A股）
     * EG-SZ : http://hq.sinajs.cn/list=sz000783（深圳A股）
     *  MORE : http://hq.sinajs.cn/list=sz000783,sz000698,sh601919
     */
    public final static String QUERY_TODAY = "http://hq.sinajs.cn/list=";

    /**
     * 雅虎的股票历史数据接口
     * EG-SS : http://table.finance.yahoo.com/table.csv?s=600000.ss
     * EG-SZ : http://table.finance.yahoo.com/table.csv?s=000001.sz
     */
    public final static String QUERY_HISTORY = "http://table.finance.yahoo.com/table.csv?s=";

}
