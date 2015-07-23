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
        Dec;

        /**
         * 根据索引生成{Month}类型对象
         * @param ordinal {Month} 枚举类型索引
         * @return {Month}枚举类型对象
         */
        public static Month build(int ordinal) {
            Month month = Jan;

            switch (ordinal) {
                case 0 :
                    month = Jan;
                    break;
                case 1 :
                    month = Feb;
                    break;
                case 2 :
                    month = Mar;
                    break;
                case 3 :
                    month = Apr;
                    break;
                case 4 :
                    month = May;
                    break;
                case 5 :
                    month = Jun;
                    break;
                case 6 :
                    month = Jul;
                    break;
                case 7 :
                    month = Aug;
                    break;
                case 8 :
                    month = Sep;
                    break;
                case 9 :
                    month = Oct;
                    break;
                case 10 :
                    month = Nov;
                    break;
                case 11 :
                    month = Dec;
                    break;
            }
            return month;
        }
    }
}
