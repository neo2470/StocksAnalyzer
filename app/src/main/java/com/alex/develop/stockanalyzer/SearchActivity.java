package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alex.develop.entity.Stock;

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

        Analyzer analyzer = (Analyzer) getApplication();

        SearchAdapter adapter = new SearchAdapter(Analyzer.getStockList());

        AutoCompleteTextView stockSearch = (AutoCompleteTextView) findViewById(R.id.stockSearch);
        stockSearch.setThreshold(1);
        stockSearch.setAdapter(adapter);
        setResult(Activity.RESULT_OK);

        // 自定义键盘
        symbols = new Keyboard(this, R.xml.symbols);
        qwerty = new Keyboard(this, R.xml.qwerty);
        mKeyboard = (KeyboardView) findViewById(R.id.keyboardView);
        mKeyboard.setKeyboard(symbols);
        mKeyboard.setOnKeyboardActionListener(onKeyboardActionListener);
    }

    private void showKeyboard() {
        mKeyboard.setVisibility(View.VISIBLE);
        mKeyboard.setEnabled(true);
    }

    private void hideKeyboard() {
        mKeyboard.setVisibility(View.GONE);
        mKeyboard.setEnabled(false);
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

    private OnKeyboardActionListener onKeyboardActionListener = new OnKeyboardActionListener() {

        @Override
        public void onPress(int primaryCode) {

        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            // Get the EditText and its Editable
            View focus = getWindow().getCurrentFocus();
            if(null == focus || EditText.class == focus.getClass()) {
                return;
            }

            EditText editText = (EditText) focus;
            Editable editable = editText.getText();
            int start = editText.getSelectionStart();

            boolean insert = true;
            String data = null;

            if(KEY_600 == primaryCode) {
                data = getString(R.string.key_600);
            } else if(KEY_000 == primaryCode) {
                data = getString(R.string.key_000);
            } else if(KEY_300 == primaryCode) {
                data = getString(R.string.key_300);
            } else if(KEY_002 == primaryCode) {
                data = getString(R.string.key_002);
            } else if(KEY_DEL == primaryCode) {
                if(null != editable && 0 < start) {
                    editable.delete(start-1, start);
                }
                insert = false;
            } else if(KEY_ABC == primaryCode) {
                insert = false;
                mKeyboard.setKeyboard(qwerty);
            } else if(KEY_OK == primaryCode) {
                insert = false;

            } else if(KEY_HID == primaryCode) {
                insert = false;
                hideKeyboard();
            } else if(KEY_XXX == primaryCode) {
                insert = false;

            } else if(KEY_123 == primaryCode) {
                insert = false;
                mKeyboard.setKeyboard(symbols);
            } else {
                data = Character.toString((char) primaryCode);
            }

            if(null != editable && insert) {
                editable.insert(start, data);
            }
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void swipeLeft() {

        }

        @Override
        public void swipeRight() {

        }

        @Override
        public void swipeDown() {

        }

        @Override
        public void swipeUp() {

        }

        private final static int KEY_600 = 9901;
        private final static int KEY_000 = 9902;
        private final static int KEY_300 = 9903;
        private final static int KEY_002 = 9904;
        private final static int KEY_DEL = 9905;
        private final static int KEY_ABC = 9906;
        private final static int KEY_OK  = 9907;
        private final static int KEY_HID = 9908;
        private final static int KEY_XXX = 9909;
        private final static int KEY_123 = 9910;
    };

    private KeyboardView mKeyboard;
    private Keyboard symbols;
    private Keyboard qwerty;

}
