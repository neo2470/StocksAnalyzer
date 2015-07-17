package com.alex.develop.task;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.widget.Toast;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.stockanalyzer.R;
import com.alex.develop.util.SQLiteHelper;

/**
 * Created by alex on 15-7-15.
 * 收藏或者取消收藏Stock，使用异步任务的方式更改SQLite
 */
public class CollectStockTask extends AsyncTask<Stock, Void, String> {

    /**
     * 构造方法
     * @param collect 是否收藏该Stock,1表示收藏；0表示取消收藏
     */
    public CollectStockTask(int collect) {
        this.collect = collect;
    }

    @Override
    protected String doInBackground(Stock... params) {

        SQLiteHelper dbHelper = SQLiteHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Stock stock = params[0];
        stock.setCollectStamp(System.currentTimeMillis());

        ContentValues values = new ContentValues();
        values.put(Stock.Table.Column.COLLECT, collect);
        values.put(Stock.Table.Column.COLLECT_STAMP, stock.getCollectStamp());

        String where = Stock.Table.Column.CODE + " = ?";
        String[] whereArgs = {params[0].getCode()};

        boolean flag = 0 < db.update(Stock.Table.NAME, values, where, whereArgs);

        int resId = 1 == collect ? R.string.collect_stock_success : R.string.remove_stock_success;
        if(!flag) {
            resId = 1 == collect ? R.string.collect_stock_failure : R.string.remove_stock_failure;
        }

        String result = Analyzer.getContext().getString(resId);
        result = String.format(result, stock.getName());

        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        Toast.makeText(Analyzer.getContext(), result, Toast.LENGTH_SHORT).show();
    }

    private int collect;
}
