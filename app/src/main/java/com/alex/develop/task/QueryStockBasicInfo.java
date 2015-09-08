package com.alex.develop.task;

import android.os.AsyncTask;

import com.alex.develop.entity.ApiStore;
import com.alex.develop.entity.Stock;
import com.alex.develop.util.NetworkHelper;

import java.util.HashMap;

/**
 * Created by alex on 15-9-8.
 * 查询股票基本信息
 */
public class QueryStockBasicInfo extends AsyncTask<Stock, Void, Void> {

    @Override
    protected Void doInBackground(Stock... params) {

        HashMap<String, String> header = new HashMap<>();
        header.put(ApiStore.JDWX_API_KEY, ApiStore.JDWX_API_KEY_VALUE);

        String url = ApiStore.getStockInfoUrl(params);
        String data = NetworkHelper.getWebContent(url, header, ApiStore.JDWX_CHARTSET);

        return null;
    }
}
