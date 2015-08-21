package com.alex.develop.entity;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 存储K线数据的
 */
public class CandleList {

    public CandleList() {

        st = new Cursor();
        ed = new Cursor();

        low = 1000000.0f;
        high = 0.0f;
        volume = 0;

        nodes = new ArrayList<>();
    }

    public int size() {
        return nodes.size();
    }

    public void add(Node node) {

        // 记录最低价格和最高价格
        low = low > node.getLow() ? node.getLow() : low;
        high = high < node.getHigh() ? node.getHigh() : high;
        volume = volume < node.getVolume() ? node.getVolume() : volume;

        nodes.add(node);

        // 设置结束游标的初始位置
        if (1 == nodes.size()) {
            ed.node = 0;
            ed.candle = node.size() - 1;

            st.node = ed.node;
            st.candle = ed.candle;
        }
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    /**
     * 将游标{st}和{ed}同时向左或向右移动{day}个数据单位
     *
     * @param day
     */
    public void move(int day) {

    }

    /**
     * 将游标{cursor}移动{day}个单位
     *
     * @param cursor 将要被移动的游标
     * @param day    day > 0，向右移动；day < 0，向左移动
     */
    public void move(Cursor cursor, int day) {

        // TODO 已经测试，该方法存在问题，待修改

//        Log.d("Print-Before", cursor.node + ", " + cursor.candle);
        // 不需要移动
        if(0 == day) {
            return;
        }

        if(day > 0) {// 向右移动(股票数据越来越新)

            int nIndex = cursor.node;
            int cIndex = cursor.candle + day;

            // 同一个Node内
            if(nodes.get(nIndex).size() > cIndex) {
                cursor.candle = cIndex;
            } else {

                while (true) {

                    --nIndex;

                    // 如果超出左侧界限，则将{cursor}设置为最左侧的元素
                    if(0 > nIndex) {
                        cursor.node = 0;
                        cursor.candle = nodes.get(0).size() - 1;
                        break;
                    }

                    Node node = nodes.get(nIndex);
                    cIndex -= node.size();

                    if (node.size() > cIndex) {
                        cursor.node = nIndex;
                        cursor.candle = cIndex;
                        break;
                    }
                }
            }

        } else {// 向左移动(股票数据越来越旧)

            int nIndex = cursor.node;
            int cIndex = cursor.candle - day;

            // 同一个Node内
            if(0 < cIndex) {
                cursor.candle = cIndex;
            } else {
                while (true) {
                    ++nIndex;

                    // 如果超出右侧界限，则将{cursor}设置为最右侧的元素
                    if(nodes.size() <= nIndex) {
                        cursor.node = nodes.size() - 1;
                        cursor.candle = 0;
                        break;
                    }

                    Node node = nodes.get(nIndex);
                    cIndex += node.size();

                    if(0 > cIndex) {
                        cursor.node = nIndex;
                        cursor.candle = cIndex;
                        break;
                    }
                }
            }

        }

//        Log.d("Print-After", cursor.node + ", " + cursor.candle);

    }

    /**
     * 设置数据的可见区域，start中的索引数值肯定<=stop中的索引数值
     *
     * @param start 可见区域起始游标
     * @param stop  可见区域结束游标
     */
    public void setScope(Cursor start, Cursor stop) {

        // 同一个Node数据块内
        if (start.node == stop.node) {
            Node node = get(start.node);
            float[] data = node.getLowAndHigh(start.candle, stop.candle);
            low = data[0];
            high = data[1];
        } else {
            low = 1000000.0f;
            high = 0.0f;
            for (int i = start.node; i <= stop.node; ++i) {
                Node node = get(i);
                float[] data;
                if (i == start.node) {
                    data = node.getLowAndHigh(start.candle, node.size() - 1);
                } else if (i == stop.node) {
                    data = node.getLowAndHigh(0, stop.candle);
                } else {
                    data = node.getLowAndHigh(0, node.size() - 1);
                }

                low = low > data[0] ? data[0] : low;
                high = high < data[1] ? data[1] : high;
            }
        }
    }

    /**
     * 记录可见区域数据中股票价格的最低价格
     *
     * @return 在setScope()之前返回整个数据集合中的最低价格
     */
    public float getLow() {
        return low;
    }

    /**
     * 记录可见区域数据中股票价格的最高价格
     *
     * @return 在setScope()之前返回整个数据集合中的最高价格
     */
    public float getHigh() {
        return high;
    }

    public long getVolume() {
        return volume;
    }

    public Cursor getStart() {
        return st;
    }

    public Cursor getEnd() {
        return ed;
    }

    private Cursor st;
    private Cursor ed;

    private float low;
    private float high;
    private long volume;

    private ArrayList<Node> nodes;
}
