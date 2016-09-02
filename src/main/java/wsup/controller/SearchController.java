package wsup.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import wsup.service.ServiceManager;

/**
 * Controller for "/search", simple as that.
 *
 * @author xdai2
 * @since Apr 10, 2015
 *
 */
@Controller
@RequestMapping(value="/search")
public class SearchController {

	@Autowired
	private ServiceManager serviceManager;
	
	@RequestMapping(method=RequestMethod.GET)
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException {
		serviceManager.handleRequest(request, response, ServiceManager.SEARCH_SERVICE);
	}

	public ServiceManager getServiceManager() {
		return serviceManager;
	}

	public void setServiceManager(ServiceManager serviceManager) {
		this.serviceManager = serviceManager;
	}
}
