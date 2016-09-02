package wsup.web;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import def.util.ArraysUtils;

public class RequestHolder {

	@Autowired
	private HttpServletRequest request;
	
	Map<String, String[]> paramMap;
	
	public void init() {
		paramMap = request.getParameterMap();
	}
	
	public String getParam(String key) {
		String[] params = paramMap.get(key);
		return (ArraysUtils.isEmpty(params) ? "" : params[0]);
	}
}
