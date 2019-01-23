package com.shenlandt.wh.utils;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 使用FastJSON 和JAVA的POJO的相互转换
 * <p>参考及示例见 https://github.com/alibaba/fastjson</p>
 * <p>注解方式见 https://github.com/alibaba/fastjson/wiki/JSONField</p>
 * JSONHelper.java
 */
public final class JSONHelper {
	private static final Logger logger = LoggerFactory.getLogger(JSONHelper.class);
	private static final boolean prettyFormat = false;
	// 将数组转换成JSON
	public static String array2json(Object object) {
		//第二个参数指明是否格式化json
		return JSON.toJSONString(object, prettyFormat);
	}

	// 将JSON转换成数组,其中valueClz为数组中存放的对象的Class
	public static <T> List<T> json2Array(String json, Class<T> valueClz) {
		return JSON.parseArray(json, valueClz);
	}

	// 将JSON转换成数组
	public static JSONArray json2Array(String json) {
		return JSON.parseArray(json);
	}

	// 将Collection转换成JSON
	public static String list2json(Object object) {
		return JSON.toJSONString(object, prettyFormat);
	}

	// 将Map转换成JSON
	public static String map2json(Object object) {
		return JSON.toJSONString(object, prettyFormat);
	}

	// 将JSON转换成Map
	public static Map<?, ?> json2Map(String json) {
		return (Map<?, ?>) JSON.parse(json);
	}

	// 将POJO转换成JSON
	public static String bean2json(Object object) {
		return JSON.toJSONString(object, prettyFormat);
	}

	// 将JSON转换成POJO,其中beanClz为POJO的Class
	public static <T> T json2Object(String json, Class<T> beanClz) {
		return JSON.parseObject(json, beanClz);
	}

	/***
	 * 将对象序列化为JSON文本
	 * 
	 * @param object
	 * @return
	 */
	public static String toJSONString(Object object) {
		return JSON.toJSONString(object, prettyFormat);
	}

	/***
	 * 将JSON对象数组序列化为JSON文本
	 * 
	 * @param jsonArray
	 * @return
	 */
	public static String toJSONString(JSONArray jsonArray) {
		return jsonArray.toString();
	}

	/***
	 * 将JSON对象序列化为JSON文本
	 * 
	 * @param jsonObject
	 * @return
	 */
	public static String toJSONString(JSONObject jsonObject) {
		return jsonObject.toString();
	}

}
