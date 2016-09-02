package wsup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import def.util.JsonsUtils;
import def.util.StringsUtils;
import wsup.beans.activity.AddDefActivity;
import wsup.beans.activity.RecommendDefActivity;
import wsup.db.activity.ActivityDBHandler;
import wsup.db.def.Def;
import wsup.db.def.DefDBHandler;
import wsup.db.def.DefUtil;
import wsup.db.user.UserDBHandler;
import wsup.utils.WebUtils;
import wsup.web.JsonField;
import wsup.web.JsonField.ActionJsonField;
import wsup.web.MapResponse;

public class ActivityService implements Service {
	private static final Logger LOG = LogManager.getLogger(ActivityService.class);
	
	private DefDBHandler defHandler;
	
	private UserDBHandler userDBHandler;
	
	private ActivityDBHandler activityDBHandler;
	
	private SyncActivityService syncService;

	public DefDBHandler getDefHandler() {
		return defHandler;
	}

	public void setDefHandler(DefDBHandler defHandler) {
		this.defHandler = defHandler;
	}
	
	public UserDBHandler getUserDBHandler() {
		return userDBHandler;
	}

	public void setUserDBHandler(UserDBHandler userDBHandler) {
		this.userDBHandler = userDBHandler;
	}
	
	public ActivityDBHandler getActivityDBHandler() {
		return activityDBHandler;
	}

	public void setActivityDBHandler(ActivityDBHandler activityDBHandler) {
		this.activityDBHandler = activityDBHandler;
	}
	
	public SyncActivityService getSyncService() {
		return syncService;
	}
	
	public void setSyncService(SyncActivityService syncService) {
		this.syncService = syncService;
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException {		
		Map<String, String> params = WebUtils.getStringParams(request);
		MapResponse jsonResp = new MapResponse();
		
		String action = params.get(JsonField.ACTION_PARAM);
		
		try {
			if(StringsUtils.isEmpty(action)) {
				jsonResp.setError("Activity action is empty.");
			} else {
				switch(action) {
					case ActionJsonField.ADD_DEF: {
						WebUtils.ensureRequestType(request, "POST");
						addDef(params, jsonResp);
						break;
					}
					
					case ActionJsonField.UPDATE_DEF: {
						WebUtils.ensureRequestType(request, "POST");
						updateDef(params, jsonResp);
						break;
					}
					
					case ActionJsonField.DELETE_DEF: {
						WebUtils.ensureRequestType(request, "POST");
						deleteDef(params, jsonResp);
						break;
					}
					
					case ActionJsonField.GET_ALL_DEF: {
						WebUtils.ensureRequestType(request, "GET");
						getAllDef(params, jsonResp);
						break;
					}
												
					case ActionJsonField.FOLLOW_USER: {
						WebUtils.ensureRequestType(request, "POST");
						followUser(params, jsonResp);
						break;	
					}
					
					case ActionJsonField.UN_FOLLOW_USER: {
						WebUtils.ensureRequestType(request, "POST");
						unfollowUser(params, jsonResp);
						break;	
					}
					
					case ActionJsonField.RECOMMEND_DEF: {
						WebUtils.ensureRequestType(request, "POST");
						addRecommendDefActivitiy(params, jsonResp);
						break;
					}
					
					// Sync Activities
					case ActionJsonField.SYNC_ALL_DEF: {
						WebUtils.ensureRequestType(request, "POST");
						syncService.syncAllDef(params, jsonResp);
						break;
					}
					
					case ActionJsonField.SYNC: {
						WebUtils.ensureRequestType(request, "GET");
						syncService.syncActivitiy(params, jsonResp);
						break;
					}
												
					default:	break;	
				}
			}

			response.getWriter().print(jsonResp);
			
		} catch (Exception e) {
			LOG.error("ActivityService failed.", e);
		}
	}
	
	private void addDef(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String infoJson = params.get(JsonField.DEF_INFO_OBJ);
		
		Map<String, Object> infoObj = JsonsUtils.decode2Map(infoJson);
		
		try {
			Def newDef = DefUtil.fromMap(username, infoObj);
			defHandler.insert(newDef);		
			activityDBHandler.insert(new AddDefActivity(username, newDef.getPkey().getWord()).getActivity());
			
			jsonResp.add(JsonField.QUERY, newDef.getPkey().getWord());
			jsonResp.add(JsonField.QUERY_DEF, newDef.getDef());			
		} catch (Exception e) {
			LOG.error("Add def for user {} and word {} failed.", username, infoObj.get(JsonField.DEF_WORD), e);
			jsonResp.setError("Adding new definition failed.");
		}
	}
	
	private void updateDef(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String infoJson = params.get(JsonField.DEF_INFO_OBJ);
		
		Map<String, Object> infoObj = JsonsUtils.decode2Map(infoJson);
		
		try {
			Def newDef = DefUtil.fromMap(username, infoObj);
			defHandler.update(newDef);
			
			jsonResp.add(JsonField.QUERY, newDef.getPkey().getWord());
			jsonResp.add(JsonField.QUERY_DEF, newDef.getDef());			
		} catch (Exception e) {
			LOG.error("Add def for user {} and word {} failed.", username, infoObj.get(JsonField.DEF_WORD), e);
			jsonResp.setError("Updating definition failed.");
		}
	}
	
	private void deleteDef(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String query = params.get(JsonField.QUERY);
		String queryDef = params.get(JsonField.QUERY_DEF);
		
		try {
			defHandler.delete(new Def(username, query, queryDef));
			
			jsonResp.add(JsonField.QUERY, query);
			jsonResp.add(JsonField.QUERY_DEF, queryDef);			
		} catch (Exception e) {
			LOG.error("Deleting def for user {} and word {} failed.", username, query, e);
			jsonResp.setError("Adding new definition failed.");
		}
	}
	
	private void getAllDef(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		
		if(StringsUtils.isEmpty(username) || username.equals(Def.SYSTEM_USER)) {
			jsonResp.setError("Invalid username presented.");
			return;
		}
		
		Map<String, String> defMap = new HashMap<>();
		
		try {
			List<Def> defList = defHandler.selectAll(username);
			for(Def def : defList) {
				defMap.put(def.getPkey().getWord(), def.getPkey().getDef());
			}
			
			jsonResp.add(JsonField.DEF_MAP, defMap);
			jsonResp.add(JsonField.USERNAME, username);			
		} catch (Exception e) {
			LOG.error("Getting all words for user {} failed.", username, e);
			jsonResp.setError("Getting all words failed.");
		}
	}
	
	private void followUser(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String friendName = params.get(JsonField.FRIEND_NAME);
		
		try {
			userDBHandler.addFriend(username, friendName);
			
			jsonResp.add(JsonField.USERNAME, username);
			jsonResp.add(JsonField.FRIEND_NAME, friendName);
			
		} catch (Exception e) {
			LOG.error("Add friend for user {} and friend {} failed.", username, friendName, e);
			jsonResp.setError("Adding new friend failed.");
		}
	}
	
	private void unfollowUser(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String friendName = params.get(JsonField.FRIEND_NAME);
		
		try {
			userDBHandler.deleteFriend(username, friendName);
			
			jsonResp.add(JsonField.USERNAME, username);
			jsonResp.add(JsonField.FRIEND_NAME, friendName);
			
		} catch (Exception e) {
			LOG.error("Delete friend {} for user {} failed.", friendName, username, e);
			jsonResp.setError("Adding new friend failed.");
		}
	}
	
	private void addRecommendDefActivitiy(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String rkmdee = params.get(JsonField.RECOMMENDEE);
		String rkmdWord = params.get(JsonField.RECOMMENDED_DEF);
		
		try {
			activityDBHandler.insert(new RecommendDefActivity(username, rkmdee, rkmdWord).getActivity());
			jsonResp.add(JsonField.RECOMMENDED_DEF, rkmdWord);
			jsonResp.add(JsonField.RECOMMENDEE, rkmdee);
		} catch (Exception e) {
			LOG.error("Save recommend def activity from user {} failed.", username, e);
			jsonResp.setError("Add recommend def activity failed.");
		}
	}
}
