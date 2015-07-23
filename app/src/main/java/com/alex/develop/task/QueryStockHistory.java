package com.alex.develop.task;

import android.os.AsyncTask;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.entity.Enum.EnumType;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.DateHelper;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

/**
 * Created by alex on 15-7-18.
 * 查询某支股票历史行情数据
 * @param EnumType 有二个参数，p1为{Enum.Month}类型，指明要查询的是从几月起之前的数据，具体查询多少，需要根据p2做出判断；p2为{Enum.Period}类型，指明需要的数据周期，是日，周，月
 * @param Integer 查询到的行情数据数量
 */
public class QueryStockHistory extends AsyncTask<EnumType, Void, Integer> {

    public QueryStockHistory(Stock stock) {
        this.stock = stock;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        Animation anim = AnimationUtils.loadAnimation(Analyzer.getContext(), R.anim.loading_data);
        Analyzer.getLoadView().setVisibility(View.VISIBLE);
        Analyzer.getLoadView().startAnimation(anim);
    }

    @Override
    protected Integer doInBackground(EnumType... params) {

        int length = params.length;

        // 查询周期,默认是天
        Enum.Period period = Enum.Period.Day;

        String[] date = DateHelper.getDateScope(params);

        if(1 < length) {
            period = (Enum.Period) params[length - 1];
        }

        String url = StockDataAPIHelper.getSohuHistoryUrl(stock.getCode(), date[0], date[1], period);
        String data = NetworkHelper.getWebContent(url, StockDataAPIHelper.SOHU_CHARSET);
        return stock.formSohu(data);
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);

        Analyzer.getLoadView().setVisibility(View.GONE);
        Analyzer.getLoadView().clearAnimation();
    }

    private Stock stock;
}
