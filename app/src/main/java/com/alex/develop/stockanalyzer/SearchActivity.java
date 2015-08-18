package com.alex.develop.stockanalyzer;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alex.develop.adapter.SearchAdapter;
import com.alex.develop.adapter.SearchAdapter.OnStocksFindListener;
import com.alex.develop.entity.Stock;
import com.alex.develop.ui.StockKeyboard;

/**
 * Created by alex on 15-6-15.
 * Search Stocks by EditText
 */
public class SearchActivity extends BaseActivity implements OnStocksFindListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        ActionBar actionBar = getActionBar();
        if (null != actionBar) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.search_action_bar_titile);
        }

        stockSearch = (EditText) findViewById(R.id.stockSearch);
        stockSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                doAfterTextChanged(s);
            }
        });
        stockSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mKeyboard.show();
                return false;
            }
        });

        final SearchAdapter adapter = new SearchAdapter(Analyzer.getSearchStockList());
        adapter.setOnStocksFindListener(this);

        resultList = (ListView) findViewById(R.id.resultList);
        resultList.setAdapter(adapter);
        resultList.setTextFilterEnabled(true);

        resultList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = (Stock) adapter.getItem(position);
                stock.search();
                CandleActivity.start(SearchActivity.this, stock.getIndex(), CandleActivity.OTHER_LIST);
                Log.d("Select", "Click, " + position);
            }
        });
        setResult(Activity.RESULT_OK);

        // 自定义键盘
        mKeyboard = (StockKeyboard) findViewById(R.id.keyboardView);
        mKeyboard.setOnInputTypeChangeListener(adapter);
        mKeyboard.setKeyboardLayout(R.xml.symbols, R.xml.qwerty, stockSearch);

        findNothing = (TextView) findViewById(R.id.findNothing);
    }

    @Override
    public void find(int findSize) {
        if(0 < findSize) {
            findNothing.setVisibility(View.GONE);
        } else {
            if(!stockSearch.getText().toString().isEmpty()) {
                findNothing.setVisibility(View.VISIBLE);
            }
        }
    }

    private void doAfterTextChanged(CharSequence s) {
        ListAdapter adapter = resultList.getAdapter();
        if(adapter instanceof Filterable) {
            Filter filter = ((Filterable) adapter).getFilter();
            if(null == s || 0 == s.length()) {
                filter.filter(null);
            } else {
                filter.filter(s);
            }
        }
    }

    private ListView resultList;
    private EditText stockSearch;
    private TextView findNothing;
    private StockKeyboard mKeyboard;
}
