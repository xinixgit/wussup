package wsup.utils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import def.util.ArraysUtils;

public class WebUtils {
	public static Map<String, String> getStringParams(HttpServletRequest request) {
		Map<String, String> rtnMap = new HashMap<>();
		Map<String, String[]> paramMap = request.getParameterMap();
		
		for(Map.Entry<String, String[]> entry : paramMap.entrySet()) {
			String val = ArraysUtils.isEmpty(entry.getValue()) ? null : entry.getValue()[0];
			rtnMap.put(entry.getKey(), val);
		}
		
		return rtnMap;
	}
	
	public static void ensureRequestType(HttpServletRequest request, String type) {
		if(!type.equals(request.getMethod())) {
			throw new IllegalArgumentException(type + " type of request required.");
		}
	}
}
