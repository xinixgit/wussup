package wsup.web;

import java.util.List;

public class SearchResultResponse extends MapResponse {

	public void setDefList(List<String> defList) {
		add(JsonField.DEF_LIST, defList);
	}

	public void setQuery(String query) {
		add(JsonField.QUERY, query);
	}
	
	@Override
	public boolean isEmpty() {
		return getAsList(JsonField.DEF_LIST, String.class).isEmpty();
	}
}
