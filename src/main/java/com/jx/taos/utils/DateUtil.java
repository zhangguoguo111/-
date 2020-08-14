package com.jx.taos.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * 日期操作工具类
 *
 * @author yusijia 2020.04.21
 */
public class DateUtil extends DateUtils {
	private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
	public final static String PATTERN_DEFAULT = "yyyy-MM-dd HH:mm:ss";

	public final static String PATTERN_HH_MM = "yyyy-MM-dd HH:mm";

	public static Calendar date2Calender(Date date){
		if(date==null) {
            return null;
        }
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

    /**
     * string转Calendar方法
     * @param time
     * @param pattern
     * @param lenient   是否严格校验
     * @return
     */
	public static Calendar string2Calendar(String time, String pattern, boolean lenient) {
		Calendar c = null;
		if (StringUtils.isBlank(time)) {
			return c;
		}
//		log.debug("time:" + time + ",pattern:" + pattern);
		try {
			Date date;
			if(lenient) {
                date = parseDate(time, new String[]{pattern});
            }else{
                date = parseDateLenient(time, new String[]{pattern});
            }
			c = date2Calender(date);
		} catch (Exception e) {
			log.error("DateUtils::string2Calendar::时间转换异常:" + e.getMessage());
		}
		return c;
	}

    private static Date parseDateLenient(String str, String[] parsePatterns) throws ParseException {
        if (str == null || parsePatterns == null) {
            throw new IllegalArgumentException("Date and Patterns must not be null");
        }

        SimpleDateFormat parser = null;
        ParsePosition pos = new ParsePosition(0);
        for (int i = 0; i < parsePatterns.length; i++) {
            if (i == 0) {
                parser = new SimpleDateFormat(parsePatterns[0]);
                parser.setLenient(false);
            } else {
                parser.applyPattern(parsePatterns[i]);
            }
            pos.setIndex(0);
            Date date = parser.parse(str, pos);
            if (date != null && pos.getIndex() == str.length()) {
                return date;
            }
        }
        throw new ParseException("Unable to parse the date: " + str, -1);
    }

    public static Calendar string2Calendar(String time) {

		return string2Calendar(time, PATTERN_DEFAULT);
	}

    public static Calendar string2Calendar(String time, String pattern) {

        return string2Calendar(time, pattern, true);
    }


	public static String calendar2String(Calendar c, String pattern) {
		if (c == null) {
            return null;
        }
		try {
			Date date = c.getTime();
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			log.error("DateUtils::calendar2String::时间转换格式异常", e);
		}
		return null;
	}

	public static String long2String(Long c, String pattern) {
		if (c == null) {
            return null;
        }
		try {
			Date date = new Date(c);
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.format(date);
		} catch (Exception e) {
			log.error("DateUtils::calendar2String::时间转换格式异常", e);
		}
		return null;
	}

	public static String long2String(Long c) {
		return long2String(c,PATTERN_DEFAULT);
	}

	public static String calendar2String(Calendar c) {

		return calendar2String(c, PATTERN_DEFAULT);
	}

	public static String getCurrentTimeStr(){
		return long2String(System.currentTimeMillis(), PATTERN_DEFAULT);
	}
	
	/**
	 * 
	* @Description: 获取目标时间
	* @param field Calendar自带的年月日时等常量值
	* @param amount 正值加负值减
	* @return 
	* Calendar
	 */
	public static Calendar getTargetTime(int field, int amount){
		Calendar time= Calendar.getInstance();
		time.add(field, amount);
		return time;
	}
	public static Calendar getTargetTime(Calendar time, int field, int amount){
		time.add(field, amount);
		return time;
	}

	public static Calendar getTargetTime(long time, int field, int amount){
		Calendar calendar= Calendar.getInstance();
		calendar.setTimeInMillis(time);
		calendar.add(field,amount);
		return calendar;
	}

	/**
	 * 比较两个string类型的日期先后
	 *
	 * @param dateStr1 日期1
	 * @param dateStr2 日期2
	 * @return 日期1早于日期2则返回true
	 * boolean
	 * */
	public static boolean compareBefore(String dateStr1, String dateStr2){
		//todo
		return false;
	}

	/**
	 * 获取两个时间点之间的时间间隔，单位毫秒
	 *
	 * @param beginTime 开始时间
	 * @param endTime 结束时间
	 * @return 毫秒数
	 */
	public static Long getInterval(String beginTime, String endTime){
		if(StringUtils.isBlank(beginTime) || StringUtils.isBlank(endTime)){
			throw new RuntimeException("beginTime or endTime must not be blank.");
		}
		Calendar begin = string2Calendar(beginTime);
		Calendar end = string2Calendar(endTime);

		return end.getTimeInMillis() - begin.getTimeInMillis();
	}

}
