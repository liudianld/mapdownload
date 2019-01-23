package com.shenlandt.wh.utils;

import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;

/**
 * 
 * @description 得到当前应用的系统路径
 */
public class SystemPath {

	public static String getSysPath()
	{
		String path= Thread.currentThread().getContextClassLoader().getResource("").toString();
		String temp=path.replaceFirst("file:/", "").replaceFirst("WEB-INF/classes/", "");
		String separator= System.getProperty("file.separator");
		String resultPath=temp.replaceAll("/", separator+separator);
		return resultPath;
	}
	public static String getClassPath(String resourceName)
	{
		String path= Thread.currentThread().getContextClassLoader().getResource(resourceName).toString();
		String temp=path.replaceFirst("file:/", "");
		String separator= System.getProperty("file.separator");
		String resultPath=temp.replaceAll("/", separator+separator);
		return resultPath;
	}
	public static String getSystempPath()
	{
		return System.getProperty("java.io.tmpdir");
	}
	public static String getSeparator()
	{
		return System.getProperty("file.separator");
	}
	
	public static String getResourcePath(String resourceName){
	    if (resourceName == null) {
            return null;
        }
	    
	    return SystemPath.class.getClassLoader().getResource(resourceName).getPath();
	}
	
	public static Path getFileAbsoultePath(String path) {
        if (path == null) {
            return null;
        }
        return FileSystems.getDefault().getPath(path).toAbsolutePath();
    }
	
	public static InputStream getStream(String resourceName){
        if (resourceName == null) {
            return null;
        }
        InputStream stream = ResourceUtil.class.getClassLoader().getResourceAsStream(resourceName);  
        return stream;
    }
	
	public static void main(String[] args){
		System.out.println(getSysPath());
		System.out.println(System.getProperty("java.io.tmpdir"));
		System.out.println(getSeparator());
		System.out.println(getClassPath(""));
	}
}
