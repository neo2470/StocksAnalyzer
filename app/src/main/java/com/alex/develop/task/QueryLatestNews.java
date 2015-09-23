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
public class QueryLatestNews extends AsyncTask<Integer, Void, Boolean> {

    public QueryLatestNews(ArrayList<News> news) {
        this.news = news;
        NEWS_PAGES = Integer.MAX_VALUE;
    }

    public static int getNewsPages() {
        return NEWS_PAGES;
    }

    @Override
    protected Boolean doInBackground(Integer... params) {

        page = params[0];

        HashMap<String, String> header = new HashMap<>();
        header.put(ApiStore.BAIDU_APISTORE_API_KEY, ApiStore.BAIDU_APISTORE_API_VALUE);

        final String newsUrl = ApiStore.getFinanceNewsUrl(page);
        final String content = NetworkHelper.getWebContent(newsUrl, header, ApiStore.SHOW_API_CHARSET);
        return formJSON(content);
    }

    private boolean formJSON(String content) {
        boolean flag = false;
        try {
            JSONObject data = new JSONObject(content);

            final String resBodyKey = "showapi_res_body";
            if(!data.has(resBodyKey)) {
                return flag;
            }

            JSONObject resBody = data.optJSONObject(resBodyKey);
            JSONObject pageBean = resBody.optJSONObject("pagebean");
            JSONArray contentList = pageBean.optJSONArray("contentlist");

            NEWS_PAGES = pageBean.optInt("allPages");
            if(null == cache) {
                cache = new ArrayList<>();
            }

            final int contentSize = contentList.length();
            if(0 < contentSize) {
                for(int i=0; i<contentSize; ++i) {
                    JSONObject newsObj = contentList.optJSONObject(i);

                    if(1 == page) {

                        // TODO 下拉刷新 not be tested
                        final String nid = newsObj.optString("nid");
                        News latestNews = news.get(0);
                        if(nid.equals(latestNews.getNid())) {
                            cache.addAll(news);
                            news = cache;
                            cache = null;
                            break;
                        } else {
                            cache.add(new News(newsObj));
                        }

                    } else {
                        news.add(new News(newsObj));
                    }
                }
            } else {
                // TODO 提示用户没有更多数据了
            }

            flag = true;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return flag;
    }

    private static int NEWS_PAGES;// 新闻的总页数（每页约20条数据）

    private ArrayList<News> cache;// 存储下拉刷新得到的数据
    private ArrayList<News> news;
    private int page;// 当前读书数据的页数
}
