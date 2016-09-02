package wsup.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import def.util.JsonsUtils;
import wsup.task.TaskResult;

/**
 * Build a simple json response, and write it to response
 * when controller's finished with business.
 *
 * @author xdai2
 * @since May 2, 2015
 *
 */
public class MapResponse implements TaskResult {

	private Map<String, Object> respMap = new HashMap<>();
	
	public void setError(Object error) {
		setError(error, true);
	}
	
	/**
	 * For security sometimes, it's best to contain only error.
	 * in the response.
	 */
	public void setError(Object error, boolean clearData) {
		respMap.clear();
		respMap.put(JsonField.ERROR, error);
	}
	
	public <T> Map<String, T> getAsMap(String key, Class<T> typeClass) {
		return (Map<String, T>)respMap.get(key);
	}
	
	public <T> List<T> getAsList(String key, Class<T> typeClass) {
		return (List<T>)respMap.get(key);
	}
	
	public String getAsString(String key) {
		return (String)respMap.get(key);
	}
	
	public MapResponse add(String key, Object obj) {
		respMap.put(key, obj);
		return this;
	}
	
	@Override
	public boolean isEmpty() {
		return respMap.isEmpty();
	}

	@Override
	public Map<String, Object> toMap() {
		return respMap;
	}
	
	@Override
	public String toString() {
		return JsonsUtils.encode2Json(respMap);
	}
}
