package com.hanoseok.utils.auxiliary.reflect;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * 
 * @author oshan
 *
 */
public class ReflectionUtil {
	
	private static ThreadLocal<List<Object>> stack = new ThreadLocal<List<Object>>();

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static final List LEAVES = Arrays.asList(
	        Boolean.class, Character.class, Byte.class, Short.class,
	        Integer.class, Long.class, Float.class, Double.class, Void.class,
	        String.class);
	

	/**
	 * 객체 정보를 Recursive 하게 reflection 한다.
	 * 큰 객체는 성능저하를 가지고 올 수 있으며,
	 * StackOverFlow 가 발생할 수 있을 가능성이 있으니,
	 * 주의해서 사용해야 한다. 
	 * @param o
	 * @return
	 */
	public static String toStringRecursive(Object o) {
		try{
	    	List<Object> stackList = new ArrayList<Object>();
	    	stack.set(stackList);
	    	return reflect(o);
		}finally{
			stack.remove();
		}
	}
	
	/**
	 * RecursiveReflection 을 이용하여 인스턴스가 다른 객체가 실제로 같은 값을 가지는지 비교한다.
	 * Depth가 큰 객체는 100% 믿을 수 없지만 어느정도 크기의 객체는 필드에 같은 값들이 세팅되어 있는지 확인이 가능하다.
	 * 성능저하 및 StackOverFlow 발생가능성이 있으니 주의하여 사용해야 한다.
	 * @param o1
	 * @param o2
	 * @return
	 */
	public static boolean isEqualsValue(Object o1, Object o2) {
		return toStringRecursive(o1).equals(toStringRecursive(o2));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static String reflect(Object o){
		try{
		    if (o == null)
		        return "<null>";
		   
		    if (LEAVES.contains(o.getClass()))
		    	return o.toString();
		    
		    StringBuilder sb = new StringBuilder();
		    if (o instanceof Map) {
		    	List keyList = new ArrayList(Arrays.asList(((Map) o).keySet().toArray()));
		    	Collections.sort(keyList);
		    
		    	for (Object key : keyList) {
		    		 sb.append(reflect(key) + ":" + reflect(((Map) o).get(key))).append(",");
				}
		    	if(sb.lastIndexOf(",") > 0){
		    		sb.delete(sb.lastIndexOf(","), sb.length());
		    	}
		    	return sb.toString();
		    }else if(o instanceof Collection) {
		    	if(o instanceof Comparable){
			    	for (Object o2 : new TreeSet<Object>((Collection<Object>) o)) {
			    		 sb.append(reflect(o2)).append(",");
					}
		    	}else{
		    		for (Object o2 : (Collection<Object>) o) {
		    			sb.append(reflect(o2)).append(",");
		    		}
		    	}
		    	if(sb.lastIndexOf(",") > 0){
		    		sb.delete(sb.lastIndexOf(","), sb.length());
		    	}
		    	return sb.toString();
		    }else if(o instanceof Runnable) {
		    	return o.toString();
		    }else{
		    	if(!stack.get().contains(o.hashCode())){	// StackOverFlow 방지 
		    		stack.get().add(o.hashCode());
				    sb.append(o.getClass().getSimpleName()).append("[");
				    List<Field> fl = new ArrayList<Field>(Arrays.asList(o.getClass().getDeclaredFields()));
				    if(fl != null && !fl.isEmpty()){
					    for (Field f : fl) {
					        if (Modifier.isStatic(f.getModifiers()))
					            continue;
					        f.setAccessible(true);
								sb.append(f.getName()).append(":");
								try {
									sb.append(reflect(f.get(o)));
								} catch (Exception e) {
									sb.append(e.getMessage());
								}
								sb.append(",");
					    }
					    if(sb.lastIndexOf(",") > 0){
				    		sb.delete(sb.lastIndexOf(","), sb.length());
				    	}
					    sb.append("]");
				    }
				    return sb.toString();
		    	}else{
		    		return sb.append(o.getClass().getSimpleName()).append("[Maybe StackOverFlow]").toString();
		    	}
		    }
		}catch(Exception e){
			return e.getMessage();
		}
	}
	
}

