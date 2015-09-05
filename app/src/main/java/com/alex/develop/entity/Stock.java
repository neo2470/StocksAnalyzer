package com.alex.develop.entity;

import android.provider.BaseColumns;
import android.util.Log;

import com.alex.develop.task.CollectStockTask;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.util.DateHelper;
import com.alex.develop.util.StockDataAPIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Date;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by alex on 15-5-22.
 * 一只股票
 */
public final class Stock extends BaseObject {

    public static class Table {

        public static abstract class Column implements BaseColumns {
            public static final String CODE = "code";// 股票代码
            public static final String CODE_CN = "code_cn";// 股票拼音首字母代码
            public static final String NAME = "name";// 股票名称
            public static final String COLLECT = "collect";// 股票是否被收藏
            public static final String COLLECT_STAMP = "collect_stamp";// 股票被收藏的时间戳
            public static final String SEARCH = "search";// 股票被搜索的次数
        }

        public static final String NAME = "stock_list";
        public static final String SQL_CREATE =
                "CREATE TABLE " + NAME + " (" +
                        Column._ID + " INTEGER PRIMARY KEY," +
                        Column.CODE + " char(10)," +
                        Column.CODE_CN + " char(10)," +
                        Column.NAME + " nchar(10)," +
                        Column.COLLECT + " INTEGER," +
                        Column.COLLECT_STAMP + " TEXT," +
                        Column.SEARCH + " INTEGER)";
    }

    public Stock(String code, String name) {
        this.code = code;
        this.name = name;

        if (null == candleList) {
            candleList = new CandleList();

            if (null == today) {
                today = new Candlestick();
                today.setApiFrom(Enum.API.Sina);
            }
        }

        if (null == salePrice) {
            salePrice = new float[StockDataAPIHelper.SINA_ENTRUST_LEVEL];
        }

        if (null == saleVolume) {
            saleVolume = new long[StockDataAPIHelper.SINA_ENTRUST_LEVEL];
        }

        if (null == buyPrice) {
            buyPrice = new float[StockDataAPIHelper.SINA_ENTRUST_LEVEL];
        }

        if (null == buyVolume) {
            buyVolume = new long[StockDataAPIHelper.SINA_ENTRUST_LEVEL];
        }

        st = new Cursor(candleList);
        ed = new Cursor(candleList);
    }

    public String getCodeCN() {
        return codeCN;
    }

    public void setCodeCN(String codeCN) {
        this.codeCN = codeCN;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public float[] getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(float[] salePrice) {
        this.salePrice = salePrice;
    }

    public long[] getSaleVolume() {
        return saleVolume;
    }

    public void setSaleVolume(long[] saleVolume) {
        this.saleVolume = saleVolume;
    }

    public float[] getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(float[] buyPrice) {
        this.buyPrice = buyPrice;
    }

    public long[] getBuyVolume() {
        return buyVolume;
    }

    public void setBuyVolume(long[] buyVolume) {
        this.buyVolume = buyVolume;
    }

    public String getTime() {
        return null == time ? "" : time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isSuspended() {
        return suspend;
    }

    public long getStamp() {
        return stamp;
    }

    public Candlestick getToday() {
        return today;
    }

    public CandleList getCandleList() {
        return candleList;
    }

    public long getCollectStamp() {
        return collectStamp;
    }

    public void setCollectStamp(long collectStamp) {
        this.collectStamp = collectStamp;
    }

    public int getCollect() {
        return collect;
    }

    public boolean isCollected() {
        return 1 == collect;
    }

    public void setCollect(int collect) {
        this.collect = collect;
    }

    public void collect(int collect) {
        this.collect = collect;

        // 更新数据库
        new CollectStockTask(collect).execute(this);
    }

    public int getSearch() {
        return search;
    }

    public void setSearch(int search) {
        this.search = search;
    }

    public void search() {

    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Cursor getStart() {
        return st;
    }

    public Cursor getEnd() {
        return ed;
    }

    /**
     * 将游标{st}和{ed}同时向左或向右移动{day}个数据单位
     *
     * @param day
     */
    public void moveCursor(int day) {
        ed.move(day);
        st.move(day);
        candleList.setScope(st, ed);
    }

    /**
     * 重置数据可视区域的游标
     *
     * @return boolean，如果{candleList}中所含的数据量
     * 少于要显示的数据量{Config.ITEM_AMOUNTS}返回false，
     * 否则返回true
     */
    public boolean resetCursor() {

        // TODO 此方法貌似有些问题
        if (candleList.getCount() < Config.ITEM_AMOUNTS) {
            return false;
        }

        ed.node = 0;
        ed.candle = candleList.getNodes().get(0).size() - 1;

        st.node = ed.node;
        st.candle = ed.candle;

        // 程序第一次下载的数据量 保证至少要{>=Config.ITEM_AMOUNTS}
        st.move(-Config.ITEM_AMOUNTS);
        Log.d("Print-resetCursor", st.node + ", " + st.candle + ", " + ed.node + ", " + ed.candle);

        candleList.setScope(st, ed);

        return true;
    }

    public void requestData() {
        new QueryStockHistory(this).execute(Enum.Month.Jul, Enum.Period.Day);
    }

    public void fromSina(String[] data) {
        today.setOpen(Float.valueOf(data[1]));// 开盘价
        today.setLastClose(Float.valueOf(data[2])); // 昨日收盘价
        today.setClose(Float.valueOf(data[3]));// 当前价格
        today.setHigh(Float.valueOf(data[4]));// 今日最高价
        today.setLow(Float.valueOf(data[5]));// 今日最低价
        today.setVolume(Long.valueOf(data[8]));// 成交量(单位：股)
        today.setAmount(Float.valueOf(data[9]));// 成交额(单位：元)
        today.initialize();

        for (int i = 10, j = 11, m = 20, n = 21, k = 0; k < StockDataAPIHelper.SINA_ENTRUST_LEVEL; i += 2, j += 2, m += 2, n += 2, ++k) {

            // 委买
            buyVolume[k] = Long.valueOf(data[i]);// 买k+1数量(单位：股)
            buyPrice[k] = Float.valueOf(data[j]);// 买k+1报价(单位：元)

            // 委卖
            saleVolume[k] = Long.valueOf(data[m]);// 卖k+1数量(单位：股)
            salePrice[k] = Float.valueOf(data[n]);// 卖k+1报价（单位：元）
        }

        // 根据成交量判断是否停牌
        if (StockDataAPIHelper.SINA_SUSPEND_VOLUME.equals(data[8])) {
            suspend = true;
        }

        today.setDate(data[30].replace(StockDataAPIHelper.SINA_DATE_DIVIDER, ""));
        time = data[31];

        stamp = System.currentTimeMillis();
    }

    /**
     * 根据sohu股票行情接口读取数据
     *
     * @param data sohu提供的json 数据
     * @return 读取到的数据量, <0, 表示没有读到任何数据(无此股票)；>0,为一共读取到的行情数量
     */
    public int formSohu(String data) {

        int flag = -1;
        try {
            JSONArray array = new JSONArray(data);
            JSONObject info = array.getJSONObject(0);

            String status = info.optString(StockDataAPIHelper.SOHU_JSON_STATUS);
            if (!status.equals(StockDataAPIHelper.SOHU_JSON_STATUS_OK)) {
                return flag;
            }

            String code = info.optString(StockDataAPIHelper.SOHU_JSON_CODE);
            if (code.endsWith(this.code)) {
                JSONArray candle = info.optJSONArray(StockDataAPIHelper.SOHU_JSON_HQ);

                int size = candle.length();

                if (DateHelper.isMarketOpen() && 0 == candleList.size()) {
                    ++size;
                }

                Node node = new Node(size);
                for (int i = 0; i < candle.length(); ++i) {
                    Candlestick candlestick = new Candlestick(candle.optJSONArray(i));
                    node.add(candlestick);
                }

                if (0 == candleList.size()) {// K线日期最新的结点

                    /**
                     * 在开盘期间，由于搜狐的历史数据没有当天的数据，此时使用新浪的当日数据；
                     * 收盘后，搜狐的数据才包含当天的数据，此时使用搜狐的数据
                     */

                    if (DateHelper.isMarketOpen()) {

                        String lastCandleDate = node.get(size - 2).getDate();
                        if (!lastCandleDate.equals(today.getDate())) {
                            node.add(today);
                        }

                    } else {
                        today = node.get(size - 1);
                    }
                }

                candleList.add(node);
                flag = size;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return flag;
    }

    private String code;  // 股票代码
    private String codeCN;// 股票拼音首字母代码
    private String name;// 股票名称
    private float[] salePrice;// 委卖价格（单位：元）
    private long[] saleVolume;// 委卖数量(单位：手)
    private float[] buyPrice;// 委买价格（单位：元）
    private long[] buyVolume;// 委买数量(单位：手)
    private String time;// 时间
    private boolean suspend;// 是否停牌
    private long stamp;// 查询时间戳
    private long collectStamp;// 被收藏的时间戳
    private int collect;// 股票是否被收藏
    private int search;// 股票被搜索的次数
    private int index;// 在{Analyzer.stockList}中的索引

    private Cursor st;// 被绘制的K线的起始位置
    private Cursor ed;// 被绘制的K线的结束位置

    private Candlestick today;// 今日行情

    private CandleList candleList;// 蜡烛线（链表）
}