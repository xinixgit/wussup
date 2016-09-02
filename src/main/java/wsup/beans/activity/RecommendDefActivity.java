package wsup.beans.activity;

/**
 * Activity when an user recommended a word to a 
 * another user.
 *
 * @author xdai2
 * @since May 10, 2015
 *
 */
public class RecommendDefActivity extends ActivityBuilder {
	
	public static final String RECOMMENDED_DEF = "recommended_def";
	
	public static final String RKMD_WORD_ACTIVITY_TYPE = "recommend_def";

	/**
	 * 
	 * @param username username of the person who recommended the word
	 * @param recommendee username of the person the word is recommended to
	 * @param word
	 */
	public RecommendDefActivity(String username, String recommendee, String word) {
		super(username, recommendee);
		getContentMap().put(RECOMMENDED_DEF, word);
	}

	@Override
	protected String getActivityType() {
		return RKMD_WORD_ACTIVITY_TYPE;
	}
}
