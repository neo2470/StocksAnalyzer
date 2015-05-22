package com.alex.develop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-5-22.
 * 自选股列表
 */
public class StockFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stock_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        intialize();

        ListView stockList = (ListView) act.findViewById(R.id.stockList);
        StockListAdapter stockListAdapter = new StockListAdapter();
        stockList.setAdapter(stockListAdapter);
    }

    private void intialize() {
        if(null == stocks) {
            stocks = new ArrayList();
        }

        for(int i=0; i<10; ++i) {
            String id = "60000"+i;
            String name = "股票"+i;
            float increase = 0.0f;
            stocks.add(new Stock(id, name, increase));
        }
    }


    private List<Stock> stocks;// 自选股列表
    private class StockListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return stocks.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(null == convertView) {
                LayoutInflater inflater = LayoutInflater.from(act);
                convertView = inflater.inflate(R.layout.stock_details, null);
            }

            TextView stockName = (TextView) convertView.findViewById(R.id.stockName);
            stockName.setText(stocks.get(position).getName());

            return convertView;
        }
    }
}
