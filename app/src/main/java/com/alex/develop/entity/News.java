package com.alex.develop.entity;

import android.util.Log;

import com.alex.develop.util.DateHelper;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by alex on 15年9月21日.
 * 新闻类
 */
public class News {

    public News(JSONObject data) {
        nid = data.optString("nid");
        title = data.optString("title").trim();
        desc = data.optString("desc").trim();
        link = data.optString("link").trim();

        // 解析新闻配图
        final String imageUrls = "imageurls";
        if(data.has(imageUrls)) {
            final JSONArray array = data.optJSONArray(imageUrls);
            if(1 <= array.length()) {
                final JSONObject obj = array.optJSONObject(0);
                imgUrl = obj.optString("url");
            } else {
                imgUrl = null;
            }
        } else {
            imgUrl = null;
        }

        source = data.optString("source");
        pubDate = data.optString("pubDate");

        Log.d("Print", title);
        Log.d("Print", pubDate);
        Log.d("Print", source);
        Log.d("Print", "----------------------------");
    }

    public String getNid() {
        return nid;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

    public String getLink() {
        return link;
    }

    public boolean hasImage() {
        return null == imgUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getSource() {
        return source;
    }

    public String getPubDate() {
        return pubDate;
    }

    public String getPubDateFromNow() {
        return DateHelper.getDateFromNow(pubDate);
    }

    private String nid;// 新闻ID
    private String title;// 新闻标题
    private String desc;// 新闻描述
    private String link;// 具体内容链接
    private String imgUrl;// 新闻配图
    private String source;// 新闻来源
    private String pubDate;// 发布时间
}
