package wsup.utils;

import org.apache.commons.lang.StringUtils;

public class SearchTaskUtil {
	public static final String QUERY_CLEAN_PATTERN = "\\d?\\W?";
	
	public static String sanitizeQueryWord(String queryWord) {
		if(StringUtils.isEmpty(queryWord)) {
			return queryWord;
		}
	
		String[] array = StringUtils.split(queryWord);
		if(array.length == 1) {
			return array[0].replaceAll(QUERY_CLEAN_PATTERN, "");
		}
		
		StringBuilder buf = new StringBuilder();
		for(int i=0; i<array.length; i++) {
			String s = array[i].replaceAll(QUERY_CLEAN_PATTERN, "");
			buf.append(s);
			
			if(i < array.length-1) {
				buf.append(" ");
			}
		}
		
		return buf.toString();
	}
	
	public static void main(String[] arg) {
		String s = "Pri*<s9t1in0e";
		System.out.println(sanitizeQueryWord(s));
	}
}
