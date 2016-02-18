package cc.meiwen.util;

import android.text.TextUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * 日期操作类：格式化日期，计算时间差，以友好的方式显示时间等等；
 * 
 * @author Jinsk
 * @date 2014-05-28
 * @time 15:01:23
 * */
public class MnDateUtil {

	/** 格式化时间：年-月-日  时：分：秒 */
	public static String dateFormatYMDHMS = "yyyy-MM-dd HH:mm:ss";
	
	/** 格式化时间：年-月-日 时：分 */
	public static String dateFormatYMDHM = "yyyy-MM-dd HH:mm";
	
	/** 格式化时间：年-月-日 */
	public static String dateFormatYMD = "yyyy-MM-dd";
	
	/** 格式化时间：时：分：秒 */
	public static String dateFormatHMS = "HH:mm:ss";
	
	/** 格式化时间：年-月 */
	public static String dateFormatYM = "yyyy-MM";
	
	/** 格式化时间：时：分 */
	public static String dateFormatHM = "HH:mm";
	
	/** 格式化时间：年-月-日-时-分-秒，一般用于设置文件名 */
	public static String dateFormatFileName = "yyyy-MM-dd-HH-mm-ss";
	
	
	/**
	 * 将date类型的日期转换为String
	 * 
	 * @param date 需要的转换的日期
	 * @param format 日期格式，例如：yyyy-MM-dd HH:mm:ss
	 * @return 格式化之后的字符串时间
	 * */
	public static String stringByFormat(Date date, String format){
		String strDate = null;
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try{
			strDate = sdf.format(date);
		} catch (Exception e){
			e.printStackTrace();
		}
		return strDate;
	}
	
	/**
	 * 计算两个时间相差的天数
	 * 
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 两个时间的天数差
	 * */
	public static int offsetDay(long date1, long date2){
		int day = 0;
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		//获取年份和日期
		int y1 = calendar1.get(Calendar.YEAR);
		int d1 = calendar1.get(Calendar.DAY_OF_YEAR);
		int y2 = calendar2.get(Calendar.YEAR);
		int d2 = calendar2.get(Calendar.DAY_OF_YEAR);
		
		int maxDay = 0;
		if(y1 - y2 > 0){
			maxDay = calendar2.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 + maxDay;
		} else if (y1 - y2 < 0){
			maxDay = calendar1.getActualMaximum(Calendar.DAY_OF_YEAR);
			day = d1 - d2 - maxDay;
		} else {
			day = d1 - d2;
		}
		
		return day;
	}
	
	/**
	 * 描述：获取指定日期时间的字符串,用于导出想要的格式.
	 *
	 * @param strDate String形式的日期时间，必须为yyyy-MM-dd HH:mm:ss格式
	 * @param format 输出格式化字符串，如："yyyy-MM-dd HH:mm:ss"
	 * @return String 转换后的String类型的日期时间
	 */
	public static String stringByFormat(String strDate, String format) {
		String mDateTime = null;
		try {
			Calendar c = new GregorianCalendar();
			SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat(dateFormatYMDHMS);
			c.setTime(mSimpleDateFormat.parse(strDate));
			SimpleDateFormat mSimpleDateFormat2 = new SimpleDateFormat(format);
			mDateTime = mSimpleDateFormat2.format(c.getTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mDateTime;
	}
	
	
	/**
	 * 描述：计算两个日期所差的小时数.
	 *
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 所差的小时数
	 */
	public static int offsetHour(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int h1 = calendar1.get(Calendar.HOUR_OF_DAY);
		int h2 = calendar2.get(Calendar.HOUR_OF_DAY);
		int h = 0;
		int day = offsetDay(date1, date2);
		h = h1-h2+day*24;
		return h;
	}
	
	/**
	 * 描述：计算两个日期所差的分钟数.
	 *
	 * @param date1 第一个时间的毫秒表示
	 * @param date2 第二个时间的毫秒表示
	 * @return int 所差的分钟数
	 */
	public static int offsetMinutes(long date1, long date2) {
		Calendar calendar1 = Calendar.getInstance();
		calendar1.setTimeInMillis(date1);
		Calendar calendar2 = Calendar.getInstance();
		calendar2.setTimeInMillis(date2);
		int m1 = calendar1.get(Calendar.MINUTE);
		int m2 = calendar2.get(Calendar.MINUTE);
		int h = offsetHour(date1, date2);
		int m = 0;
		m = m1-m2+h*60;
		return m;
	}
	
	
	/**
	 * 描述：根据时间返回格式化后的时间的描述.<br />
	 * 小于1小时显示多少分钟前  大于1小时显示今天＋实际日期，大于今天全部显示实际时间
	 *
	 * @param strDate the str date
	 * @param outFormat the out format
	 * @return the string
	 */
	public static String friendlyTime(String strDate,String outFormat) {
		
		DateFormat df = new SimpleDateFormat(dateFormatYMDHMS);
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		try {
			c2.setTime(df.parse(strDate));
			c1.setTime(new Date());
			int d = offsetDay(c1.getTimeInMillis(), c2.getTimeInMillis());
			if(d==0){
				int h = offsetHour(c1.getTimeInMillis(), c2.getTimeInMillis());
				if(h>0){
					return "今天"+stringByFormat(strDate,dateFormatHM);
					//return h + "小时前";
				}else if(h<0){
					//return Math.abs(h) + "小时后";
				}else if(h==0){
					int m = offsetMinutes(c1.getTimeInMillis(), c2.getTimeInMillis());
					if(m>0){
						return m + "分钟前";
					}else if(m<0){
						//return Math.abs(m) + "分钟后";
					}else{
						return "刚刚";
					}
				}
			}else if(d>0){
				if(d == 1){
					//return "昨天"+getStringByFormat(strDate,outFormat);
				}else if(d==2){
					//return "前天"+getStringByFormat(strDate,outFormat);
				}
			}else if(d<0){
				if(d == -1){
					//return "明天"+getStringByFormat(strDate,outFormat);
				}else if(d== -2){
					//return "后天"+getStringByFormat(strDate,outFormat);
				}else{
				    //return Math.abs(d) + "天后"+getStringByFormat(strDate,outFormat);
				}
			}
			
			String out = stringByFormat(strDate,outFormat);
			if(!TextUtils.isEmpty(out)){
				return out;
			}
		} catch (Exception e) {
		}
		
		return strDate;
	}
	
	
	
	
}
