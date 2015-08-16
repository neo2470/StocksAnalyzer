package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
        stockSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKeyboard.show();
                return true;
            }
        });
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
                holder.stockCode1 = (TextView) convertView.findViewById(R.id.stockCode1);
                holder.stockCode2 = (TextView) convertView.findViewById(R.id.stockCode2);
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

            if(InputType.Numeric == mKeyboard.getInputType()) {
                holder.stockCode1.setText(stock.getCode());
                holder.stockCode2.setText(stock.getCodeCN());
            } else {
                holder.stockCode1.setText(stock.getCodeCN());
                holder.stockCode2.setText(stock.getCode());
            }

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
                    String prefixStr = prefix.toString().trim().toUpperCase();
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

            /**
             * 判断某只股票是否符合查询条件
             *
             * 支持的查询方式：
             * 1、[X]：包含{X}
             * 2、[X-]：以{X}开头
             * 3、[X-Y]：以{X}开头，以{Y}结尾
             * 4、[X-Y-]：以{X}开头，中间包含{Y}，但不以{Y}结尾
             * 5、[X-Y-Z]：以{X}开头，中间包含{Y}，以{Z}结尾
             * 6、[-X]：以{X}结尾
             * 7、[-X-]：中间包含{X}，但不以{X}开头，也不以{X}结尾
             * 8、[-X-Y]：中间包含{X}，以{Y}结尾，但不以{X}开头
             *
             * @param stock 股票
             * @param prefix 查询字符串
             * @return 符合条件返回true，否则返回false
             */
            private boolean access(Stock stock, String prefix) {
                String code;

                InputType inputType = mKeyboard.getInputType();
                if(InputType.Alphabet == inputType) {
                    code = stock.getCodeCN();
                } else {
                    code = stock.getCode();
                }

                String[] split = prefix.split(KEY_SPLIT);

                if(1 == split.length) {
                    if(prefix.endsWith(KEY_SPLIT)) {// [X-]
                        return code.startsWith(split[0]);
                    } else {// [X]
                        return code.contains(prefix);
                    }
                }

                if(2 == split.length) {
                    if(prefix.startsWith(KEY_SPLIT)) {
                        if(prefix.endsWith(KEY_SPLIT)) {// [-X-]
                            return !code.startsWith(split[1]) && code.contains(split[1]) && !code.endsWith(split[1]);
                        } else {// [-X]
                            return code.endsWith(split[1]);
                        }

                    } else {
                        if(prefix.endsWith(KEY_SPLIT)) {// [X-X-]
                            int start = split[0].length();
                            String subCode = code.substring(start);
                            return code.startsWith(split[0]) && subCode.contains(split[1]) && !code.endsWith(split[1]);
                        } else {// [X-X]
                            return code.startsWith(split[0]) && code.endsWith(split[1]);
                        }
                    }
                }

                if(3 == split.length) {
                    if(prefix.startsWith(KEY_SPLIT)) {// [-X-X]
                        int end = code.length() - split[2].length();
                        String subCode = code.substring(0, end);
                        return !code.startsWith(split[1]) && subCode.contains(split[1]) && code.endsWith(split[2]);
                    } else {// [X-X-X]
                        int start = split[0].length();
                        int end = code.length() - split[2].length();
                        String subCode = code.substring(start, end);
                        return code.startsWith(split[0]) && subCode.contains(split[1]) && code.endsWith(split[2]);
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
        TextView stockCode1;
        TextView stockCode2;
        TextView stockName;
    }

    private StockKeyboard mKeyboard;
}
