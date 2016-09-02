package wsup.beans.activity;

/**
 * Activity which an user added a new def.
 *
 * @author xdai2
 * @since May 7, 2015
 *
 */
public class AddDefActivity extends ActivityBuilder {
	public static final String ADDED_WORD = "added_word";
	
	public static final String ADD_DEF_ACTIVITY_TYPE = "add_def";
	
	public AddDefActivity(String username, String word) {
		super(username);
		getContentMap().put(ADDED_WORD, word);
	}

	@Override
	protected String getActivityType() {
		return ADD_DEF_ACTIVITY_TYPE;
	}
}
