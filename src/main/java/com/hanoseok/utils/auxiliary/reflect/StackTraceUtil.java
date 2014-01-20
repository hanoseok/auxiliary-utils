package com.hanoseok.utils.auxiliary.reflect;

import java.lang.reflect.Method;

public class StackTraceUtil {
	
	public final static int ALL_LINE = Integer.MAX_VALUE;
	
	public static String getCaller() {
		StringBuilder sb = new StringBuilder();

		try{
			StackTraceElement[] s = Thread.currentThread().getStackTrace();
			for(int i = 0 ; i < s.length ; i ++ ) {
				if("getCaller".equals(s[i].getMethodName())){
					sb.append(s[i+2].getClassName()).append(":").append(s[i+1].getMethodName()).append(" [").append(s[i+2].getLineNumber()).append("]");
					break;
				}
			}	
		} catch (Exception e){}
		
		return sb.toString();
	}
	
	public static Method getCallingMethodList(Class<?> parameterTypes) {
		try{
			StackTraceElement[] s = Thread.currentThread().getStackTrace();
			for(int i = 0 ; i < s.length ; i ++ ) {
				if("getCaller".equals(s[i].getMethodName())){
					return Class.forName(s[i+2].getClassName()).getDeclaredMethod(s[i+1].getMethodName(), parameterTypes);
				}
			}
		} catch (Exception e){}
		return null;
	}
	
	
	public static String getStackTrace(int line) {
		return getStackTrace(line, 1);
	}
	
	
	public static String getStackTrace(int line, int skipDepth) {
		StringBuilder sb = new StringBuilder();
		try{
			StackTraceElement[] s = Thread.currentThread().getStackTrace();
			
			int depth = 2 + skipDepth;
			int c = s.length;
			
			if( line + depth < s.length ){
				c = line + depth;
			}
			
			for(int i = depth ; i < c ; i ++ ) {
					sb.append(s[i].getClassName()).append(":" + s[i].getMethodName()).append(" [").append(s[i].getLineNumber()).append("]").append("\n");
			}
		} catch (Exception e){}
		return sb.toString();
	}
	

}
