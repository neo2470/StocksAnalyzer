package com.alex.develop.task;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.alex.develop.entity.Stock;
import com.alex.develop.stockanalyzer.Analyzer;
import com.alex.develop.util.SQLiteHelper;

/**
 * Created by alex on 15-7-15.
 * 收藏或者取消收藏Stock
 */
public class CollectStockTask extends AsyncTask<Stock, Void, Boolean> {

    /**
     *
     * @param collect 是否收藏该Stock,1表示收藏；0表示取消收藏
     */
    public CollectStockTask(int collect) {
        this.collect = collect;
    }

    @Override
    protected Boolean doInBackground(Stock... params) {

        SQLiteHelper dbHelper = SQLiteHelper.getInstance();
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Stock.Table.Column.COLLECT, collect);
        values.put(Stock.Table.Column.COLLECT_STAMP, System.currentTimeMillis());

        String where = Stock.Table.Column.CODE + " = ?";
        String[] whereArgs = {params[0].getCode()};

        int flag = db.update(Stock.Table.NAME, values, where, whereArgs);

        return 0 < flag;
    }

    @Override
    protected void onPostExecute(Boolean flag) {
        String result;
        Context context = Analyzer.getContext();

        if(flag) {
            result = "添加成功";
        } else {
            result = "添加失败";
        }

        Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
    }

    private int collect;
}
