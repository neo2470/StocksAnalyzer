package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alex.develop.entity.Stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Created by alex on 15-6-15.
 * Search Stocks by AutoCompleteTextView
 */
public class SearchActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.search_action_bar_titile);
        }

        Analyzer analyzer = (Analyzer) getApplication();

        SearchAdapter adapter = new SearchAdapter(Analyzer.getStockList());

        AutoCompleteTextView stockSearch = (AutoCompleteTextView) findViewById(R.id.stockSearch);
        stockSearch.setThreshold(1);
        stockSearch.setAdapter(adapter);
        setResult(Activity.RESULT_OK);

        // 自定义键盘
        Keyboard keyboard = new Keyboard(this, R.xml.qwerty);
        KeyboardView keyboardView = (KeyboardView) findViewById(R.id.keyboardView);
        keyboardView.setKeyboard(keyboard);
        keyboardView.setPreviewEnabled(false);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private class SearchAdapter extends BaseAdapter implements Filterable {

        public SearchAdapter(List<Stock> stocks) {
            this.stocks = stocks;
        }

        @Override
        public Filter getFilter() {

            if (null == filter) {
                filter = new StockFilter();
            }

            return filter;
        }

        @Override
        public int getCount() {
            return stocks.size();
        }

        @Override
        public Object getItem(int position) {
            return stocks.get(position).getName();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            final Stock stock = stocks.get(position);
            ViewHolder holder;

            if (null == convertView) {
                convertView = LayoutInflater.from(SearchActivity.this).inflate(R.layout.stock_search_item, null);

                holder = new ViewHolder();
                holder.stockCollectBtn = (ToggleButton) convertView.findViewById(R.id.stockCollectBtn);
                holder.stockCode = (TextView) convertView.findViewById(R.id.stockCode);
                holder.stockName = (TextView) convertView.findViewById(R.id.stockName);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }


            holder.stockCollectBtn.setOnCheckedChangeListener(null);
            if (stock.isCollected()) {
                holder.stockCollectBtn.setChecked(true);
            } else {
                holder.stockCollectBtn.setChecked(false);
            }

            holder.stockCollectBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    stock.collect(isChecked ? 1 : 0);
                }
            });

            holder.stockCode.setText(stock.getCode());
            holder.stockName.setText(stock.getName());
            return convertView;
        }

        private class StockFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence prefix) {
                FilterResults results = new FilterResults();

                if (null == originalStocks) {
                    synchronized (mLock) {
                        originalStocks = new ArrayList<>(stocks);
                    }
                }

                if (null == prefix || 0 == prefix.length()) {
                    synchronized (mLock) {
                        ArrayList<Stock> list = new ArrayList<>(originalStocks);
                        results.values = list;
                        results.count = list.size();
                    }
                } else {
                    String prefixStr = prefix.toString().toLowerCase();
                    final List<Stock> values = originalStocks;
                    final List<Stock> newValues = new ArrayList<>();
                    for (Stock stock : values) {
                        String code = stock.getCode();
                        if (code.startsWith(prefixStr) || code.contains(prefixStr)) {
                            newValues.add(stock);
                        }
                    }

                    results.values = newValues;
                    results.count = newValues.size();
                }

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                stocks = (List<Stock>) results.values;

                if (0 < results.count) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }
            }
        }


        private List<Stock> stocks;
        private List<Stock> originalStocks;
        private final Object mLock = new Object();
        private StockFilter filter;
    }

    private static class ViewHolder {
        ToggleButton stockCollectBtn;
        TextView stockCode;
        TextView stockName;
    }
}
