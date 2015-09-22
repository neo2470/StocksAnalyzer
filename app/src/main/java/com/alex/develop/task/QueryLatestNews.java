package com.alex.develop.task;

import android.os.AsyncTask;
import android.util.Log;

import com.alex.develop.entity.ApiStore;
import com.alex.develop.entity.News;
import com.alex.develop.util.NetworkHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by alex on 15年9月21日.
 * 查询新闻信息
 */
public class QueryLatestNews extends AsyncTask<Integer, Void, Void> {

    public QueryLatestNews(ArrayList<News> news) {
        this.news = news;
    }

    @Override
    protected Void doInBackground(Integer... params) {

        HashMap<String, String> header = new HashMap<>();
        header.put(ApiStore.BAIDU_APISTORE_API_KEY, ApiStore.BAIDU_APISTORE_API_VALUE);

        final String newsUrl = ApiStore.getFinanceNewsUrl(params[0]);
        final String content = NetworkHelper.getWebContent(newsUrl, header, ApiStore.SHOW_API_CHARSET);
        formJSON(content);

        return null;
    }

    private void formJSON(String content) {
        try {
            JSONObject data = new JSONObject(content);

            JSONObject resBody = data.optJSONObject("showapi_res_body");
            JSONObject pageBean = resBody.optJSONObject("pagebean");
            JSONArray contentList = pageBean.optJSONArray("contentlist");

            final int contentSize = contentList.length();
            if(0 < contentSize) {
                for(int i=0; i<contentSize; ++i) {
                    JSONObject newsObj = contentList.optJSONObject(i);
                    news.add(new News(newsObj));
                }
            } else {
                // TODO 提示用户没有更多数据了
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private ArrayList<News> news;
}
