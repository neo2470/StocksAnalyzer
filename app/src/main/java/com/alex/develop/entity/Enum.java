package com.alex.develop.entity;

/**
 * Created by alex on 15-6-17.
 * 定义一些枚举类型
 */
public final class Enum extends BaseObject {

    public interface EnumType {}

    /**
     * 排序方式
     */
    public enum Order implements EnumType {
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
    public enum Period implements EnumType {
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

    /**
     * 月份
     */
    public enum Month implements EnumType {
        Jan,
        Feb,
        Mar,
        Apr,
        May,
        Jun,
        Jul,
        Aug,
        Sep,
        Oct,
        Nov,
        Dec
    }
}
