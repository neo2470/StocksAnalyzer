package com.alex.develop.entity;

import java.util.ArrayList;

/**
 * Created by alex on 15-7-31.
 * 存储K线数据的
 */
public class CandleList {

    public CandleList() {
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

        node.trim2Size();
        nodes.add(node);
    }

    public Node get(int index) {
        return nodes.get(index);
    }

    /**
     * 设置数据的可见区域，start中的索引数值肯定<=stop中的索引数值
     * @param start 可见区域起始游标
     * @param stop 可见区域结束游标
     */
    public void setScope(Cursor start, Cursor stop) {

        // 同一个Node数据块内
        if(start.node == stop.node) {
            Node node = get(start.node);
            float[] data = node.getLowAndHigh(start.candle, stop.candle);
            low = data[0];
            high = data[1];
        } else {
            low = high = 0.0f;
            for (int i = start.node; i<=stop.node; ++i) {
                Node node = get(i);
                float[] data;
                if(i == start.node) {
                    data = node.getLowAndHigh(start.candle, node.size()-1);
                } else if(i == stop.node) {
                    data = node.getLowAndHigh(0, stop.candle);
                } else {
                    data = node.getLowAndHigh(0, node.size()-1);
                }

                low = low > data[0] ? data[0] : low;
                high = high < data[1] ? data[1] : high;
            }
        }
    }

    /**
     * 记录可见区域数据中股票价格的最低价格
     * @return 在setScope()之前返回整个数据集合中的最低价格
     */
    public float getLow() {
        return low;
    }

    /**
     * 记录可见区域数据中股票价格的最高价格
     * @return 在setScope()之前返回整个数据集合中的最高价格
     */
    public float getHigh() {
        return high;
    }

    private float low;
    private float high;
    private long volume;

    private ArrayList<Node> nodes;
}
