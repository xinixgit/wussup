package wsup.service;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Delegate request to different service handlers.
 * 
 * @author xdai2
 * @since 4.9.15
 */
public class ServiceManager {
	public static final String SEARCH_SERVICE = "SearchService";
	
	public static final String USER_SERVICE = "UserService";
	
	public static final String ACTIVITY_SERVICE = "ActivityService";
	
	private Map<String, Service> serviceMap = new HashMap<>();
	
	private Service searchService;
	
	private Service userService;
	
	private Service activityService;

	public void init() {
		serviceMap.put(SEARCH_SERVICE, searchService);
		serviceMap.put(USER_SERVICE, userService);
		serviceMap.put(ACTIVITY_SERVICE, activityService);
	}

	public void handleRequest(HttpServletRequest request, HttpServletResponse response, 
					String serviceName) throws ServletException {
		serviceMap.get(serviceName).handleRequest(request, response);
	}
	
	public Service getSearchService() {
		return searchService;
	}

	public void setSearchService(Service searchService) {
		this.searchService = searchService;
	}

	public Service getUserService() {
		return userService;
	}

	public void setUserService(Service userService) {
		this.userService = userService;
	}

	public Service getActivityService() {
		return activityService;
	}

	public void setActivityService(Service activityService) {
		this.activityService = activityService;
	}
	
	
}
