package com.shenlandt.wh.utils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

public class StringExtUtil {

	/**
	 * 模仿C#格式化字符串
	 * 
	 * @param str
	 * @param args
	 * @return
	 */
	public static String format(String str, String... args) {
		for (int i = 0; i < args.length; i++) {
			str = str.replaceFirst("\\{" + i + "\\}", args[i]);
		}
		return str;
	}

	/**
	 * 安全链接字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String softLink(String... strs) {
		StringBuffer sb = new StringBuffer();
		for (String s : strs) {
			sb.append(StringUtils.isEmpty(s) ? "" : s);
		}
		return sb.toString();
	}

	/**
	 * 安全去首位链接字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String softTrimLink(String... strs) {
		StringBuffer sb = new StringBuffer();
		for (String s : strs) {
			sb.append(StringUtils.trimToEmpty(s));
		}
		return sb.toString();
	}
	
	/**
	 * 判断一个字符串是否都为数字
	 * @param strNum
	 * @return
	 */
	public boolean isDigit(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/**
	 * 安全toString
	 * 
	 * @param str
	 * @return
	 */
	public static String toString(Object str) {
		if (str == null)
			return null;
		return str.toString();
	}

	/**
	 * 通过","链接List
	 * 
	 * @param strlist
	 * @return
	 */
	public static String formatByComma(Collection<String> strlist) {
		if (strlist==null || strlist.size()==0)
			return "";
		String[] strs = new String[strlist.size()];
		strlist.toArray(strs);
		return StringExtUtil.formatByComma(strs);
	}
	
	/**
	 * 通过","链接List
	 * 
	 * @param strlist
	 * @return
	 */
	public static List<String> formatByComma(String str) {
		if(StringUtils.isBlank(str))
			return Lists.newArrayList();
		String[] nidsArr = str.split(",");
		List<String> list = Arrays.asList(nidsArr);  
		return list;
	}
	
	/**
	 * 将数组轻而易举的转换成用逗号分隔的字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String formatByComma(String... strs) {
		return StringExtUtil.formatByComma(true, strs);
	}

	/**
	 * 将数组轻而易举的转换成用逗号分隔的字符串
	 * 
	 * @param strs
	 * @return
	 */
	public static String formatByComma(boolean filterNull, String... strs) {
		if (!filterNull)
			ArrayUtils.removeElement(strs, null);
		String str = ArrayUtils.toString(strs);
		return StringUtils.substring(str, 1, str.length() - 1);
	}

	/**
	 * 在数组每个元素前后追加数据
	 * 
	 * @param array
	 * @param head
	 * @param end
	 * @return
	 */
	public static String[] formatEcho(String head, String end, String... array) {
		if (array==null || array.length==0)
			return null;
		for (int i = 0; i < array.length; i++) {
			if (array[i] == null)
				continue;
			array[i] = head + array[i] + end;
		}
		return array;
	}

	/**
	 * 把用","分割的字符串，转换成数组
	 * 
	 * @param str
	 * @return
	 */
	public static String[] splitByComma(String str) {
		return StringUtils.split(str, ",");
	}
	
	/**
	 * 删除指定元素在用","分割的字符串中
	 * @param str
	 * @param element
	 * @return
	 */
	public static String removeElementByComma(String str, String element){
		if(str==null){
			return null;
		}
		String[] strArr = str.split(",");
		strArr  = ArrayUtils.removeElement(strArr, element);
		return StringExtUtil.formatByComma(strArr);
	}
	
	/**
	 * 追加元素在用","分割的字符串中
	 * @param str
	 * @param element
	 * @return
	 */
	public static String addElementByComma(String str, String element){
		if(str==null){
			return StringExtUtil.formatByComma(ArrayUtils.add(null, element));
		}
		String[] strArr = str.split(",");
		strArr  = ArrayUtils.add(strArr, element);
		return StringExtUtil.formatByComma(strArr);
	}
	

	public static void main(String[] args) {
		String[] stringArray = { "Red", "Orange", "Blue", "Brown", "Red" };
		System.out.println(StringExtUtil.formatByComma(stringArray));
		
		String str = "";
		System.out.println(StringExtUtil.removeElementByComma(str,"2"));
		
		System.out.println(StringExtUtil.addElementByComma(null,"2"));
	}
}