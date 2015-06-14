package com.alex.develop.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.alex.develop.entity.Candlestick;

import java.util.List;

/**
 * Created by alex on 15-6-14.
 */
public class CandleView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    public CandleView(Context context) {
        super(context);
    }

    public CandleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setCandles(List<Candlestick> candles) {
        this.candles = candles;
    }

    public void draw() {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void run() {

    }

    private List<Candlestick> candles;
    private SurfaceHolder surfaceHolder;
}
