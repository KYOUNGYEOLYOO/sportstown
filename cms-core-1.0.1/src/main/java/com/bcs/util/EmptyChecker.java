package com.bcs.util;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class EmptyChecker {
	
	
	@SuppressWarnings("rawtypes")
	public static Boolean isEmpty(Object obj)
	{
		if(obj instanceof String) return obj == null || obj.toString().trim().isEmpty();
		else if (obj instanceof List) return obj == null || ((List) obj).isEmpty();
		else if (obj instanceof Map) return obj == null || ((Map) obj).isEmpty();
		else if (obj instanceof Object[]) return obj == null || Array.getLength(obj) == 0;
		
		return obj == null;
	}
	
	public static Boolean isNotEmpty(Object obj)
	{
		return !isEmpty(obj);
	}
}
