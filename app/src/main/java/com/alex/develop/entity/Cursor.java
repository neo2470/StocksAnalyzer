package com.alex.develop.entity;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 用于精确定位某个K线数据的游标
 */
public class Cursor {

    public Cursor() {}

    public Cursor(CandleList candleList) {
        this.candleList = candleList;
    }

    public void setCandleList(CandleList candleList) {
        this.candleList = candleList;
    }

    public void copy(Cursor csr) {
        csr.node = node;
        csr.candle = candle;
    }

    public boolean isOldest() {
        final int node = candleList.size() - 1;
        final int candle = 0;
        return node == this.node && candle == this.candle;
    }

    public boolean isNewest() {
        final int node = 0;
        final int candle = candleList.get(node).size() - 1;
        return node == this.node && candle == this.candle;
    }

    public int getMoveDays() {
        return moveDays;
    }

    /**
     * 将游标{cursor}移动{day}个单位
     * @param day day > 0，向右移动；day < 0，向左移动
     * @return -1，游标移动至数据的起点；1，游标移动至数据的终点；0，其他情况
     */
    public Move move(int day) {

        ArrayList<Node> nodes = candleList.getNodes();
        Move flag = Move.None;
        moveDays = day;

        // 不需要移动
        if(0 == day) {
            return flag;
        }

        if(day > 0) {// View向右移动(股票数据越来越新)

            if(isNewest()) {
                return flag;
            }

            int nIndex = node;
            int cIndex = candle + day - 1;

            // 同一个Node内
            if(nodes.get(nIndex).size() > cIndex) {
                candle = cIndex;
                flag = Move.Moved;
            } else {

                cIndex -= nodes.get(nIndex).size();

                while (true) {

                    --nIndex;

                    // 如果超出左侧界限，则将{cursor}设置为最左侧（CandleList）的元素
                    if(0 > nIndex) {
                        node = 0;
                        candle = nodes.get(0).size() - 1;
                        flag = Move.Moved;
                        break;
                    }

                    Node data = nodes.get(nIndex);

                    if (data.size() > cIndex) {
                        node = nIndex;
                        candle = cIndex;
                        flag = Move.Moved;
                        break;
                    } else {
                        cIndex -= data.size();
                    }
                }
            }
        } else {// View向左移动(股票数据越来越旧)

            if(isOldest()) {
                return Move.None;
            }

            // 负数取绝对值
            day = -day;

            int nIndex = node;
            int cIndex = candle - day + 1;

            // 同一个Node内
            if(0 <= cIndex) {
                candle = cIndex;
                flag = Move.Moved;
            } else {
                while (true) {
                    ++nIndex;

                    // 如果超出右侧界限，则将{cursor}设置为最右侧（CandleList）的元素
                    if(nodes.size() <= nIndex) {
                        node = nodes.size() - 1;
                        candle = 0;
                        flag = Move.Moved;
                        break;
                    }

                    Node data = nodes.get(nIndex);
                    cIndex += data.size();

                    if(0 < cIndex) {
                        node = nIndex;
                        candle = cIndex;
                        flag = Move.Moved;
                        break;
                    }
                }
            }
        }

        return flag;
    }

    public int node;// CandleList中用于定位Node的索引

    public int candle;// Node中用于定位Candlestick的索引

    public enum Move {
        None,
        Moved
    }

    private int moveDays;// 移动的天数
    private CandleList candleList;
}
