package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.Candlestick;
import com.alex.develop.entity.Stock;

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

        // 绘制K线

        // 绘制分割线
        canvas.drawLine(0, candleHeight, width, candleHeight, pen);

        // 绘制指标

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
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(1);

        touch = new PointF();
        crosshairs = false;
    }

    private Paint pen;// 画笔
    private PointF touch;// 触点
    private boolean crosshairs;// 是否绘十字准线

    private int width;// view的宽度
    private int height;// view的高度
    private int candleHeight;// K线部分view的高度
    private int quotaHeight;// 指标部分view的高度

    private Stock stock;
}
