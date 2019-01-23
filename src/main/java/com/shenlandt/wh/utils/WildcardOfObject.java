package com.shenlandt.wh.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WildcardOfObject {
	private final static String regex = "\\$\\((\\d+.?(\\w)*)\\)"; //匹配$(0.ggg) 或$(0)
	
	/**
	 * 使用指定的正则表达式进行匹配
	 * 在input字符串中,如果字符串中发现匹配项,则用obj对象数组中的对象进行替换.
	 * @param regex
	 * @return
	 */
	public static String format(Object[] obj ,String input, String regex){
		StringBuffer sb = new StringBuffer();
		Matcher m = Pattern.compile(regex).matcher(input);
		while (m.find()) { // 遍历匹配项
			if(obj==null || obj.length==0){
				System.out.println("指定表达式时,obj不能为空!");
				return input;
			}
			// group(0)返回整个文本，group(1)返回匹配正则表达式的文本，如果表达式中有括号，返回匹配第一对括号之间表达式的文本
			String[] temp = m.group(1).split("\\.");
			int num = Integer.parseInt(temp[0]);
			if(num>obj.length-1){
				System.out.println("下标越界错误,查看'.'前的数字(起始位置为0)是否超过了传入参数的个数! 参数个数:"+obj.length+",位置:"+num);
				return "";
			}
			
			if(m.group(2)!=null){//不为空则表示使用"."位置占位符
				ReflectHelper reflectHelper = new ReflectHelper(obj[num]);
				String anchor = temp[1];
				System.out.println(anchor);
				// 往sb追加自上个匹配后到此次匹配的文本，并将匹配项替换为对应值的文本
				m.appendReplacement(sb, reflectHelper.getMethodValue(anchor)+"");
			}else{//如果只有数字,则表示把这个数字位置对应的参数直接输出.
				m.appendReplacement(sb, obj[num].toString());
			}
		}
		m.appendTail(sb); // 往sb追加自最后一个匹配后至末尾的文本
		
		if(sb.length()==0)// 如果没有匹配到则反回input
			sb.append(input);
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	/**
     * 使用默任的regex,匹配$(0.ggg) 或$(0)
     * 在input字符串中,如果字符串中发现匹配项,则用obj对象数组中的对象进行替换.
     * 
     * @param obj
     * @param str
     * @return
     */
    public static String format(Object[] obj ,String input){
    	return format(obj, input,regex);
    }
    
    
    ////////////
	public static void main(String[] args) {
	}
}
