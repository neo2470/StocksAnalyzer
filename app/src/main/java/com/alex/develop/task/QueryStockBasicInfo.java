package com.alex.develop.task;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import com.alex.develop.entity.ApiStore;
import com.alex.develop.entity.Stock;
import com.alex.develop.util.NetworkHelper;
import com.alex.develop.util.SQLiteHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by alex on 15-9-8.
 * 查询股票基本信息
 */
public class QueryStockBasicInfo extends AsyncTask<Stock, Void, Void> {

    public QueryStockBasicInfo() {
        builder = new StringBuilder();
    }

    @Override
    protected Void doInBackground(Stock... params) {

        HashMap<String, String> header = new HashMap<>();
        header.put(ApiStore.JDWX_API_KEY, ApiStore.JDWX_API_KEY_VALUE);

        /**
         * No Data Returned Stock
         */
        HashSet<String> exc = new HashSet<>();
        exc.add("000033");
        exc.add("000520");

        final int size = 100;
        List<Stock> stocks = new ArrayList<>();

        int count = 0;
        int total = 1;
        boolean fetchData = false;
        boolean first = true;
        for (Stock stock : params) {

            if(first && stock.getCode().startsWith("0")) {
                fetchData = true;
                first = false;
            } else {
                fetchData = false;
            }

            if (fetchData || size == count || total == params.length) {
                String url = ApiStore.getStockInfoUrl(stocks.toArray(new Stock[stocks.size()]));
                String content = NetworkHelper.getWebContent(url, header, ApiStore.JDWX_CHARTSET);
                fromJSON(content, stocks);

                stocks.clear();
                count = 0;
            } else {
                ++count;
            }

            if (!exc.contains(stock.getCode())) {
                stocks.add(stock);
            }

            ++total;
        }

        Log.d("Print-Total", "查到的数据 " + statistics);
        Log.d("Print-Total", "未查到的数据 " + builder.toString());

        return null;
    }

    private void fromJSON(String content, List<Stock> stocks) {
        try {
            JSONObject jsonObject = new JSONObject(content);

            if (jsonObject.has(ApiStore.JDWX_JSON_RESULT)) {
                JSONObject result = jsonObject.optJSONObject(ApiStore.JDWX_JSON_RESULT);
                int retCode = result.optInt(ApiStore.JDWX_JSON_RETCODE);

                if (ApiStore.JDWX_JSON_STATUS_OK == retCode) {
                    JSONArray data = result.optJSONArray(ApiStore.JDWX_JSON_DATA);

                    for (int i = 0, j = 0; i < data.length(); ++i, ++j) {
                        JSONObject item = data.optJSONObject(i);
                        String listDate = item.optString(ApiStore.JDWX_JSON_LIST_DATE);
                        String ticker = item.optString(ApiStore.JDWX_JSON_TICKER);

                        Stock stock = stocks.get(j);

                        if (!ticker.equals(stock.getCode())) {

                            builder.append(stock.getCode());
                            builder.append(",");

//                            --i;
//                            continue;
                        }

                        write2SQLite(stock, listDate, ticker);
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private boolean write2SQLite(Stock stock, String listDate, String ticker) {

        SQLiteHelper dbHelper = SQLiteHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Stock.Table.Column.LIST_DATE, listDate);

        String where = Stock.Table.Column.CODE + " = ?";
        String[] whereArgs = {stock.getCode()};

        if (ticker.equals(stock.getCode())) {
            Log.d("Print-write2SQLite", stock.getName() + ", " + listDate + "<======>" + stock.getCode() + ", " + ticker);
        } else {
            Log.e("Print-write2SQLite", stock.getName() + ", " + listDate + "<======>" + stock.getCode() + ", " + ticker);
        }

        boolean flag = 1 == db.update(Stock.Table.NAME, values, where, whereArgs);

        if (flag) {
            ++statistics;
        }

        return flag;
    }

    private int statistics;
    private StringBuilder builder;
}
