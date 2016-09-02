package wsup.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import wsup.service.ServiceManager;

/**
 * Handles all requests related to user's information.
 * E.g. user just added new word.
 *
 * @author xdai2
 * @since Apr 28, 2015
 *
 */
@Controller
@RequestMapping(value="/user")
public class UserController {
	@Autowired
	private ServiceManager serviceManager;
	
	@RequestMapping()
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		serviceManager.handleRequest(request, response, ServiceManager.USER_SERVICE);
	}
	
	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
}
