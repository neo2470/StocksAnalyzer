package com.alex.develop.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by alex on 15-5-24.
 * 读取网络数据
 */
public class NetworkHelper {

    public static String getWebContent(String webUrl, String charset) {
        return getWebContent(webUrl, null, charset);
    }

    /**
     * 读取一张网页的内容
     * @param webUrl 网页对应的URL
     * @return 网页内容字符串
     */
    public static String getWebContent(String webUrl, HashMap<String, String> header, String charset) {
        HttpURLConnection urlConnection = null;
        StringBuilder builder = new StringBuilder();
        try {
            URL url = new URL(webUrl);
            urlConnection = (HttpURLConnection) url.openConnection();

            if(null != header) {
                for(String key : header.keySet()) {
                    urlConnection.setRequestProperty(key, header.get(key));
                }
            }

            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charset));

            String line;
            while (null != (line=bufferedReader.readLine())) {
                builder.append(line);
            }

            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != urlConnection) {
                urlConnection.disconnect();
            }
        }
        return builder.toString();
    }

    private NetworkHelper(){}
}
