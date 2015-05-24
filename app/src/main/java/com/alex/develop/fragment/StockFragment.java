package com.alex.develop.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.develop.entity.Stock;
import com.alex.develop.settings.Config;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.NetworkHelper;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

        stockList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void intialize() {
        if(null == stocks) {
            stocks = new ArrayList();
        }

        new ReadStocksFromAsset().execute("sz");
    }


    private List<Stock> stocks;// 自选股列表
    private static  class ViewHolder {
        TextView stockName;
        TextView stockID;
        TextView stockClose;
        TextView stockIncrease;
    }
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
                ViewHolder holder = new ViewHolder();
                holder.stockName = (TextView) convertView.findViewById(R.id.stockName);
                holder.stockID = (TextView) convertView.findViewById(R.id.stockId);
                holder.stockClose = (TextView) convertView.findViewById(R.id.stockClose);
                holder.stockIncrease = (TextView) convertView.findViewById(R.id.stockIncrease);
                convertView.setTag(holder);
            }

            Stock stock = stocks.get(position);
            ViewHolder holder = (ViewHolder) convertView.getTag();

            // 股票名称
            holder.stockName.setText(stock.getName());

            // 股票代码
            holder.stockID.setText(stock.getId());

            // 股票价格（收盘价）
            holder.stockClose.setText(stock.getCloseString());

            // 股票涨幅
            holder.stockIncrease.setText(stock.getIncreaseString());

            return convertView;
        }
    }
    private class ReadStocksFromAsset extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            boolean result = false;
            InputStream inputStream = null;
            try {
                inputStream = act.getAssets().open(params[0]);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                String line = null;
                while(null != (line=bufferedReader.readLine())) {
                    String[] data = line.split(",");
                    stocks.add(new Stock(data[0], data[1]));
                }
                result = true;

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(null != inputStream) {
                        inputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            StringBuilder query = new StringBuilder();
            for(int i=0; i<10; ++i) {
                Stock stock = stocks.get(i);
                query.append(",sz");
                query.append(stock.getId());
            }

            query.delete(0,1);
            String data = NetworkHelper.getContent(Config.QUERY_TODAY + query.toString());
            Log.d("Print", data);

            return result;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
