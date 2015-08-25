package com.alex.develop.entity;

import android.util.Log;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 存储K线数据的
 */
public class CandleList {

    public CandleList() {

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
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    /**
     * 设置数据的可见区域，start中的索引数值肯定<=stop中的索引数值
     *
     * @param st 可见区域起始游标
     * @param ed  可见区域结束游标
     */
    public void setScope(Cursor st, Cursor ed) {

        // 同一个Node数据块内
        if (st.node == ed.node) {
            Node node = get(st.node);
            float[] data = node.getLowAndHigh(st.candle, ed.candle);
            low = data[0];
            high = data[1];
            volume = node.getVolume();
            Log.d("Test", low + ", " + high + ", " + volume);

        } else {
            low = Float.MAX_VALUE;
            high = 0.0f;
            for (int i = st.node; i >= ed.node; --i) {
                Node node = get(i);
                float[] data;
                if (i == st.node) {
                    data = node.getLowAndHigh(st.candle, node.size() - 1);
                } else if (i == ed.node) {
                    data = node.getLowAndHigh(0, ed.candle);
                } else {
                    data = node.getLowAndHigh(0, node.size() - 1);
                }

                low = low > data[0] ? data[0] : low;
                high = high < data[1] ? data[1] : high;
                volume = volume < node.getVolume() ? node.getVolume() : volume;
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

    private float low;
    private float high;
    private long volume;

    private ArrayList<Node> nodes;
}
