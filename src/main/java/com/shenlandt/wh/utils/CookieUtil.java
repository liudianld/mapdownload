package com.shenlandt.wh.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * cookie?
 * @author Administrator
 *
 */
public class CookieUtil{
     
    /**
     * 获得Cookie的值 
     * @param cookieName
     * @return 
     */
    public static String getCookieValue(HttpServletRequest request,String cookieName)
    {
    	Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals(cookieName))
            {
                return cookie.getValue();
            }
        }
        return null;
    }
    
    /**
     * 删除Cookie
     * @param cookieName
     * @return
     */
    public static void removeCookie(HttpServletRequest request, HttpServletResponse response, String cookieName)
    {
    	Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals(cookieName))
            {
            	cookie.setMaxAge(0);
            	cookie.setPath("/");
            	response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * 修改Cookie 
     * @param cookieName
     * @param value
     * @param expires 失效时间.秒
     */
    public static void SetCookie(HttpServletRequest request, HttpServletResponse response, String cookieName,String value,int expires)
    {
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie : cookies)
        {
            if(cookie.getName().equals(cookieName))
            {
            	cookie.setValue(value);
            	cookie.setMaxAge(expires);
            	response.addCookie(cookie);
                break;
            }
        }
    }

    /**
     * 添加Cookie
     * @param cookieName
     * @param value
     * @param expires 失效时间.秒
     */
    public static void addCookie(HttpServletResponse response, String cookieName,  String value, int expires)
    {
        Cookie cookie = new Cookie(cookieName, value);
        cookie.setPath("/");
        cookie.setMaxAge(expires);
        response.addCookie(cookie);
    }
}
