package com.alex.develop.entity;

import com.alex.develop.util.UnitHelper;

/**
 * Created by alex on 15-8-4.
 */
public class Config {

    public static void updateConfig(float width) {
//        itemWidth = width / (ITEM_AMOUNTS + (1 + ITEM_AMOUNTS) * ITEM_SPACE_WIDTH_RATIO);
//        itemSpace = itemWidth * ITEM_SPACE_WIDTH_RATIO;
    }

    public static void setRatio(float height, float value) {
        ratio = value / height;
    }

    public static float val2px(float value) {
        return value / ratio;
    }

    public static float px2val(float px) {
        return px * ratio;
    }

    public static float itemWidth = UnitHelper.dp2px(20);
    public static float itemSpace = UnitHelper.dp2px(5);

    public static final int ITEM_AMOUNTS = 30;
    public static final float ITEM_SPACE_WIDTH_RATIO = 0.2f;

    private static float ratio;
}
