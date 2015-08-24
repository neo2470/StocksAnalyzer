package com.alex.develop.entity;

/**
 * Created by alex on 15-8-4.
 * 根据画板的尺寸来计算K线显示的宽度、间距以及某种
 * 类型的值与画板尺寸单位px的比值（用于数值转换）
 */
public class Config {

    /**
     * 计算K线的宽度和间距(px)
     * @param width 画板（绘制K线区域）的宽度
     */
    public static void init(float width) {
        itemWidth = width / (ITEM_AMOUNTS + (1 + ITEM_AMOUNTS) * ITEM_SPACE_WIDTH_RATIO);
        itemSpace = itemWidth * ITEM_SPACE_WIDTH_RATIO;
    }

    /**
     * 计算某种量与画布高度的比，用于量与画布尺寸(px)的相互转换
     * @param height 画布的高度(px)
     * @param value 某种量的最大值与最小值之差
     *
     * @example 在绘制K线图的情况下：
     * height 表示K线图画板的高度,
     * value 表示股票的最高价格与最低价格之差（某个特定区间内）
     *
     * @example 在绘制指标VOL(成交量)的情况下：
     * height 表示绘制指标的画板的高度
     * value 表示成交量的最大值与最小值(0)之差（某个特定区间内）
     */
    public void setRatio(float height, float value) {
        ratio = value / height;
    }

    /**
     * 设置y=0时，对应屏幕Y方向上的坐标(px)
     * @param px y=0时，对应在屏幕上的位置
     */
    public void setReferYpx(float px) {
        referY = px;
    }

    /**
     * 设置y=0时，对应的某种量的值
     * @param value y=0时，对应的某种量的值
     */
    public void setReferValue(float value) {
        referV = value;
    }

    /**
     * 将某种量转换为px
     * @param value 某种量的值
     * @return px
     */
    public float val2px(float value) {
        return referY - (value - referV) / ratio;
    }

    public float px2val(float px) {
        return referV + px * ratio;
    }

    public static float itemWidth;// K线的宽度
    public static float itemSpace;// K线的间隔

    public static final int ITEM_AMOUNTS = 30;// 初始状态下，屏幕上显示的K线个数
    public static final float ITEM_SPACE_WIDTH_RATIO = 0.3f;// K线间隔占K线宽度的比例

    private float referV;// y=0时，对应的某种量的值
    private float referY;// y=0时，对应在屏幕上的位置
    private float ratio;// 某种量与画布高度的比值
}
