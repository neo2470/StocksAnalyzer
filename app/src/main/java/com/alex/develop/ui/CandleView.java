package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(null != candles) {
            float x = 0;
            for (Candlestick candle : candles) {
                candle.draw(x, canvas, pen);
                x += (Candlestick.WIDTH + Candlestick.SPACE);
            }
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
    }

    private List<Candlestick> candles;
    private Paint pen;
}
