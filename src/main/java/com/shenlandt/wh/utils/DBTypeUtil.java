package com.shenlandt.wh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/*import org.springframework.orm.hibernate4.LocalSessionFactoryBean;*/

/**
 * 
 */
public class DBTypeUtil {
	private static Logger log = LoggerFactory.getLogger(DBTypeUtil.class);
	/**
	 * 获取数据库类型
	 * @return
	public static String getDBType(){
		String retStr="";
		LocalSessionFactoryBean sf = (LocalSessionFactoryBean)SpringContextHolder.getBean("&sessionFactory");
		String dbdialect = sf.getHibernateProperties().getProperty("hibernate.dialect");
		log.debug(dbdialect);
		if (dbdialect.equals("org.hibernate.dialect.MySQLDialect")) {
			retStr="mysql";
		}else if (dbdialect.contains("Oracle")) {//oracle有多个版本的方言
			retStr = "oracle";
		}else if (dbdialect.equals("org.hibernate.dialect.SQLServerDialect")) {
			retStr = "sqlserver";
		}else if (dbdialect.contains("PostgreSQL")||dbdialect.contains("Postgis")) {
			retStr = "postgresql";
		}
		return retStr;
	}
	 */
}
