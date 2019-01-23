package com.shenlandt.wh.utils;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

/**
 * 生成流水号工具类
 * @author Administrator
 *
 */
public class SerialNumberUtil {
	private static final int MAX_VALUE=9999;
    private static final String FORMAT = "yyMMdd";
    private static final Format DF= new SimpleDateFormat(FORMAT);
    private static final byte[] lock = new byte[0];
    private String prefix = "SN";
    private Date date = null;
    private int number=1;
    private static Map<String, SerialNumberUtil> map = new HashMap<String, SerialNumberUtil>();
    
    /**
     * 生成带指定前缀及指定日期的流水号
     * @param prefix
     * @param date
     */
    private SerialNumberUtil(String prefix,Date date){
    	if(StringUtils.isNotBlank(prefix))
    		this.prefix = prefix;
        this.date = date;
    }
    
    /**
     * 生成默认前缀及当前日期的流水号
     * @return
     */
    public static SerialNumberUtil newInstance(){
        Date date = new Date();
        return newInstance(null,date);
    }
     
    /**
     * 生成指定前缀及当前日期的流水号
     * @param prefix
     * @return
     */
    public static SerialNumberUtil newInstance(String prefix){
        Date date = new Date();
        return newInstance(prefix,date);
    }
     
    public static SerialNumberUtil newInstance(String prefix,Date date){
        SerialNumberUtil o = null;
        synchronized (lock) {
            String key = getKey(prefix, date);
            if(map.containsKey(key)){
                o = map.get(key);
                int number = o.getNumber();
                if(number<MAX_VALUE){
                    o.setNumber(number+1);
                }else {
                    o.setNumber(1);
                }
                 
            } else {
                 o = new SerialNumberUtil(prefix,date);
                 map.put(key, o);
            }
        }
        return o;
    }
     
     
     
    private static String getKey(String prefix,Date date){
        return prefix+format(date);
    }
 
    private static String format(Date date){
        return DF.format(date);
    }
     
    public String toString(){
        return  prefix+ format(date) + String.format("%04d", number);
    }
 
    public void setNumber(int number) {
        this.number = number;
    }
 
    public int getNumber() {
        return number;
    }
}
