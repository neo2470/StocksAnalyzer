package com.alex.develop.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Debug;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.develop.entity.*;
import com.alex.develop.entity.Enum;
import com.alex.develop.task.QueryStockHistory;
import com.alex.develop.util.UnitHelper;

import java.util.Random;

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

    public void setStock(final Stock stock) {

        this.stock = stock;
        csr.setCandleList(this.stock.getCandleList());

        // 重置游标成功，则说明已经下载过数据，无需重复下载
        // TODO 此处的逻辑貌似有问题，发现了一个BUG
        if(stock.resetCursor()) {
            updateParameters();
            return ;
        }

        new QueryStockHistory(this.stock) {

            @Override
            protected void onPostExecute(Integer integer) {
                super.onPostExecute(integer);
                stock.resetCursor();
                updateParameters();
            }
        }.execute(Enum.Month.Jul, Enum.Period.Day);
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
            drawCross = false;
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

        drawTextAndLine(canvas);

    }

    public void updateParameters() {
        CandleList data = stock.getCandleList();
        kCfg.setRatio(kArea.height(), data.getHighest() - data.getLowest());
        kCfg.setReferValue(data.getLowest());

        qCfg.setRatio(qArea.height(), data.getVolume());
        qCfg.setReferValue(0);

        highestValue.setText(String.format("%.2f", data.getHighest()));
        lowestValue.setText(String.format("%.2f", data.getLowest()));

        invalidate();
    }

    private void selectCandlestick(MotionEvent event) {
        drawCross = true;

        float x = event.getX();
        float y = event.getY();

        touch.set(x, y);

        // 使得十字线自动吸附K线
        String[] temp  = String.format("%.2f", (x-kArea.left) / (Config.itemWidth + Config.itemSpace)).split("\\.");
        int intSub = Integer.valueOf(temp[0]);
        float floatSub = Float.valueOf("0." + temp[1]);

        if(floatSub > Config.ITEM_SPACE_WIDTH_RATIO/(1+Config.ITEM_SPACE_WIDTH_RATIO)) {
            ++intSub;
        }

        stock.getStart().copy(csr);
        csr.move(intSub);

        CandleList data = stock.getCandleList();
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
        Cursor ed = stock.getEnd();
        Cursor st = stock.getStart();

        if(0 < data.size()) {
            for (int i = st.node; i >= ed.node; --i) {
                Node node = data.get(i);

                // 计算特殊情况下遍历的开始和结束的位置
                int start = i == st.node ? st.candle : 0;
                int stop = i == ed.node ? ed.candle : node.size() - 1;

                for (int j = start; j <= stop; ++j) {
                    Candlestick candle = node.get(j);
//                    pen.setColor(Color.RED);
//                    canvas.drawText(j+"", x, 50, pen);
                    candle.drawCandle(x, kCfg, canvas, pen);
                    candle.drawVOL(x, qCfg, canvas, pen);
                    x += Config.itemWidth + Config.itemSpace;
//                    Log.d("Print Candlestick # " + j, candle.getDate() + ", " + candle.getLow() + ", " + candle.getHigh() + ", " + candle.getIncreaseString());
                }
            }
        }
    }

    private void drawTextAndLine(Canvas canvas) {


        // 绘制十字线及其对应得坐标
        if (drawCross) {

            // 绘制十字线
            pen.setColor(Color.WHITE);
            canvas.drawLine(kArea.left, touch.y, kArea.right, touch.y, pen);
            canvas.drawLine(touch.x, 0, touch.x, height, pen);

            // 绘制横坐标
            float value = 0.00f;
            boolean inKArea = true;// 手指是否在K线区域内
            if(kArea.top < touch.y && touch.y < kArea.bottom) {
                value = kCfg.px2val(touch.y);
                inKArea = true;
            }

            if(qArea.top < touch.y && touch.y < qArea.bottom) {
                value = qCfg.px2val(touch.y);
                inKArea = false;
            }

            float x1 = 0;
            float y1 = touch.y - textValue.getBound().height() / 2;

            // 考虑[上下]边界情况
            if(inKArea) {
                y1 = kArea.top > y1 ? kArea.top : y1;
                if (kArea.bottom < y1 + textValue.getBound().height()) {
                    y1 = kArea.bottom - textValue.getBound().height();
                }
            } else {
                y1 = qArea.top > y1 ? qArea.top : y1;
                if (qArea.bottom < y1 + textValue.getBound().height()) {
                    y1 = qArea.bottom - textValue.getBound().height();
                }
            }

            // 手指移出kArea顶部的时候显示最大值
            if(y1 == kArea.top) {
                value = stock.getCandleList().getHighest();
            }

            textValue.setText(String.format("%.2f", value));
            textValue.draw(x1, y1, canvas);// 绘制K线纵坐标(价格)

            float x2 = touch.x - dateValue.getBound().width() / 2;
            float y2 = kArea.bottom - dateValue.getBound().height();

            // 考虑[左右]边界情况
            x2 = 0 > x2 ? 0 : x2;
            if(kArea.right < x2 + dateValue.getBound().width()) {
                x2 = kArea.right - dateValue.getBound().width();
            }

            Candlestick candle = stock.getCandleList().get(csr.node).get(csr.candle);
            dateValue.setText(candle.getDate());
            dateValue.draw(x2, y2, canvas);// 绘制K线日期
        }

        // 绘制课时区域内股票的最高价和最低价
        CandleList data = stock.getCandleList();
        if(0 < data.size()) {
            Candlestick highest = data.getCandlestickHigh();
            Candlestick lowest = data.getCandlestickLow();

            float[] high = getDrawTextXY(highest, true, highestValue);
            pen.setColor(highestValue.getTextColor());
            canvas.drawLine(high[0], high[1], high[2], high[3], pen);
            highestValue.draw(high[4], high[5], canvas);

            float[] low = getDrawTextXY(lowest, false, lowestValue);
            pen.setColor(lowestValue.getTextColor());
            canvas.drawLine(low[0], low[1], low[2], low[3], pen);
            lowestValue.draw(low[4], low[5], canvas);
        }
    }

    /**
     * 计算绘制最高价和最低价所需的坐标信息
     * @param candle 目标K线
     * @param highOrLow true，最高价；false，最低价
     * @param textValue 将被绘制的价格字符串
     * @return
     *
     * [0] : 价格指示线条的起始坐标X
     * [1] : 价格指示线条的起始坐标Y
     * [2] : 价格指示线条的结束坐标X
     * [3] : 价格指示线条的结束坐标Y
     * [4] : 价格字符串绘制的起始坐标X
     * [5] : 价格字符串绘制的起始坐标Y
     */
    private float[] getDrawTextXY(Candlestick candle, boolean highOrLow, TextValue textValue) {
        float[] data = new float[6];

        data[0] = candle.getCenterXofArea();
        data[1] = kCfg.val2px(highOrLow ? candle.getHigh() : candle.getLow());

        if(highOrLow) {
            data[3] = data[1] + Config.ITEM_MARK_OFFSET_Y;
        } else {
            data[3] = data[1] - Config.ITEM_MARK_OFFSET_Y;
        }

        data[5] = data[3] - textValue.getBound().height() / 2;

        if(kArea.left > data[0]-Config.ITEM_MARK_OFFSET_X-textValue.getBound().width()) {// 必须绘制在右侧
            data[2] = data[0] + Config.ITEM_MARK_OFFSET_X;
            data[4] = data[2];
        } else if(kArea.right < data[0]+Config.ITEM_MARK_OFFSET_X+highestValue.getBound().width()) {// 必须绘制在左侧
            data[2] = data[0] - Config.ITEM_MARK_OFFSET_X;
            data[4] = data[2] - textValue.getBound().width();
        } else {// 左右皆可
            if(leftOrRight) {// 左侧
                data[2] = data[0] - Config.ITEM_MARK_OFFSET_X;
                data[4] = data[2] - textValue.getBound().width();
            } else {// 右侧
                data[2] = data[0] + Config.ITEM_MARK_OFFSET_X;
                data[4] = data[2];
            }

            // 保证当最高价K线和最低价K线都在屏幕中间时，两者的指示线方向相反
            // TODO 此功能存在BUG
//            leftOrRight = !leftOrRight;
        }

        return data;
    }

    private void initialize() {
        pen = new Paint();
        pen.setTextSize(30);
        pen.setAntiAlias(true);
        pen.setStyle(Paint.Style.FILL_AND_STROKE);
        pen.setStrokeWidth(UnitHelper.dp2px(1));

        touch = new PointF();
        kArea = new RectF();
        kCfg = new Config();

        qArea = new RectF();
        qCfg = new Config();

        final float textSize = UnitHelper.sp2px(15);

        textValue = new TextValue();
        textValue.setTextSize(textSize);

        dateValue = new TextValue();
        dateValue.setTextSize(textSize);

        highestValue = new TextValue();
        highestValue.setTextSize(textSize);
        highestValue.setShowBorder(false);
        highestValue.setAlpha(255);

        lowestValue = new TextValue();
        lowestValue.setTextSize(textSize);
        lowestValue.setShowBorder(false);
        lowestValue.setAlpha(255);

        csr = new Cursor();

        drawCross = false;

        leftOrRight = 0 == new Random().nextInt() >> 1;
    }

    private Paint pen;// 画笔
    private PointF touch;// 触点，当用户点击K线图形时，绘制十字线，用于告知用户当前查看的是那一天的K线
    private RectF kArea;// 绘制K线部分图形区域
    private Config kCfg;// K线图的配置信息
    private RectF qArea;// 绘制指标部分区域
    private Config qCfg;// 指标图的配置信息

    private TextValue textValue;
    private TextValue dateValue;
    private TextValue highestValue;
    private TextValue lowestValue;

    private Stock stock;
    private Cursor csr;

    private onCandlestickSelectedListener listener;

    private boolean drawCross;// 是否绘十字准线
    private boolean leftOrRight;

    private int width;// View的宽度
    private int height;// View的高度
    private int trNum = 10;// 背景表格行数
    private int tdNum = 5;// 背景表格列数
}
