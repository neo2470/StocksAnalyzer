package com.alex.develop.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alex.develop.entity.Stock;
import com.alex.develop.settings.StockDataAPI;
import com.alex.develop.stockanalyzer.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by alex on 15-5-25.
 * 蜡烛线绘制
 */
public class CandleFragment extends BaseFragment {

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.candle_fragment, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        stockData = (TextView) act.findViewById(R.id.stockData);
        new PullStockHistory().execute(stock.getId());
    }

    private Stock stock;
    private TextView stockData;
    private class PullStockHistory extends AsyncTask<String, Integer, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            StringBuilder builder = new StringBuilder();

            String api = StockDataAPI.getHistoryUrl(stock.getId());
            try {
                URL url = new URL(api);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream inputStream = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                String line = null;
                while(null != (line=reader.readLine())) {
                    builder.append(line);
                    builder.append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(null != urlConnection) {
                    urlConnection.disconnect();
                }
            }

            return builder.toString();
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            stockData.setText(s);
        }
    }
}
