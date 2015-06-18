package com.alex.develop.fragment;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.util.StockDataAPIHelper;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.NetworkHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alex on 15-5-22.
 * 自选股列表
 */
public class StockFragment extends BaseFragment implements CompoundButton.OnCheckedChangeListener {

    public interface OnStockSelectedListener {
        void onStockSelected(int index);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            stockSelectedListener = (OnStockSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + "must implement the OnStockSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.stock_fragment, container, false);

        Bundle bundle = getArguments();
        Analyzer analyzer = (Analyzer) act.getApplication();
        if (null != bundle) {
            boolean isCollectView = bundle.getBoolean(ARG_IS_COLLECT_VIEW);
            if (isCollectView) {
                stocks = analyzer.getStockList();
            } else {
                stocks = analyzer.getStockList();
            }
        } else {
            stocks = analyzer.getStockList();
        }

        RadioButton codeRadio = (RadioButton) view.findViewById(R.id.codeRadio);
        codeRadio.setOnCheckedChangeListener(this);
        RadioButton priceRadio = (RadioButton) view.findViewById(R.id.priceRadio);
        priceRadio.setOnCheckedChangeListener(this);
        RadioButton increaseRadio = (RadioButton) view.findViewById(R.id.increaseRadio);
        increaseRadio.setOnCheckedChangeListener(this);

        final ListView stockList = (ListView) view.findViewById(R.id.stockList);
        stockListAdapter = new StockListAdapter();
        stockList.setAdapter(stockListAdapter);

        stockList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                stockSelectedListener.onStockSelected(position);
            }
        });

        stockList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (SCROLL_STATE_IDLE == scrollState) {
                    if (queryStart < queryStop) {
                        new UpdateStockInfo().execute(queryStart, queryStop);
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                queryStart = firstVisibleItem;
                queryStop = firstVisibleItem + visibleItemCount;

                if (flag && 0 < visibleItemCount) {
                    new UpdateStockInfo().execute(queryStart, queryStop);
                    flag = false;
                }
            }

            private boolean flag = true;
        });

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh :
                new UpdateStockInfo().execute(queryStart, queryStop);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.codeRadio :
                break;
            case R.id.priceRadio :
                break;
            case R.id.increaseRadio :
                break;
        }
    }

    private Stock[] queryStockList(int start, int end) {

        List<Stock> temp = new ArrayList<>();
        for(int i = start; i<end; ++i) {
            Stock stock = stocks.get(i);
            long stamp = System.currentTimeMillis();
            if(StockDataAPIHelper.SINA_REFRESH_INTERVAL < stamp - stock.getStamp()) {//5秒内不重复查询
                if(!stock.getTime().startsWith("15")) {// 15:00:00以后不重复查询
                    temp.add(stock);
                }
            }
        }

        return temp.toArray(new Stock[temp.size()]);
    }

    public static final String ARG_IS_COLLECT_VIEW = "collect";

    private int queryStart;
    private int queryStop;
    private List<Stock> stocks;// 自选股列表
    private OnStockSelectedListener stockSelectedListener;
    private StockListAdapter stockListAdapter;
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
            return position;
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

            int textColor = act.getResources().getColor(R.color.stock_rise);
            float increase = stock.getToday().getIncrease();
            String price = stock.getToday().getCloseString();
            String increaseString = stock.getToday().getIncreaseString();

            if(0 > increase) {
                textColor = act.getResources().getColor(R.color.stock_fall);
            }
            if(stock.isSuspended()) {
                textColor = act.getResources().getColor(R.color.stock_suspended);
                price = stock.getToday().getLastCloseString();
                increaseString = act.getString(R.string.trade_suspended);
            }

            // 如果没有查询到股票数据则显示默认的字符
            if ("".equals(stock.getTime())) {
                price = act.getString(R.string.stock_default);
                increaseString = price;
                textColor = act.getResources().getColor(R.color.stock_suspended);
            }

            // 股票名称
            holder.stockName.setText(stock.getName());

            // 股票代码
            holder.stockID.setText(stock.getCode());

            // 股票价格（收盘价）
            holder.stockClose.setText(price);
            holder.stockClose.setTextColor(textColor);

            // 股票涨幅
            holder.stockIncrease.setText(increaseString);
            holder.stockIncrease.setTextColor(textColor);

            return convertView;
        }
    }

    private class UpdateStockInfo extends AsyncTask<Integer, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadView = (ImageView) act.findViewById(R.id.loading);
            Animation anim = AnimationUtils.loadAnimation(act, R.anim.loading_data);
            loadView.setVisibility(View.VISIBLE);
            loadView.startAnimation(anim);
        }

        @Override
        protected Void doInBackground(Integer... params) {
            NetworkHelper.querySinaToday(queryStockList(params[0], params[1]));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            loadView.setVisibility(View.GONE);
            loadView.clearAnimation();
            stockListAdapter.notifyDataSetChanged();
        }

        private ImageView loadView;
    }
}
