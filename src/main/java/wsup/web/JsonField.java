package wsup.web;

public interface JsonField {
	/*
	 * General
	 */
	public static final String ERROR = "error";
	
	public static final String RESPONSE = "response";
	
	public static final String ACTION_PARAM = "action";
	
	
	/*
	 * Search Service
	 */
	public static final String QUERY = "query";
	
	public static final String QUERY_DEF = "query_def";
	
	public static final String DEF_LIST = "def";
	
	
	/*
	 * User Service
	 */
	public static final String USERNAME = "username";
	
	public static final String PASSWORD = "password";
	
	public static final String SECURE_TOKEN = "secure_token";
	
	public static final String NAME = "name";
	
	
	/*
	 * Activity Service
	 */	
	public static final String DEF_INFO_OBJ = "info";
	
	public static final String DEF_WORD_KEY = "wordKey";
	
	public static final String DEF_WORD = "word";
	
	public static final String DEF_DEFINITION = "def";
	
	public static final String DEF_EXAMPLE = "example";
	
	public static final String DEF_CURR_REVIEW_INTV = "currReviewIntv";
	
	public static final String DEF_NEXT_REVIEW_TIME = "nextReviewTime";
	
	public static final String DEF_ADD_DATE = "addDate";
	
	public static final String FRIEND_NAME = "friend_name";
	
	public static final String FRIEND_LIST = "friend_list";
	
	public static final String SINCE = "since";
	
	public static final String ACTIVITY_LIST = "activity_list";
	
	public static final String TYPE = "type";
	
	public static final String TIMESTAMP = "timestamp";
	
	public static final String CONTENT = "content";
	
	public static final String RECOMMENDEE = "rkmdee";
	
	public static final String RECOMMENDED_DEF = "rkmded_def"; 
	
	public static final String DEF_MAP = "def_map";
	
	public static final String DEF_SYNC_MAP = "def_sync_map";
	
	public static final String DEF_SYNC_OBJECT = "def_sync_obj";
	
	public static final String DEF_SYNC_LAST_TIME = "def_last_sync_time";
	
	public static final String DEF_SYNC_CURR_TIME = "def_curr_sync_time";
	
	/**
	 * Action params only.
	 *
	 * @author xdai2
	 * @since May 3, 2015
	 *
	 */
	public static class ActionJsonField {
		/*
		 * User Service
		 */
		public static final String REGISTER = "register";
		
		public static final String SIGN_IN = "signin";
		
		public static final String SIGN_OUT = "signout";
		
		public static final String SEARCH_USER = "search_user";
		
		
		/*
		 * Activity Service
		 */
		public static final String ADD_DEF = "add_def";
		
		public static final String UPDATE_DEF = "update_def";
		
		public static final String DELETE_DEF = "del_def";
		
		public static final String FOLLOW_USER = "follow_user";
		
		public static final String UN_FOLLOW_USER = "unfollow_user";
		
		public static final String SYNC = "sync";
		
		public static final String RECOMMEND_DEF = "rkmd_def";
		
		public static final String GET_ALL_DEF = "get_all_def";
		
		public static final String SYNC_ALL_DEF = "sync_all_def";
	}
}
