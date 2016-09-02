package wsup.db.def;

import java.util.HashMap;
import java.util.Map;

import def.util.StringsUtils;
import wsup.web.JsonField;

public class DefUtil {
	public static Map<String, Object> toMap(Def def) {
		Map<String, Object> map = new HashMap<>();		
		map.put(JsonField.USERNAME, def.getPkey().getUsername());
		map.put(JsonField.DEF_WORD, def.getPkey().getWord());
		map.put(JsonField.DEF_DEFINITION, def.getPkey().getDef());
		map.put(JsonField.DEF_WORD_KEY, def.getWordKey());
		map.put(JsonField.DEF_EXAMPLE, def.getExample());
		map.put(JsonField.DEF_CURR_REVIEW_INTV, def.getCurrReviewIntv());
		map.put(JsonField.DEF_NEXT_REVIEW_TIME, def.getNextReviewTime());
		map.put(JsonField.DEF_ADD_DATE, def.getAddDate());
		
		return map;
	}
	
	public static Def fromMap(String username, Map<String, Object> mapData) {
		String word = (String)mapData.get(JsonField.DEF_WORD);
		String definition = (String)mapData.get(JsonField.DEF_DEFINITION);
		String wordKey = (String)mapData.get(JsonField.DEF_WORD_KEY);
		String example = (String)mapData.get(JsonField.DEF_EXAMPLE);
		String currReviewIntv = mapData.get(JsonField.DEF_CURR_REVIEW_INTV).toString();
		String nextReviewTime = mapData.get(JsonField.DEF_NEXT_REVIEW_TIME).toString();
		String addDate = mapData.get(JsonField.DEF_ADD_DATE).toString();
		
		if(StringsUtils.isAnyEmpty(username, word, definition, wordKey)) {
			throw new IllegalArgumentException("Not enough information to create a def.");
		}
		
		Def def = new Def(username, word, definition);
		def.setWordKey(wordKey);
		def.setExample(example);
		def.setCurrReviewIntv(currReviewIntv);
		def.setNextReviewTime(nextReviewTime);
		def.setAddDate(addDate);
		
		return def;
	}
}
