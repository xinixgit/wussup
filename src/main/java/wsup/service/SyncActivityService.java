package wsup.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import wsup.beans.activity.AddDefActivity;
import wsup.beans.activity.DefSyncActivity;
import wsup.beans.activity.RecommendDefActivity;
import wsup.db.activity.Activity;
import wsup.db.activity.ActivityDBHandler;
import wsup.db.def.Def;
import wsup.db.def.DefDBHandler;
import wsup.db.def.DefUtil;
import wsup.db.user.User;
import wsup.db.user.UserDBHandler;
import wsup.web.ActivityResponseBuilder;
import wsup.web.JsonField;
import wsup.web.MapResponse;
import def.util.JsonsUtils;
import def.util.ListsUtils;
import def.util.StringsUtils;
import def.util.Util;

/**
 * Handles all sync activities.
 *
 * @author xdai2
 * @since Jun 21, 2015
 *
 */
public class SyncActivityService {
	private static final Logger LOG = LogManager.getLogger(SyncActivityService.class);
	
	private DefDBHandler defHandler;
	
	private UserDBHandler userDBHandler;
	
	private ActivityDBHandler activityDBHandler;

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
	
	public void syncActivitiy(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		long since = Util.parseLong(params.get(JsonField.SINCE), -1L);
		
		if(since < 0) {
			return;
		}
		
		try {
			User user = userDBHandler.select(username);
			Set<String> friendSet = user.getFriends();
			List<Activity> actiList = new ArrayList<>();
			
			if(!CollectionUtils.isEmpty(friendSet)) {
				// All friends ADD_DEF activities.
				List<Activity> addDefActi = activityDBHandler.select(friendSet, since, AddDefActivity.ADD_DEF_ACTIVITY_TYPE);
				if(!ListsUtils.isEmpty(addDefActi)) {
					actiList.addAll(addDefActi);
				}
				
				// All friends ADD_DEF activities.
				List<Activity> rkmdDefActi = activityDBHandler.select(username, friendSet, since, 
						RecommendDefActivity.RKMD_WORD_ACTIVITY_TYPE);	
				if(!ListsUtils.isEmpty(rkmdDefActi)) {
					actiList.addAll(rkmdDefActi);
				}
			}
			
			Collections.sort(actiList, new Comparator<Activity>(){
				@Override
				public int compare(Activity o1, Activity o2) {
					return new Long(o1.getPkey().getDatetime().getTime()).compareTo(o2.getPkey().getDatetime().getTime());
				}			
			});
			
			new ActivityResponseBuilder().build(jsonResp, actiList);
			
		} catch (Exception e) {
			LOG.error("Sync activity for user {} failed.", username, e);
			jsonResp.setError("Sync activity failed.");
		}
	}
	
	/**
	 * Always treat client side def with priority. Only for items client
	 * doesn't have but exists in db, include it to client response.
	 */
	public void syncAllDef(Map<String, String> params, MapResponse jsonResp) {
		String username = params.get(JsonField.USERNAME);
		String syncJson = params.get(JsonField.DEF_SYNC_OBJECT);	
		
		if(StringsUtils.isEmpty(username) || username.equals(Def.SYSTEM_USER)) {
			String error = "Invalid username presented.";
			LOG.error(error);
			jsonResp.setError(error);
			return;
		}
		
		if(StringUtils.isEmpty(syncJson)) {
			String error = "Invalid data presented.";
			LOG.error(error);
			jsonResp.setError(error);
			return;
		}

		Map<String, Object> syncObj = JsonsUtils.decode2Map(syncJson);
		Map<String, Object> cltDefMap = (Map<String, Object>)syncObj.get(JsonField.DEF_SYNC_MAP);			
		Map<String, Object> respDefObj = new HashMap<>();
		
		try {			
			Map<String, Def> svrDefMap = new HashMap<>();
			List<Def> svrInsList = new ArrayList<>();
			List<Def> svrDelList = new ArrayList<>();
			Map<String, Object> cltInsMap = new HashMap<>();
			
			List<Activity> syncActiList = activityDBHandler.select(new HashSet<String>(Arrays.asList(username)), 
					DefSyncActivity.DEF_SYNC_ACTIVITY_TYPE);

			List<Def> defListInDb = defHandler.selectAll(username);	
			for(Def def : defListInDb) {
				String wordKey = def.getWordKey();
				svrDefMap.put(wordKey, def);
			}
			
			/*
			 * Insert and update DB with newer def from client.
			 */
			for(Iterator<String> keyIter = cltDefMap.keySet().iterator(); keyIter.hasNext();) {
				String defKey = keyIter.next();
				Map<String, Object> cltDefObj = (Map<String, Object>)cltDefMap.get(defKey);
				Def cltDef = DefUtil.fromMap(username, cltDefObj);
				
				if(svrDefMap.containsKey(defKey)) {
					Def svrDef = (Def)svrDefMap.get(defKey);					
					if(cltDef.equals(svrDef)) {
						continue;
					}
					
					svrDelList.add(svrDef);
				}
				
				svrInsList.add(cltDef);
			}
			
			try {
				if(!ListsUtils.isEmpty(svrDelList)) {
					defHandler.deleteAll(svrDelList);
				}
				
				if(!ListsUtils.isEmpty(svrInsList)) {
					defHandler.insertAll(svrInsList);
				}
			} catch (Exception e) {
				LOG.error("Update Def DB with client data failed.", e);
			}
			
			/*
			 * Record this sync activity 
			 */
			long currSyncTime = System.currentTimeMillis();
			Activity newActi = new DefSyncActivity(username, currSyncTime).getActivity();
			if(syncActiList != null && syncActiList.size() > 0) {
				Activity lastSyncActi = syncActiList.get(0);
				lastSyncActi.setContent(newActi.getContent());
				activityDBHandler.update(lastSyncActi);
			} else {
				activityDBHandler.insert(newActi);
			}
			
			/*
			 * Find out what's missing on client's side, and put it
			 * into response.
			 */
			for(Iterator<String> keyIter = svrDefMap.keySet().iterator(); keyIter.hasNext();) {
				String defKey = keyIter.next();
				if(!cltDefMap.containsKey(defKey)) {
					Def def = (Def)svrDefMap.get(defKey);
					cltInsMap.put(defKey, DefUtil.toMap(def));
				}
			}

			respDefObj.put(JsonField.DEF_SYNC_MAP, cltInsMap);
			respDefObj.put(JsonField.USERNAME, username);		
			respDefObj.put(JsonField.DEF_SYNC_CURR_TIME, currSyncTime);
			jsonResp.add(JsonField.DEF_SYNC_OBJECT, respDefObj);
			
		} catch (Exception e) {
			LOG.error("Getting all words for user {} failed.", username, e);
			jsonResp.setError("Failed to sync all definition.");
		}
	}
}
