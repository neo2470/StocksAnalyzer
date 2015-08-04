package com.alex.develop.stockanalyzer;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.ui.CandleView;
import com.alex.develop.ui.StockHeader;

/**
 * Created by alex on 15-6-15.
 * 绘制蜡烛图（K线图）
 */
public class CandleActivity extends BaseActivity implements CandleView.onCandlestickSelectedListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_activity);
        Analyzer.setLoadView(findViewById(R.id.loading));

        Bundle bundle = getIntent().getExtras();
        int index = bundle.getInt(ARG_STOCK_INDEX);
        boolean isCollect = bundle.getBoolean(ARG_FROM_COLLECT);

        if(isCollect) {
            stock = Analyzer.getCollectStockList(false).get(index);
        } else {
            stock = Analyzer.getStockList().get(index);
        }

        stockHeader = (StockHeader) findViewById(R.id.stockHeader);
        stockHeader.setStock(stock.getName(), stock.getCode());
        stockHeader.updateHeaderInfo(stock.getToday());

        candleView = (CandleView) findViewById(R.id.candleView);
        candleView.setStock(stock);
    }

    @Override
    public void onSelected(Candlestick candlestick) {
        stockHeader.updateHeaderInfo(candlestick);
    }

    public void onBottomBtnClicked(View view) {

        switch (view.getId()) {
            case R.id.periodBtn :
                findViewById(R.id.periods).setVisibility(View.VISIBLE);
                findViewById(R.id.indicators).setVisibility(View.GONE);
                break;
            case R.id.indicatorBtn :
                findViewById(R.id.periods).setVisibility(View.GONE);
                findViewById(R.id.indicators).setVisibility(View.VISIBLE);
                break;
        }
    }

    private void foreach() {

        CandleList data = stock.getCandleList();

        for (int i=data.size()-1; i>=0; --i) {
            Node node = data.get(i);
            Log.d("Debug ArrayList # " + i, node.get(0).getDate() + " ~ " + node.get(node.size() - 1).getDate());

            for(Candlestick candle : node.getCandlesticks()) {
                Log.d("Debug Candlestick", candle.getDate() + ", " + candle.getCloseString() + ", " + candle.getIncreaseString());
            }
        }
    }

    public static final String ARG_STOCK_INDEX = "stockIndex";
    public static final String ARG_FROM_COLLECT = "fromCollect";

    private Stock stock;
    private StockHeader stockHeader;
    private CandleView candleView;
}
