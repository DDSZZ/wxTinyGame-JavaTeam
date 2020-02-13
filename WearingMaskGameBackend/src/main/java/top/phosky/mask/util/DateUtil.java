package top.phosky.mask.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/* @author Phosky, NEU
 * @version 2.2
 */
public class DateUtil {
    public final static long A_DAY_LENGTH = 24 * 60 * 60 * 1000;
    public static final SimpleDateFormat SDF_yyyyMMdd = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_HHmmssSSS_0 = new SimpleDateFormat("HH:mm:ss:SSS");
    public static final SimpleDateFormat SDF_HHmmssSSS_1 = new SimpleDateFormat("HHmmssSSS");
    public static final SimpleDateFormat SDF_MMddHHmmss = new SimpleDateFormat("MMddHHmmss");
    public static final SimpleDateFormat SDF_yyyyMMddHHmmssSSS_0 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");
    public static final SimpleDateFormat SDF_yyyyMMddHHmmssSSS_1 = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    private static DateUtil singleton;

    private DateUtil() {
    }

    //单例模式
    public static DateUtil getSingleton() {
        if (singleton == null) {
            singleton = new DateUtil();
        }
        return singleton;
    }

    // 得到现在时间的字符串
    public String getInstanceDate(SimpleDateFormat sdf) {
        Calendar c = Calendar.getInstance();
        return sdf.format(c.getTime());
    }

    // 通过年份得到总天数
    public Integer getDaysByYear(Integer year) {
        if ((year % 4 == 0 && year % 400 != 0) || (year % 100 == 0 && year % 400 == 0)) {
            return 366;
        } else {
            return 365;
        }
    }

    // 通过月份和年份得到该月的天数
    public Integer getDaysByMonth(Integer month, Integer year) {
        switch (month) {
            case 1:
            case 5:
            case 3:
            case 7:
            case 8:
            case 10:
            case 12:
                return 31;
            case 2:
                if (getDaysByYear(year) == 365) {
                    return 28;
                } else {
                    return 29;
                }
            case 4:
            case 6:
            case 9:
            case 11:
            default:
                return 30;
        }
    }

    // 得到以前到现在相距的周数
    public Integer getDistanceWeeks(Date date) {
        return getDistanceDays(date) / 7;
    }

    // 得到以前到现在相距的天数
    public Integer getDistanceDays(Date date) {
        long between = getDistanceMillions(date);
        return Integer.parseInt(String.valueOf(between / A_DAY_LENGTH));
    }

    // 得到以前到现在相距的毫秒数
    public long getDistanceMillions(Date date) {
        Calendar c = Calendar.getInstance();
        Calendar c1 = Calendar.getInstance();
        c1.setTime(date);
        long between = c.getTimeInMillis() - c1.getTimeInMillis();
        return between;
    }

    // 通过日期得到星期几，星期天是1，接着2~7
    public Integer getDayInWeekByDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        Integer day = c.get(Calendar.DAY_OF_WEEK);
        day = (day == 1) ? 7 : day - 1;
        return day;
    }

    //检验一天是否符合实际情况
    //正确返回1，否则返回负数。年不能为负数：-1，月必须是0~12的整数：-2，日超出边界：-3
    public int justicCorrectDay(int year, int month, int day) {
        if (year < 0) {
            return -1;
        }
        if (month <= 0 || month > 12) {
            return -2;
        }
        if (day <= 0 || day > getDaysByMonth(month, year)) {
            return -3;
        }
        return 1;
    }
}
