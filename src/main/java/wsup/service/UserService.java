package wsup.service;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;

import def.util.RandomStringGenerator;
import def.util.StringsUtils;
import wsup.db.user.User;
import wsup.db.user.UserDBHandler;
import wsup.utils.WebUtils;
import wsup.web.JsonField;
import wsup.web.JsonField.ActionJsonField;
import wsup.web.MapResponse;

public class UserService implements Service {
	private static final Logger LOG = LogManager.getLogger(UserService.class);
	
	private UserDBHandler userDBHandler;
	
	@Autowired
	private StandardPBEStringEncryptor strongEncryptor;

	public UserDBHandler getUserDBHandler() {
		return userDBHandler;
	}

	public void setUserDBHandler(UserDBHandler userDBHandler) {
		this.userDBHandler = userDBHandler;
	}

	public StandardPBEStringEncryptor getStrongEncryptor() {
		return strongEncryptor;
	}

	public void setStrongEncryptor(StandardPBEStringEncryptor strongEncryptor) {
		this.strongEncryptor = strongEncryptor;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {		
		Map<String, String> params = WebUtils.getStringParams(request);
		MapResponse jsonResp = new MapResponse();
		
		String action = params.get(JsonField.ACTION_PARAM);
		
		try {
			if(StringsUtils.isEmpty(action)) {
				jsonResp.setError("User action is empty.");
			} else {
				switch(action) {
					case ActionJsonField.REGISTER:	registerUser(params, jsonResp);
													break;
												
					case ActionJsonField.SIGN_IN:	signIn(params, jsonResp);
													break;
												
					case ActionJsonField.SIGN_OUT:	signOut(params, jsonResp);
													break;
												
					case ActionJsonField.SEARCH_USER:	searchUser(params, jsonResp);
														break;
												
					default:	break;	
				}
			}

			response.getWriter().print(jsonResp);
			
		} catch (Exception e) {
			LOG.error("UserService failed.", e);
		}
	}
	
	private void registerUser(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String password = params.get(JsonField.PASSWORD);
		
		if(StringsUtils.isAnyEmpty(username, password)) {
			jsonResp.setError("Username or Password is empty");
			return;
		}

		try {
			RandomStringGenerator randStringGenerator = new RandomStringGenerator();
			String secureToken = randStringGenerator.newString(26);	
			String pwdHash = strongEncryptor.encrypt(password);
			
			userDBHandler.insert(new User(username, pwdHash, secureToken));
			
			jsonResp.add(JsonField.USERNAME, username);
			jsonResp.add(JsonField.SECURE_TOKEN, secureToken);
		} catch (Exception e) {
			jsonResp.setError("Registration failed.");
			LOG.error("Register failed with username {} and password {}.", username, password, e);
		}
	}
	
	private void signIn(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String password = params.get(JsonField.PASSWORD);
		
		if(StringsUtils.isAnyEmpty(username, password)) {
			jsonResp.setError("Username or password is empty");
			return;
		}
		
		try {
			User user = userDBHandler.select(username);
			String decryptedPassword = strongEncryptor.decrypt(user.getPassword());		
			
			if(!user.getUsername().equals(username) ||
					!decryptedPassword.equals(password)) {
				jsonResp.setError("Authentication failed.");
				return;
			}
			
			RandomStringGenerator randStringGenerator = new RandomStringGenerator();
			String secureToken = randStringGenerator.newString(26);	
			
			user.setSecureToken(secureToken);
			userDBHandler.update(user);
			
			jsonResp.add(JsonField.USERNAME, username);
			jsonResp.add(JsonField.SECURE_TOKEN, secureToken);
			jsonResp.add(JsonField.FRIEND_LIST, user.getFriends());
			
		} catch (Exception e) {
			jsonResp.setError("Sign in failed.");
			LOG.error("Sign in failed with username {} and password {}.", username, password, e);
		}
	}

	private void signOut(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		
		if(StringsUtils.isEmpty(username)) {
			jsonResp.setError("Username is empty");
			return;
		}
		
		try {
			User user = userDBHandler.select(username);
			user.setSecureToken("");
			userDBHandler.update(user);
			
			jsonResp.add(JsonField.USERNAME, username);
			
		} catch (Exception e) {
			jsonResp.setError("Sign out failed.");
			LOG.error("Sign out failed with username {}.", username, e);
		}
	}
	
	private void searchUser(Map<String, String> params, MapResponse jsonResp) {
		String searchName = params.get(JsonField.NAME);
		
		if(StringsUtils.isEmpty(searchName)) {
			jsonResp.setError("Searched username is empty");
			return;
		}
		
		try {
			User user = userDBHandler.select(searchName);
			String username = (user == null ? "" : user.getUsername());
			
			jsonResp.add(JsonField.USERNAME, username);
			
		} catch (Exception e) {
			jsonResp.setError("Sign out failed.");
			LOG.error("User search failed with username {}.", searchName, e);
		}
	}
}
