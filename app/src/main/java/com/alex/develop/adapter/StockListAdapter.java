package com.alex.develop.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;

import java.util.List;

/**
 * Created by alex on 15-8-19.
 * 呈现所有股票列表信息ListView的Adapter
 */
public class StockListAdapter extends BaseAdapter {

    public StockListAdapter(List<Stock> stocks) {
        this.stocks = stocks;
    }

    @Override
    public int getCount() {
        return null == stocks ? 0 : stocks.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(null == convertView) {
            LayoutInflater inflater = LayoutInflater.from(Analyzer.getContext());
            convertView = inflater.inflate(R.layout.stock_details, null);
            ViewHolder holder = new ViewHolder();
            holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
            holder.stockCode = (TextView) convertView.findViewById(R.id.stockCode);
            holder.stockClose = (TextView) convertView.findViewById(R.id.stockClose);
            holder.stockIncrease = (TextView) convertView.findViewById(R.id.stockIncrease);
            convertView.setTag(holder);
        }

        Stock stock = stocks.get(position);
        ViewHolder holder = (ViewHolder) convertView.getTag();

        int textColor = Analyzer.getContext().getResources().getColor(R.color.stock_rise);
        float increase = stock.getToday().getIncrease();
        String price = stock.getToday().getCloseString();
        String increaseString = stock.getToday().getIncreaseString();

        if(0 > increase) {
            textColor = Analyzer.getContext().getResources().getColor(R.color.stock_fall);
        }
        if(stock.isSuspended()) {
            textColor = Analyzer.getContext().getResources().getColor(R.color.stock_suspended);
            price = stock.getToday().getLastCloseString();
            increaseString = Analyzer.getContext().getString(R.string.trade_suspended);
        }

        // 如果没有查询到股票数据则显示默认的字符
        if ("".equals(stock.getTime())) {
            price = Analyzer.getContext().getString(R.string.stock_default);
            increaseString = price;
            textColor = Analyzer.getContext().getResources().getColor(R.color.stock_suspended);
        }

        // 股票名称
        holder.stockName.setText(stock.getName());

        // 股票代码
        holder.stockCode.setText(stock.getCode());

        // 股票价格（收盘价）
        holder.stockClose.setText(price);
        holder.stockClose.setTextColor(textColor);

        // 股票涨幅
        holder.stockIncrease.setText(increaseString);
        holder.stockIncrease.setTextColor(textColor);

        return convertView;
    }

    private static class ViewHolder {
        TextView stockName;
        TextView stockCode;
        TextView stockClose;
        TextView stockIncrease;
    }

    private List<Stock> stocks;
}

