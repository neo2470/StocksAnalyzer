package com.alex.develop.entity;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

import com.alex.develop.util.UnitHelper;

/**
 * Created by alex on 15-8-25.
 * 绘制一个带边框的文本
 */
public class TextValue {

    public TextValue() {
        rect = new RectF();

        pen = new Paint();
        pen.setAntiAlias(true);

        padding = UnitHelper.dp2px(2);

        textColor = Color.WHITE;
        rectColor = Color.GRAY;
    }

    public void setText(String text) {
        this.text = text;
        measureText();
    }

    public void setTextSize(float fontSize) {
        pen.setTextSize(fontSize);
        measureText();
    }

    public void setTextColor(int color) {
        textColor = color;
    }

    public void setRectColor(int color) {
        rectColor = color;
    }

    public void setPadding(float padding) {
        this.padding = padding;
        measureText();
    }

    public float getRight() {
        return  rect.right;
    }

    public void draw(float x, float y, Canvas canvas) {

        if(null != text) {

            x += pen.getStrokeWidth();

            float dX = padding;
            float dy = rect.height() / 2;

            rect.offsetTo(x, y - dy);

            pen.setColor(rectColor);
            pen.setAlpha(alpha);
            pen.setStyle(Paint.Style.FILL_AND_STROKE);

            canvas.drawRect(rect, pen);

            pen.setColor(textColor);
            pen.setAlpha(alpha);
            pen.setStyle(Paint.Style.FILL);

            canvas.drawText(text, x + dX, y - (dy + pen.getFontMetrics().top), pen);
        }
    }

    private void measureText() {

        if(null == text) {
            return;
        }

        Paint.FontMetrics fontMetrics = pen.getFontMetrics();
        rect.left = 0;
        rect.top = 0;
        rect.right = pen.measureText(text) + padding * 2;
        rect.bottom = fontMetrics.bottom - fontMetrics.top;
    }

    private Paint pen;
    private RectF rect;
    private String text;

    private int rectColor;
    private int textColor;
    private final int alpha = 200;

    private float padding;
}
