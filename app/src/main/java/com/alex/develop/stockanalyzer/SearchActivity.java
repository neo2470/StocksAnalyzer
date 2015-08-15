package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum.InputType;
import com.alex.develop.ui.StockKeyboard;

import java.util.ArrayList;
import java.util.List;

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

        AutoCompleteTextView stockSearch = (AutoCompleteTextView) findViewById(R.id.stockSearch);
        stockSearch.setThreshold(1);
        stockSearch.setAdapter(new SearchAdapter(Analyzer.getStockList()));
        setResult(Activity.RESULT_OK);

        // 自定义键盘
        mKeyboard = (StockKeyboard) findViewById(R.id.keyboardView);
        mKeyboard.setKeyboardLayout(R.xml.symbols, R.xml.qwerty);
    }

    public class SearchAdapter extends BaseAdapter implements Filterable {

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
                    String prefixStr = prefix.toString().toUpperCase();
                    final List<Stock> values = originalStocks;
                    final List<Stock> newValues = new ArrayList<>();

                    for (Stock stock : values) {
                        if (access(stock, prefixStr)) {
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

            private boolean access(Stock stock, String data) {
                String code;

                InputType inputType = mKeyboard.getInputType();
                if(InputType.Alphabet == inputType) {
                    code = stock.getCodeCN();
                } else {
                    code = stock.getCode();
                }

                String[] split = data.split(KEY_SPLIT);
                if(1 == split.length) {
                    return code.startsWith(data) || code.contains(data) || code.endsWith(data);
                }

                if(2 == split.length) {

                    if(split[1].isEmpty()) {
                        return code.startsWith(split[0]) || code.contains(split[0]) || code.endsWith(split[0]);
                    } else {
                        return code.startsWith(split[0]) && code.endsWith(split[1]);
                    }
                }

                if(3 == split.length) {
                    if(split[2].isEmpty()) {
                        return code.startsWith(split[0]) && code.endsWith(split[1]);
                    } else {
                        return code.startsWith(split[0]) && code.contains(split[1]) && code.endsWith(split[2]);
                    }
                }
                return false;
            }
        }

        private List<Stock> stocks;
        private StockFilter filter;
        private List<Stock> originalStocks;
        private final Object mLock = new Object();
        private final String KEY_SPLIT = getString(R.string.key_split);
    }

    private static class ViewHolder {
        ToggleButton stockCollectBtn;
        TextView stockCode;
        TextView stockName;
    }

    private StockKeyboard mKeyboard;
}
