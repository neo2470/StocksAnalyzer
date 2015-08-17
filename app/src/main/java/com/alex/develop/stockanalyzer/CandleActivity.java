package com.alex.develop.stockanalyzer;

import android.content.Context;
import android.content.Intent;
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

    public static void start(Context context, int index, int from) {
        Intent intent = new Intent();
        intent.setClass(context, CandleActivity.class);
        intent.putExtra(CandleActivity.INDEX, index);
        intent.putExtra(CandleActivity.FROM, from);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.candle_activity);
        Analyzer.setLoadView(findViewById(R.id.loading));

        Bundle bundle = getIntent().getExtras();
        int index = bundle.getInt(INDEX);
        int from = bundle.getInt(FROM);

        if(COLLECT_LIST == from) {
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

    public static final String INDEX = "stockIndex";
    public static final String FROM = "from";
    public static final int STOCK_LIST = 0;
    public static final int COLLECT_LIST = 1;
    public static final int OTHER_LIST = 2;

    private Stock stock;
    private StockHeader stockHeader;
    private CandleView candleView;
}
