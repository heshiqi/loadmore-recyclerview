package com.heshiqi.widget.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTimeUtil {

    /**
     * 计算两个日期型的时间相差多少时间
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @return
     */
    public static String twoDateDistance(Date startDate, Date endDate) {

        if (startDate == null || endDate == null) {
            return null;
        }
        long timeLong = endDate.getTime() - startDate.getTime();
        if (timeLong < 60 * 1000) {
            if (timeLong < 1000) {
                return "刚刚";
            } else {
                return timeLong / 1000 + "秒前";
            }
        } else if (timeLong < 60 * 60 * 1000) {
            timeLong = timeLong / 1000 / 60;
            return timeLong + "分钟前";
        } else if (timeLong < 60 * 60 * 24 * 1000) {
            timeLong = timeLong / 60 / 60 / 1000;
            return timeLong + "小时前";
        } else if (timeLong < 60 * 60 * 24 * 1000 * 7 * 4) {
            timeLong = timeLong / 1000 / 60 / 60 / 24;
            return timeLong + "天前";
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
            return sdf.format(startDate);
        }
    }

}
