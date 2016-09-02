package wsup.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import wsup.service.ServiceManager;

/**
 * Handles all authentication requests.
 *
 * @author xdai2
 * @since Apr 28, 2015
 *
 */
@Controller
@RequestMapping("/secure")
public class SecureController {
	@Autowired
	private ServiceManager serviceManager;

	@RequestMapping("/user")
	public void handleUserRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		serviceManager.handleRequest(request, response, ServiceManager.USER_SERVICE);
	}
	
	@RequestMapping("/activity")
	public void handleActivityRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		serviceManager.handleRequest(request, response, ServiceManager.ACTIVITY_SERVICE);
	}
	
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
}
