package com.shenlandt.wh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

/**
 * 专门加载yaml配置文件
 * 
 * <p>Version: 1.0
 */
public class YamlLoader {
    private static final Logger LOGGER = LoggerFactory.getLogger(YamlLoader.class);
    
    public static Map<String, Object> getConfiguration(final String confFilePath) throws Exception {
        try {
            FileInputStream fis = new FileInputStream(new File(confFilePath));
            return getConfiguration(fis);
        }catch (FileNotFoundException fne) {
            throw new Exception("配置文件不存在", fne);
        } 
    }
    
    public static Map<String, Object> getConfiguration(final Path confFilePath) throws Exception {
        try {
            FileInputStream fis = new FileInputStream(confFilePath.toFile());
            return getConfiguration(fis);
        }catch (FileNotFoundException fne) {
            throw new Exception("配置文件不存在", fne);
        } 
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getConfiguration(final InputStream stream) throws Exception {
        Yaml yaml = new Yaml();

        Map<String, Object> conf = null;

        try {
            conf = (Map<String, Object>) yaml.load(stream);
        } catch (Throwable t) {
            throw new Exception("解析配置文件时发生错误", t);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException ioe) {
                    throw new Exception("不能关闭 InputStream",ioe);
                }
            }
        }
        return conf;
    } 
    
    @SuppressWarnings("unchecked")
	public static List<Map<String, Object>> getAsListOfMaps(final Map<String, Object> conf, final String key, final List<Map<String, Object>> defaultValue) {
        if (conf == null) {
            LOGGER.debug("在配置文件中没有指定参数组 {}. 使用它的默认值 {}", key, defaultValue);
            return defaultValue;
        }

        Object o = conf.get(key);

        if (o instanceof List) {
            return (List<Map<String, Object>>) o;
        } else {
            LOGGER.debug("在配置文件中没有指定参数组 {}. 使用它的默认值 {}", key, defaultValue);
            return defaultValue;
        }
    }
    
    @SuppressWarnings("unchecked")
	public static Map<String, Object> getAsMap(final Map<String, Object> conf, final String key) {
        if (conf == null) {
            LOGGER.debug("在配置文件中没有指定参数组 {}.", key);
            return null;
        }

        Object o = conf.get(key);

        if (o instanceof Map) {
            return (Map<String, Object>) o;
        } else {
            LOGGER.debug("在配置文件中没有指定参数组 {}.", key);
            return null;
        }
    }
    
    public static Boolean getAsBooleanOrDefault(final Map<String, Object> conf, final String key, final Boolean defaultValue) {
        if (conf == null) {
            LOGGER.debug("试图从一个空的配置Map中得到参数 {}. 使用它的默认值 {}", key, defaultValue);
            return defaultValue;
        }

        Object o = conf.get(key);

        if (o == null) {
            if (defaultValue) {
                LOGGER.debug("在配置文件中没有指定参数 {}. 使用它的默认值 {}", key, defaultValue);
            }
            return defaultValue;
        } else if (o instanceof Boolean) {
            LOGGER.debug("参数 {} 设置为 {}", key, o);
            return (Boolean) o;
        } else {
            LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, o, defaultValue);
            return defaultValue;
        }
    }

    public static String getAsStringOrDefault(final Map<String, Object> conf, final String key, final String defaultValue) {

        if (conf == null || conf.get(key) == null) {
            if (defaultValue != null ) {
                LOGGER.debug("在配置文件中没有指定参数 {}. 使用它的默认值 {}", key, defaultValue);
            }
            return defaultValue;
        } else if (conf.get(key) instanceof String) {
            LOGGER.debug("参数 {} 设置为 {}", key, conf.get(key));
            return (String) conf.get(key);
        } else {
            LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
            return defaultValue;
        }
    }

    public static Integer getAsIntegerOrDefault(final Map<String, Object> conf, final String key, final Integer defaultValue) {
        if (conf == null || conf.get(key) == null) {
            if (defaultValue != null) {
                LOGGER.debug("在配置文件中没有指定参数 {}. 使用它的默认值 {}", key, defaultValue);
            }
            return defaultValue;
        } else if (conf.get(key) instanceof Integer) {
            LOGGER.debug("参数 {} 设置为 {}", key, conf.get(key));
            return (Integer) conf.get(key);
        } else {
            LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
            return defaultValue;
        }
    }

    public static Long getAsLongOrDefault(final Map<String, Object> conf, final String key, final Long defaultValue) {
        if (conf == null || conf.get(key) == null) {
            if (defaultValue != null) {
                LOGGER.debug("在配置文件中没有指定参数 {}. 使用它的默认值 {}", key, defaultValue);
            }
            return defaultValue;
        } else if (conf.get(key) instanceof Number) {
            LOGGER.debug("参数 {} 设置为 {}", key, conf.get(key));
            try {
                return Long.parseLong(conf.get(key).toString());
            } catch (NumberFormatException nfe) {
                LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
                return defaultValue;
            }
        } else {
            LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
            return defaultValue;
        }
    }

    public static int[] getAsArrayOfInts(final Map<String, Object> conf, final String key, final int[] defaultValue) {
        if (conf == null || conf.get(key) == null) {
            if (defaultValue != null) {
                LOGGER.debug("在配置文件中没有指定参数 {}. 使用它的默认值 {}", key, defaultValue);
            }
            return defaultValue;
        } else if (conf.get(key) instanceof List) {
            LOGGER.debug("参数 {} 设置为 {}", key, conf.get(key));

            int ret[] = convertListToIntArray((List<?>) conf.get(key));

            if (ret.length == 0) {
                LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
                return defaultValue;
            } else {
                return ret;
            }
        } else {
            LOGGER.warn("参数值错误 {}: {}. 使用它的默认值  {}", key, conf.get(key), defaultValue);
            return defaultValue;
        }
    }
    
    public static int[] convertListToIntArray(List<?> integers) {
        int[] ret = new int[integers.size()];
        Iterator<?> iterator = integers.iterator();
        for (int i = 0; i < ret.length; i++) {
            Object o = iterator.next();

            if (o instanceof Integer) {
                ret[i] = (Integer) o;
            } else {
                return new int[0];
            }
        }
        return ret;
    }
}
