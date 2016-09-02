package wsup.interceptor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import def.util.StringsUtils;
import wsup.db.user.User;
import wsup.db.user.UserDBHandler;
import wsup.utils.WebUtils;
import wsup.web.JsonField;
import wsup.web.MapResponse;

public class AuthenticationInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private UserDBHandler userDBHandler;

	public UserDBHandler getUserDbHandler() {
		return userDBHandler;
	}

	public void setUserDbHandler(UserDBHandler userDBHandler) {
		this.userDBHandler = userDBHandler;
	}
	
	/**
	 * Checks the secure_token against what's in DB.
	 */
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		
		Map<String, String> params = WebUtils.getStringParams(request);
		boolean error = false;
		
		String username = params.get(JsonField.USERNAME);
		String secureToken = params.get(JsonField.SECURE_TOKEN);
		
		if(StringsUtils.isAnyEmpty(username, secureToken)) {
			error = true;
		}
		
		else {
			User user = userDBHandler.select(username);
			if(user == null || StringsUtils.isEmpty(user.getSecureToken()) ||
					!user.getSecureToken().equals(secureToken)) {
				error = true;
			}
		}
		
		if(error) {
			MapResponse resp = new MapResponse();
			resp.setError("Access denied.");
			response.getWriter().print(resp);
		}
		
		return !error;
	}
	
}
