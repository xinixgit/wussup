package wsup.service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for service.
 *
 * @author xdai2
 * @since Apr 10, 2015
 *
 */
public interface Service {
	
	/**
	 * Different services should handle request differently.
	 * 
	 * @param request
	 * @param response
	 * @throws ServletException
	 */
	public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException;	
}
