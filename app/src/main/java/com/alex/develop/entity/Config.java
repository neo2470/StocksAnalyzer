package com.alex.develop.entity;

import com.alex.develop.util.UnitHelper;

/**
 * Created by alex on 15-8-4.
 */
public class Config {

    public static void setRatio(float height, float high, float low) {
        ratio = (high-low) / height;
        Config.low = low;
    }

    public static void setAxisY(float y) {
        refery = y;
    }

    public static float val2px(float value) {
        return refery - (value-low) / ratio;
    }

    public static float px2val(float px) {
        return px * ratio;
    }

    public static float itemWidth = UnitHelper.dp2px(15);
    public static float itemSpace = UnitHelper.dp2px(5);

    public static final int ITEM_AMOUNTS = 30;
    public static final float ITEM_SPACE_WIDTH_RATIO = 0.3f;

    private static float low;
    private static float refery;
    private static float ratio;
}
