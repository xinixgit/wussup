package wsup.beans.activity;

public class DefSyncActivity extends ActivityBuilder{
	public static final String LAST_SYNC_TIME = "last_sync_time";
	
	public static final String DEF_SYNC_ACTIVITY_TYPE = "def_sync";
	
	public DefSyncActivity(String username, long lastSyncTime) {
		super(username);
		getContentMap().put(LAST_SYNC_TIME, new Long(lastSyncTime).toString());
	}

	@Override
	protected String getActivityType() {
		return DEF_SYNC_ACTIVITY_TYPE;
	}
}
