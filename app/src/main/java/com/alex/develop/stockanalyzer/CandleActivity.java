package com.alex.develop.stockanalyzer;

import android.os.Bundle;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.ui.CandleView;
import com.alex.develop.ui.StockHeader;
import com.alex.develop.util.DateHelper;

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

        // 请求历史数据
        new QueryStockHistory(stock).execute(DateHelper.offset(-30), DateHelper.today(), Enum.Period.Day.toString());
    }

    @Override
    public void onSelected(Candlestick candlestick) {
        stockHeader.updateHeaderInfo(candlestick);
    }

    public static final String ARG_STOCK_INDEX = "stockIndex";
    public static final String ARG_FROM_COLLECT = "fromCollect";

    private Stock stock;
    private StockHeader stockHeader;
    private CandleView candleView;
}
