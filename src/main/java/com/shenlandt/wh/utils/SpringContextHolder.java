package com.shenlandt.wh.utils;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.NoSuchMessageException;

public class SpringContextHolder implements ApplicationContextAware {     
	private static Logger log = LoggerFactory.getLogger(SpringContextHolder.class);
    
    /**   
     * 以静态变量保存ApplicationContext,可在任意代码中取出ApplicaitonContext.   
     */    
   private static ApplicationContext context;     
   
   /**   
    * 实现ApplicationContextAware接口的context注入函数, 将其存入静态变量.   
    */    
   public void setApplicationContext(ApplicationContext context) {     
       SpringContextHolder.context =context;     
   }        
   public static ApplicationContext getApplicationContext() {     
       return context;     
   }     
   /**   
    * 从静态变量ApplicationContext中取得Bean, 自动转型为所赋值对象的类型.   
    */    
   public static <T> T getBean(String name) {     
       return (T) context.getBean(name);     
   }     
   
   /**
    * 获取类型为requiredType的对象
    *
    */
   public static <T> T getBean(Class<T> clz){
       T result = (T) context.getBean(clz);
       return result;
   }
   
   /**
    * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
    *
    * @param name
    * @return boolean
    */
   public static boolean containsBean(String name) {
       return context.containsBean(name);
   }

   /**
    * 判断以给定名字注册的bean定义是一个singleton还是一个prototype。 如果与给定名字相应的bean定义没有被找到，将会抛出一个异常（NoSuchBeanDefinitionException）
    *
    * @param name
    * @return boolean
    * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
    *
    */
   public static boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
       return context.isSingleton(name);
   }

   /**
    * @param name
    * @return Class 注册对象的类型
    * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
    *
    */
   public static Class<?> getType(String name) throws NoSuchBeanDefinitionException {
       return context.getType(name);
   }

   /**
    * 如果给定的bean名字在bean定义中有别名，则返回这些别名
    *
    * @param name
    * @return
    * @throws org.springframework.beans.factory.NoSuchBeanDefinitionException
    *
    */
   public static String[] getAliases(String name) throws NoSuchBeanDefinitionException {
       return context.getAliases(name);
   }
   
   /**
    * 读取资源文件信息
    * @param key
    * @param args 占位符中的内容{1} 如果没有则传入null
    * @param 选择国际化文件 默认传入null
    * @return 没找到反回null
    */
   public static String getMessage(String key,Object[] args,Locale local){
	   try{
		  return context.getMessage(key,args,local);
	   }catch(NoSuchMessageException e){
		   System.out.println(e.getMessage());
	   }
	   return null;
   }
   
   /**
    * 读取资源文件信息
    * @param key
    * @param args 占位符中的内容{1} 如果没有则传入null
    * @return 没找到反回null
    */
   public static String getMessage(String key,Object[] args){
	   try{
		  return context.getMessage(key,args,null);
	   }catch(NoSuchMessageException e){
		   log.error("",e);
	   }
	   return null;
   }
   
   /**
    * 读取资源文件信息
    * @param key
    * @return 没找到反回null
    */
   public static String getMessage(String key){
	   try{
		  return context.getMessage(key,null,null);
	   }catch(NoSuchMessageException e){
		   log.error("",e);
	   }
	   return null;
   }
   
   /**
    * 读取资源文件信息
    * @param key
    * @param args 占位符中的内容{1} 如果没有则传入null
    * @param defaultMessage
    * @return 没找到反回defaultMessage
    */
   public static String getMessage(String key,Object[] args,String defaultMessage){
	   return context.getMessage(key,args,defaultMessage,null);
   }
   
   /**
    * 读取资源文件信息
    * @param key
    * @param defaultMessage
    * @return 没找到反回defaultMessage
    */
   public static String getMessage(String key,String defaultMessage){
	   return context.getMessage(key,null,defaultMessage,null);
   }
}    
