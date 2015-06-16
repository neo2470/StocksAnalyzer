package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.Candlestick;

import java.util.List;

/**
 * Created by alex on 15-6-14.
 */
public class CandleView extends View {

    public CandleView(Context context) {
        super(context);
        initialize();
    }

    public CandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                touch.set(event.getX(), event.getY());
                invalidate();
                return true;
            case MotionEvent.ACTION_MOVE :
                touch.set(event.getX(), event.getY());
                invalidate();
                return true;
            default:
                return super.onTouchEvent(event);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(null != candles) {
            float x = 0;
            for (Candlestick candle : candles) {
                candle.draw(x, canvas, pen);
                x += (Candlestick.WIDTH + Candlestick.SPACE);
            }
        }

        if (0 < touch.x && 0 < touch.y) {
            canvas.drawLine(0, touch.y, 1000, touch.y, pen);
            canvas.drawLine(touch.x, 0, touch.x, 1000, pen);
            canvas.drawCircle(touch.x, touch.y, 10, pen);
        }
    }

    public void setCandles(List<Candlestick> candles) {
        this.candles = candles;
        invalidate();
    }


    private void initialize() {
        pen = new Paint();
        pen.setTextSize(30);
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(1);

        touch = new PointF();
    }

    private List<Candlestick> candles;
    private PointF touch;
    private Paint pen;
}
