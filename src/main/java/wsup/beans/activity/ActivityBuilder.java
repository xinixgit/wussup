package wsup.beans.activity;

import java.util.HashMap;
import java.util.Map;

import def.util.JsonsUtils;
import wsup.db.activity.Activity;

/**
 * Different activity wrapper produces activity dao objects 
 * with different data fieldsto be inserted into DB.
 *
 * @author xdai2
 * @since May 7, 2015
 *
 */
public abstract class ActivityBuilder {

	protected Map<String, Object> contentMap = new HashMap<>();
	
	protected Activity activity;
	
	public ActivityBuilder(String username) {
		this(username, "");
	}
	
	public ActivityBuilder(String username, String targetedUser) {
		activity = new Activity(username, targetedUser, System.currentTimeMillis(), "");	
		activity.getPkey().setType(getActivityType());
	}
	
	public Activity getActivity() {
		activity.setContent(JsonsUtils.encode2Json(contentMap));
		return activity;
	}
	
	public Map<String, Object> getContentMap() {
		return contentMap;
	}

	abstract protected String getActivityType();
}
