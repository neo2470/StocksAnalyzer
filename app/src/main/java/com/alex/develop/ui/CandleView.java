package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.entity.Stock;
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

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        width = w;
        height = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                crosshairs = true;
                touch.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE :
                touch.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                crosshairs = false;
                break;
        }

        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawLines(canvas);
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }

    private void drawLines(Canvas canvas) {

        // 绘制表格背景
        pen.setColor(Color.WHITE);
        pen.setTextSize(UnitHelper.sp2px(15));

        float left = pen.measureText("100.00");
        float right = pen.measureText("10.01%");

        canvas.drawLine(left, 0, left, height, pen);
        canvas.drawLine(width-right, 0, width-right, height, pen);

        int xWidth = (int) ((width-left-right) / tdNum);
        int yHeight = height / trNum;

        float x, y;
        boolean xEnd = false, yEnd = false;
        for(int i=1,j=1;;++i,++j) {

            x = left + xWidth*i;
            y = yHeight*j;

            // 竖线
            if(i < tdNum) {
                canvas.drawLine(x, 0, x, height, pen);
            } else {
                xEnd = true;
            }

            // 横线
            if(j < trNum) {
                canvas.drawText("100.00", 0, y+5, pen);
                canvas.drawText("10.01%", width-right, y+5, pen);
                canvas.drawLine(left, y, width-right, y, pen);
            } else {
                yEnd = true;
            }

            if(xEnd && yEnd) {
                break;
            }
        }

        // 绘制十字线crosshairs
        if (crosshairs) {
            pen.setColor(Color.WHITE);
            canvas.drawLine(0, touch.y, width, touch.y, pen);
            canvas.drawLine(touch.x, 0, touch.x, height, pen);
        }
    }

    private void initialize() {
        pen = new Paint();
        pen.setTextSize(30);
        pen.setAntiAlias(true);
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(1);

        touch = new PointF();
        crosshairs = false;
    }

    private Paint pen;// 画笔

    /**
     * 当用户点击K线图形时，绘制十字线，用于告知用户当前查看的是那一天的K线
     */
    private PointF touch;// 触点

    /**
     * 绘制K线部分图形和指标部分图形所在的区域
     */
    private Rect rect;

    private boolean crosshairs;// 是否绘十字准线

    private int width;// view的宽度
    private int height;// view的高度
    private int trNum = 10;// 背景表格行数
    private int tdNum = 5;// 背景表格列数
    private int candleHeight;// K线部分view的高度
    private int quotaHeight;// 指标部分view的高度


    private Stock stock;
}
