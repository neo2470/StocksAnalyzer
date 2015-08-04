package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.util.UnitHelper;

import java.util.List;

/**
 * Created by alex on 15-6-14.
 * 自定义View，主要用于绘制K线图
 */
public class CandleView extends View {

    public interface onCandlestickSelectedListener {
        void onSelected(Candlestick candlestick);
    }

    public CandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public void setStock(Stock stock) {
        this.stock = stock;
        requestData();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;

        float divider = h * 0.8f;

        if(null != kArea) {
            kArea.right = w;
            kArea.bottom = divider;
        }

        if(null != qArea) {
            qArea.right = w;
            qArea.top = divider;
            qArea.bottom = h;
        }

        Config.updateConfig(width);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                crosshairs = true;
                touch.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE :
                crosshairs = true;
                touch.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                crosshairs = false;
                break;
        }

        // 只在绘制区域内显示十字线
        if(!kArea.contains(event.getX(), event.getY())) {
            crosshairs = false;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        pen.setStyle(Paint.Style.STROKE);
        pen.setColor(Color.parseColor("#424242"));
        canvas.drawRect(kArea, pen);
        canvas.drawRect(qArea, pen);

        drawCandlesticks(canvas);

        // 绘制十字线crosshairs
        if (crosshairs) {
            pen.setColor(Color.WHITE);
            canvas.drawLine(kArea.left, touch.y, kArea.right, touch.y, pen);
            canvas.drawLine(touch.x, 0, touch.x, height, pen);
        }
    }

    /**
     * 绘制K线图形的表格背景
     * @param canvas
     */
    private void drawLines(Canvas canvas) {

        // 绘制表格背景
        pen.setColor(Color.WHITE);
        pen.setTextSize(UnitHelper.sp2px(15));

        // 左侧边界线
        canvas.drawLine(kArea.left, 0, kArea.left, kArea.bottom, pen);

        float xWidth = kArea.width() / tdNum;
        float yHeight = kArea.height() / trNum;

        float x, y;
        boolean xEnd = false, yEnd = false;
        for(int i=1,j=1;;++i,++j) {

            x = kArea.left + xWidth*i;
            y = yHeight*j;

            // 竖线
            if(i < tdNum) {
                canvas.drawLine(x, 0, x, kArea.bottom, pen);
            } else {
                xEnd = true;
            }

            // 横线
            if(j < trNum) {
//                canvas.drawText("100.00", 0, y+5, pen);
                canvas.drawLine(kArea.left, y, kArea.right, y, pen);
            } else {
                yEnd = true;
            }

            if(xEnd && yEnd) {
                break;
            }
        }
    }

    /**
     * 绘制K线
     */
    private void drawCandlesticks(Canvas canvas) {

        float x = kArea.left + Config.itemSpace;

        CandleList data = stock.getCandleList();
        Config.setRatio(kArea.height(), data.getHigh()-data.getLow());

        for (int i=data.size()-1; i>=0; --i) {
            Node node = data.get(i);
            for(Candlestick candle : node.getCandlesticks()) {
                candle.draw(x, canvas, pen);
                x += Config.itemWidth + Config.itemSpace;
            }
        }
    }


    /**
     * 绘制指标VOL
     */
    private void drawVOL() {}

    private void requestData() {

        new QueryStockHistory(stock) {

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                invalidate();
            }
        }.execute(Enum.Month.Jul, Enum.Period.Day);

    }



    private void initialize() {
        pen = new Paint();
        pen.setTextSize(30);
        pen.setAntiAlias(true);
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(1);

        touch = new PointF();
        kArea = new RectF();
        kArea.left = 70;
        qArea = new RectF();
        qArea.left = 70;

        crosshairs = false;
    }

    private Paint pen;// 画笔
    private PointF touch;// 触点，当用户点击K线图形时，绘制十字线，用于告知用户当前查看的是那一天的K线
    private RectF kArea;// 绘制K线部分图形区域
    private RectF qArea;// 绘制指标部分区域

    private Stock stock;


    private boolean crosshairs;// 是否绘十字准线

    private int width;// View的宽度
    private int height;// View的高度
    private int trNum = 10;// 背景表格行数
    private int tdNum = 5;// 背景表格列数
}
