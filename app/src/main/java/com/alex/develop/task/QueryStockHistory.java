package com.alex.develop.task;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum.Period;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.ui.LoadingDialog;
import com.alex.develop.util.DateHelper;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.StockDataAPIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by alex on 15-7-18.
 * 查询某支股票历史行情数据
 * @param Period {Enum.Period}类型，指明需要的数据周期，是日，周，月
 * @param Integer 查询到的行情数据数量
 */
public class QueryStockHistory extends AsyncTask<Period, Void, Integer> {

    public QueryStockHistory(Stock stock) {
        this.stock = stock;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Animation anim = AnimationUtils.loadAnimation(Analyzer.getContext(), R.anim.loading_data);
        Analyzer.getCandleActivityLoadView().setVisibility(View.VISIBLE);
        Analyzer.getCandleActivityLoadView().startAnimation(anim);
    }

    @Override
    protected Integer doInBackground(Period... params) {

        String end = stock.getCandleList().getOldDate();
        String start = end;

        int offset;// 根据不同的周期（月、周、日）选择不同的偏移时间

        switch (params[0]) {
            case Month:
                offset = -Constant.DATE_OFFSET_MONTH;
                break;
            case Week:
                offset = -Constant.DATE_OFFSET_WEEK;
                break;
            default:
                offset = -Constant.DATE_OFFSET_DAY;
        }

        JSONArray dataRaw = null;

        int count = 0;// 统计本次下载到的数据量
        boolean gotData = false;// 是否已经下载到数据

        do {

            start = DateHelper.offset(start, offset);

            String url = StockDataAPIHelper.getSohuHistoryUrl(stock.getCode(), start, end, params[0]);
            String data = NetworkHelper.getWebContent(url, StockDataAPIHelper.SOHU_CHARSET);

            try {

                dataRaw = new JSONArray(data);

                JSONObject obj = dataRaw.optJSONObject(0);

                final int status = obj.optInt(StockDataAPIHelper.SOHU_JSON_STATUS);
                if (obj.has(StockDataAPIHelper.SOHU_JSON_HQ) && status == StockDataAPIHelper.SOHU_JSON_STATUS_OK) {

                    count += stock.formSohu(dataRaw);
                    gotData = true;

                    Log.d("Print-Yes", start + ", " + end + ", " + "---------------------------------[" + count + "]");

                    if(Config.ITEM_AMOUNTS > count) {
                        end = DateHelper.offset(start, -1);
                    } else {
                        break;
                    }
                } else {

                    Log.d("Print-No", start + ", " + end + ", " + "---------------------------------[" + count + "]");
                }
            } catch (JSONException e) {
                e.printStackTrace();

                if(gotData) {
                    stock.setAllDataIsDownload(true);
                    break;
                }
            }
        } while (true);

        return count;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        super.onPostExecute(integer);
        Analyzer.getCandleActivityLoadView().setVisibility(View.GONE);
        Analyzer.getCandleActivityLoadView().clearAnimation();
    }

    private Stock stock;
}
