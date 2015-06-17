package com.alex.develop.entity;

/**
 * Created by alex on 15-6-17.
 */
public final class Enum extends BaseObject {

    /**
     * 排序方式
     */
    public enum Order {
        ASC,// 正序
        DESC;// 逆序

        @Override
        public String toString() {
            switch (this) {
                case ASC:
                    return "A";
                case DESC:
                    return "D";
                default:
                    return super.toString();
            }
        }
    }

    /**
     * 周期
     */
    public enum Period {
        Day,// 天
        Week,// 周
        Month;// 月

        @Override
        public String toString() {
            switch (this) {
                case Day:
                    return "d";
                case Week:
                    return "w";
                case Month:
                    return "m";
                default:
                    return super.toString();
            }
        }
    }
}
