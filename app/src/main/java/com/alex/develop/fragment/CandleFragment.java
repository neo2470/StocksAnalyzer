package com.alex.develop.fragment;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

import java.util.List;

/**
 * Created by alex on 15-5-25.
 * 蜡烛线绘制
 */
public class CandleFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.candle_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle bundle = getArguments();
        if(null != bundle) {
            List<Stock> stocks = ((Analyzer) act.getApplication()).getStockList();
            stock = stocks.get(bundle.getInt(ARG_STOCK_INDEX));
            new AsyncStockHistory().execute(stock.getCode());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        act.setBackTwice2Exit(false);

        // 隐藏不需要的部分
        act.showBottomSwitcher(false);
        ActionBar actionBar = act.getActionBar();
        if(null != actionBar) {
            actionBar.hide();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        act.setBackTwice2Exit(true);
    }

    public static final String ARG_STOCK_INDEX = "stockIndex";

    private Stock stock;
    private class AsyncStockHistory extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loadView = (ImageView) act.findViewById(R.id.loading);
            Animation anim = AnimationUtils.loadAnimation(act, R.anim.loading_data);
            loadView.setVisibility(View.VISIBLE);
            loadView.startAnimation(anim);
        }

        @Override
        protected Void doInBackground(String... params) {
            float[] data = NetworkHelper.queryHistory(stock, StockDataAPIHelper.YAHOO_HISTORY_START);
            Log.d("Print", data[0] + ", " + data[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadView.setVisibility(View.GONE);
            loadView.clearAnimation();
        }

        private ImageView loadView;
    }
}
