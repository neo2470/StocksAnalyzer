package com.alex.develop.entity;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 用于精确定位某个K线数据的游标
 */
public class Cursor {

    public Cursor(CandleList candleList) {
        this.candleList = candleList;
    }

    public Cursor copy() {
        Cursor result = new Cursor(candleList);
        result.node = node;
        result.candle = candle;
        return  result;
    }

    /**
     * 将游标{cursor}移动{day}个单位
     *
     * @param day    day > 0，向右移动；day < 0，向左移动
     */
    public void move(int day) {

        ArrayList<Node> nodes = candleList.getNodes();

        // TODO 已经测试，该方法存在问题，待修改

//        Log.d("Print-Before", cursor.node + ", " + cursor.candle);
        // 不需要移动
        if(0 == day) {
            return;
        }

        if(day > 0) {// 向右移动(股票数据越来越新)

            int nIndex = node;
            int cIndex = candle + day - 1;

            // 同一个Node内
            if(nodes.get(nIndex).size() > cIndex) {
                candle = cIndex;
            } else {

                while (true) {

                    --nIndex;

                    // 如果超出左侧界限，则将{cursor}设置为最左侧的元素
                    if(0 > nIndex) {
                        node = 0;
                        candle = nodes.get(0).size() - 1;
                        break;
                    }

                    Node data = nodes.get(nIndex);
                    cIndex -= data.size();

                    if (data.size() > cIndex) {
                        node = nIndex;
                        candle = cIndex;
                        break;
                    }
                }
            }

        } else {// 向左移动(股票数据越来越旧)

            // 负数取绝对值
            day = -day;

            int nIndex = node;
            int cIndex = candle - day + 1;

            // 同一个Node内
            if(0 <= cIndex) {
                candle = cIndex;
            } else {
                while (true) {
                    ++nIndex;

                    // 如果超出右侧界限，则将{cursor}设置为最右侧的元素
                    if(nodes.size() <= nIndex) {
                        node = nodes.size() - 1;
                        candle = 0;
                        break;
                    }

                    Node data = nodes.get(nIndex);
                    cIndex += data.size();

                    if(0 > cIndex) {
                        node = nIndex;
                        candle = cIndex;
                        break;
                    }
                }
            }

        }

//        Log.d("Print-After", node + ", " + candle);

    }



    /**
     * CandleList中用于定位Node的索引
     */
    public int node;

    /**
     * Node中用于定位Candlestick的索引
     */
    public int candle;

    private CandleList candleList;
}
