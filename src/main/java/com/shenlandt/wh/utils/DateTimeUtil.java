package com.shenlandt.wh.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.lang3.time.DateUtils;

/**
 * 日期时间工具类
 * @author wq
 * @version 1.0
 */
public class DateTimeUtil extends DateUtils
{
	// 以毫秒表示的时间
	private static final long DAY_IN_MILLIS = 24 * 3600 * 1000;
	private static final long HOUR_IN_MILLIS = 3600 * 1000;
	private static final long MINUTE_IN_MILLIS = 60 * 1000;
	private static final long SECOND_IN_MILLIS = 1000;
	
	private static String week[] = {
		"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"
	};
	
	/**
	 * 用指定参数抽取时间日期部分
	 * <br/>例如getDateSplit(System.currentTimeMillis()),"y") 为取得当前的年份
	 * @param date java.util.Date类型时间
	 * @param interval 可选值:
	 * <br/>y:年<br/>M:月<br/>d/dm:日<br/>dy:参数日期所代表的时间的已过的天数
	 * <br/>h:12制小时<br/>H:24制小时<br/>m:分
	 * <br/>s:秒<br/>S:毫秒<br/>w:星期
	 * <br/>wim:参数日期所代表的月已经过的星期数
	 * <br/>wm:参数日期所代表的月的星期数
	 * <br/>wy:参数日期所代表的年的第几周 
	 * @return 格式化好的时间
	 */
	public static int getDateExtract(Date date, String interval)
	{
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		if (interval.equalsIgnoreCase("y"))
			return c.get(Calendar.YEAR);
		if (interval.equals("M"))
			return c.get(Calendar.MONTH);
		if (interval.equalsIgnoreCase("d"))
			return c.get(Calendar.DATE);
		if (interval.equalsIgnoreCase("dm"))
			return c.get(Calendar.DAY_OF_MONTH);
		if (interval.equalsIgnoreCase("dy"))
			return c.get(Calendar.DAY_OF_YEAR);
		if (interval.equals("h"))
			return c.get(Calendar.HOUR);
		if (interval.equals("H"))
			return c.get(Calendar.HOUR_OF_DAY);
		if (interval.equals("m"))
			return c.get(Calendar.MINUTE);
		if (interval.equals("s"))
			return c.get(Calendar.SECOND);
		if (interval.equals("S"))
			return c.get(Calendar.MILLISECOND);
		if (interval.equalsIgnoreCase("w"))
			return c.get(Calendar.DAY_OF_WEEK);
		if (interval.equalsIgnoreCase("wim"))
			return c.get(Calendar.DAY_OF_WEEK_IN_MONTH);
		if (interval.equalsIgnoreCase("wm"))
			return c.get(Calendar.WEEK_OF_MONTH);
		if (interval.equalsIgnoreCase("wy"))
			return c.get(Calendar.WEEK_OF_YEAR);
		else
			return -1;
	}
	
	/**
	 * 用指定参数抽取时间日期部分
	 * @param strDate String类型日期时间
	 * @param format 日期时间格式
	 * @param interval 可选值:
	 * <br/>y:年<br/>M:月<br/>d/dm:日<br/>dy:参数日期所代表的时间的已过的天数
	 * <br/>h:12制小时<br/>H:24制小时<br/>m:分
	 * <br/>s:秒<br/>S:毫秒<br/>w:星期
	 * <br/>wim:参数日期所代表的月已经过的星期数
	 * <br/>wm:参数日期所代表的月的星期数
	 * <br/>wy:参数日期所代表的年的第几周 
	 * @return 格式化好的时间
	 * @throws ParseException
	 */
	public static int getDateExtract(String dateStr, String format, String interval)
		throws ParseException
	{
		SimpleDateFormat sformat = new SimpleDateFormat(format);
		Date date = sformat.parse(dateStr);
		return getDateExtract(date, interval);
	}
	
	/**
	 * @param value 时间
	 * @return yyyy-MM-dd HH:mm:ss格式的时间字符串
	 */
	public static String getSpecifyDateTime(long value)
	{
		return getFormatDateTime(new Date(value), "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 转换一个Long型到Date类型
	 * @param value 时间
	 * @return java.util.Date
	 */
	public static Date getDateTime(long value)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value);
		return c.getTime();
	}
	
	/**
	 * 取得当前星期
	 * @return 字符串
	 */
	public static String getCurrentWeek()
	{
		return week[getDateExtract(new Date(), "w") - 1];
	}

	/**
	 * 从给出的时间计算星期
	 * @param date 日期
	 * @return 字符串
	 */
	public static String getWeek(Date date)
	{
		return week[getDateExtract(date, "w") - 1];
	}

	/**
	 * 从给出的时间计算星期
	 * @param dateStr 日期字符串
	 * @param format 日期格式
	 * @return 字符串
	 */
	public static String getWeek(String dateStr, String format)
		throws ParseException
	{
		return week[getDateExtract(dateStr, format, "w") - 1];
	}

	/**
	 * 用指定格式,格式一个日期
	 * @param date
	 * @param format
	 * @return 字符串
	 */
	public static String getFormatDateTime(Date date, String format)
	{
		return (new SimpleDateFormat(format)).format(date);
	}

	/**
	 * string类型时间转为java.util.Date类型时间 yyyy-MM-dd 无时分秒
	 * @param strDate
	 * @return
	 * @throws ParseException
	 */
	public static Date changeStringToDate(String strDate)
		throws ParseException
	{
		if (strDate == null || strDate.equals(""))
			return null;
		else
			return DateFormat.getDateInstance(DateFormat.DEFAULT).parse(strDate);
	}

	/**
	 * 去得紧凑型日期 例如yyyyMMdd
	 * @param data 型如yyyy-MM-dd
	 * @return 字符串
	 */
	public static String getCompactDate(String data)
	{
		if (data == null || data.length() < 10)
		{
			return "";
		} else
		{
			data = (new StringBuilder(String.valueOf(data.substring(0, 4)))).append(data.substring(5, 7)).append(data.substring(8, 10)).toString();
			return data;
		}
	}

	/**
	 * 得到Long型时间从给定yyyy-MM-dd HH:mm:ss型字符串
	 * @param strDate
	 * @return Long型时间
	 * @throws ParseException
	 */
	public static long getLongDate(String strDate) throws ParseException
	{
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = format.parse(strDate);
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		Long l = c.getTimeInMillis();
		return l;
	}

	/**
	 * 格式化Long类型时间
	 * @param longDate Long类型时间
	 * @param format 时间格式
	 * @return java.util.Date
	 * @throws ParseException
	 */
	public static Date getFormatDate(long longDate, String format) throws ParseException
	{
		DateFormat dformat = new SimpleDateFormat(format);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(longDate);
		String dt = dformat.format(c.getTime());
		return dformat.parse(dt);
	}

	/**
	 * 格式化String类型时间
	 * @param strDate String类型时间
	 * @param format 时间格式
	 * @return java.util.Date
	 * @throws ParseException
	 */
	public static Date getFormatDate(String strDate, String format) throws ParseException
	{
		DateFormat dformat = new SimpleDateFormat(format);
		return dformat.parse(strDate);
	}

	/**
	 * 格式化Date类型时间
	 * @param date Date类型时间
	 * @param format 时间格式
	 * @return java.util.Date
	 * @throws ParseException
	 */
	public static Date getFormatDate(Date date, String format) throws ParseException
	{
		DateFormat dformat = new SimpleDateFormat(format);
		String dt = dformat.format(date);
		return dformat.parse(dt);
	}

	/**
	 * 用指定格式格式化当前时间
	 * 
	 * @param format
	 *  <br/>时间格式 例yyyy-MM-dd HH:mm:ss 
	 *  <br/>"yyyy.MM.dd G 'at' HH:mm:ss z" 2001.07.04 AD at 12:08:56 PDT 
	 *  <br/>"EEE, MMM d, ''yy" 星期五, 三月 9, '12
	 *  <br/>"h:mm a" 12:08 PM 
	 *  <br/>GGG hh:mm aaa" 02001.July.04 AD 12:08 PM 
	 *  <br/>"EEE, d MMM yyyy HH:mm:ss Z" Wed, 4 Jul 2001 12:08:56 -0700 
	 *  <br/>"yyMMddHHmmssZ" 010704120856-0700 
	 *  <br/>"yyyy-MM-dd'T'HH:mm:ss.SSSZ" 2001-07-04T12:08:56.235-0700
	 * @return 格式化的时间
	 */
	public static String getCurrentDate(String format)
	{
		return getFormatDateTime(new Date(), format);
	}
	
	/**
	 * 反回一个Long型的当前时间
	 * @return <Long> 当前时间
	 */
	public static Long getCurrentLongDate(){
		return new Date().getTime();
	}

	/**
	 * 取得yyyy-MM-dd格式的当前时间
	 * @return 格式化的时间
	 */
	public static String getCurrentDate()
	{
		return getCurrentDate("yyyy-MM-dd");
	}
	
	// ////////////////////////////////////////////////////////////////////////////
	// dateDiff
	// 计算两个日期之间的差值
	// ////////////////////////////////////////////////////////////////////////////

	/**
	 * 计算两个时间之间的差值，根据标志的不同而不同
	 * 
	 * @param flag
	 *            计算标志，表示按照年/月/日/时/分/秒等计算
	 * @param calSrc
	 *            减数
	 * @param calDes
	 *            被减数
	 * @return 两个日期之间的差值
	 */
	public static int dateDiff(char flag, Calendar calSrc, Calendar calDes) {

		long millisDiff = calSrc.getTime().getTime() - calDes.getTime().getTime();

		if (flag == 'y') {
			return (calSrc.get(Calendar.YEAR) - calDes.get(Calendar.YEAR));
		}

		if (flag == 'd') {
			return (int) (millisDiff / DAY_IN_MILLIS);
		}

		if (flag == 'h') {
			return (int) (millisDiff / HOUR_IN_MILLIS);
		}

		if (flag == 'm') {
			return (int) (millisDiff / MINUTE_IN_MILLIS);
		}

		if (flag == 's') {
			return (int) (millisDiff / SECOND_IN_MILLIS);
		}

		return 0;
	}
	/**
	 * 传入时间加上或减去指定的时间间隔
	 * @param date
	 * @param interval
	 * @return
	 */
	public static Date addDay(Date date, int interval) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, interval);
		return cal.getTime();
	}

	public static void main(String args[]) throws ParseException
	{
		System.out.println(getFormatDateTime(addDay(new Date(),-3), "yyyy-MM-dd HH:mm:ss"));
	}
}
