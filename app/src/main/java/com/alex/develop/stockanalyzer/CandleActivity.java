package com.alex.develop.stockanalyzer;

import android.os.Bundle;
import android.view.View;

import com.alex.develop.entity.*;
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
        candleView.setOnCandlestickSelectedListener(this);
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

    public static final String ARG_STOCK_INDEX = "stockIndex";
    public static final String ARG_FROM_COLLECT = "fromCollect";

    private Stock stock;
    private StockHeader stockHeader;
    private CandleView candleView;
}
