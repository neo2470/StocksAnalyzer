package com.alex.develop.entity;

/**
 * Created by alex on 15-8-4.
 */
public class Config {

    public static void init(float width) {
        itemWidth = width / (ITEM_AMOUNTS + (1 + ITEM_AMOUNTS) * ITEM_SPACE_WIDTH_RATIO);
        itemSpace = itemWidth * ITEM_SPACE_WIDTH_RATIO;
    }

    public void setRatio(float height, float high, float low) {
        ratio = (high - low) / height;
        this.low = low;
    }

    public void setAxisY(float y) {
        referY = y;
    }

    public float val2px(float value) {
        return referY - (value - low) / ratio;
    }

    public static float itemWidth;
    public static float itemSpace;

    public static final int ITEM_AMOUNTS = 30;
    public static final float ITEM_SPACE_WIDTH_RATIO = 0.3f;

    private float low;
    private float referY;
    private float ratio;
}
