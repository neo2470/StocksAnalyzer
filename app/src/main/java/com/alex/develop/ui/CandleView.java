package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.util.UnitHelper;

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

    public void setOnCandlestickSelectedListener(onCandlestickSelectedListener listener) {
        this.listener = listener;
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
            Config.init(kArea.width());
            kCfg.setReferYpx(kArea.bottom);
        }


        if(null != qArea) {
            qArea.right = w;
            qArea.top = divider;
            qArea.bottom = h;
            qCfg.setReferYpx(qArea.bottom);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN :
                selectCandlestick(event);
                break;
            case MotionEvent.ACTION_MOVE :
                selectCandlestick(event);
                break;
        }

        // 只在绘制区域内显示十字线
        if(kArea.left > event.getX()) {
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

    private void selectCandlestick(MotionEvent event) {
        crosshairs = true;

        float x = event.getX();
        float y = event.getY();

        touch.set(x, y);

        // 使得十字线自动吸附K线
        String[] temp  = String.format("%.2f", (x-kArea.left) / (Config.itemWidth + Config.itemSpace)).split("\\.");
        int intSub = Integer.valueOf(temp[0]);
        float floatSub = Float.valueOf("0."+temp[1]);

        if(floatSub > Config.ITEM_SPACE_WIDTH_RATIO/(1+Config.ITEM_SPACE_WIDTH_RATIO)) {
            ++intSub;
        }

        Cursor csr = new Cursor();
        CandleList data = stock.getCandleList();
        if(intSub <= ed.candle+1) {
            csr.node = ed.node;
            csr.candle = --intSub;
        } else {

            // TODO 下面的代码有待测试验证
            int nIndex = ed.node;
            int cIndex = ed.candle - intSub;
            while (true) {
                --nIndex;
                Node node = data.get(nIndex);
                cIndex += node.size()-1;
                if(0<=cIndex) {
                    break;
                }
            }

            csr.node = nIndex;
            csr.candle = cIndex;
        }

//        Log.d("Debug-Select", intSub + ", " + floatSub);

        if(csr.candle < 0) {
            csr.candle = 0;
        } else if(csr.candle > ed.candle) {
            csr.candle = ed.candle;
        }

        Candlestick candle = data.get(csr.node).get(csr.candle);
        touch.x = candle.getCenterXofArea();
        listener.onSelected(candle);
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

        if(0 < data.size()) {
            for (int i = ed.node; i >= st.node; --i) {
                Node node = data.get(i);
                for (int j = st.candle; j <= ed.candle; ++j) {
                    Candlestick candle = node.get(j);
//                    pen.setColor(Color.RED);
//                    canvas.drawText(j+"", x, 50, pen);
                    candle.drawCandle(x, kCfg, canvas, pen);
                    candle.drawVOL(x, qCfg, canvas, pen);
                    x += Config.itemWidth + Config.itemSpace;
                    Log.d("Print Candlestick # " + j, candle.getDate() + ", " + candle.getLow() + ", " + candle.getHigh() + ", " + candle.getIncreaseString());
                }
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

                CandleList data = stock.getCandleList();
                kCfg.setRatio(kArea.height(), data.getHigh()-data.getLow());
                kCfg.setReferValue(data.getLow());

                qCfg.setRatio(qArea.height(), data.getVolume());
                qCfg.setReferValue(0);

                ed.node = data.size() - 1;
                ed.candle = data.get(ed.node).size() - 1;
                st.node = data.size() - 1;
                st.candle = 0;

                invalidate();
            }
        }.execute(Enum.Month.Jul, Enum.Period.Day);

    }



    private void initialize() {
        pen = new Paint();
        pen.setTextSize(30);
        pen.setAntiAlias(true);
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(UnitHelper.dp2px(1));

        touch = new PointF();
        kArea = new RectF();
        kArea.top = 10;
        kArea.left = 70;
        kCfg = new Config();

        qArea = new RectF();
        qArea.left = 70;
        qCfg = new Config();

        st = new Cursor();
        ed = new Cursor();

        crosshairs = false;
    }

    private Paint pen;// 画笔
    private PointF touch;// 触点，当用户点击K线图形时，绘制十字线，用于告知用户当前查看的是那一天的K线
    private RectF kArea;// 绘制K线部分图形区域
    private Config kCfg;// K线图的配置信息
    private RectF qArea;// 绘制指标部分区域
    private Config qCfg;// 指标图的配置信息

    private Stock stock;
    private Cursor st;// 被绘制的K线的起始位置
    private Cursor ed;// 被绘制的K线的结束位置

    private onCandlestickSelectedListener listener;


    private boolean crosshairs;// 是否绘十字准线

    private int width;// View的宽度
    private int height;// View的高度
    private int trNum = 10;// 背景表格行数
    private int tdNum = 5;// 背景表格列数
}
