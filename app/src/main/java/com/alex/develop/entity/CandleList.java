package com.alex.develop.entity;

import java.util.LinkedList;

/**
 * Created by alex on 15-7-21.
 * 用于存储K线数据的数据结构
 */
public class CandleList {

    public CandleList() {}

    public void add(Candlestick candlestick) {

        Node node = data.getFirst();

        if(node.isFull()) {
            node = new Node();
            data.addFirst(node);
        }

        node.add(candlestick);
    }

    private LinkedList<Node> data;
    public static final int NODE_CANDLE_COUNT = 20;
}


/**
 * 存储K线数据的节点，每个节点上只存储{NODE_CANDLE_COUNT}条数据
 */
class Node{

    public Node() {
        cursor = 0;
        data = new Candlestick[CandleList.NODE_CANDLE_COUNT];
    }

    /**
     * 向节点中添加数据
     * @param candlestick 需要存储的K线数据
     * @return 添加成功，返回true;如果节点已经装满数据，则返回false
     */
    public boolean add(Candlestick candlestick) {
        boolean flag = true;

        if(!isFull()) {
            data[cursor] = candlestick;
            ++cursor;
        } else {
            flag = false;
        }

        return flag;
    }

    /**
     * 判断节点是否已经装满了数据
     * @return 数据已满返回true;未满返回false
     */
    public boolean isFull() {
        return data.length == cursor;
    }

    public String getEnd() {
        return data[cursor-1].getDate();
    }

    public String getStart() {
        return data[0].getDate();
    }

    /**
     * 节点存储数据的起始日期
     */
    private String start;

    /**
     * 节点存储数据的结束日期
     */
    private String end;

    /**
     * 插入数据的位置，该位置没有数据，其上一个位置肯定存有数据
     */
    private int cursor;

    /**
     * 存储每一天的K线数据
     */
    private Candlestick[] data;
}
