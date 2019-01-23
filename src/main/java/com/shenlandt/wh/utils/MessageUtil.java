package com.shenlandt.wh.utils;

import org.springframework.context.MessageSource;

/**
 * 取i18n 资源文件中的信息
 * @see SpringContextHolder 
 */
public class MessageUtil {

    private static MessageSource messageSource;

    /**
     * 根据消息键和参数 获取消息
     * 委托给spring messageSource
     *
     * @param code 消息键
     * @param args 参数
     * @return
     */
    public static String message(String message, Object... args) {
        if (messageSource == null) {
            messageSource = SpringUtils.getBean(MessageSource.class);
        }
        return messageSource.getMessage(message, args, null);
    }

}
