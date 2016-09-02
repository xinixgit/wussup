package wsup.task.search;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import def.util.StringsUtils;
import wsup.db.def.Def;
import wsup.db.def.DefDBHandler;
import wsup.task.Task;
import wsup.task.TaskResult;
import wsup.utils.SearchTaskUtil;
import wsup.web.JsonField;
import wsup.web.RequestHolder;
import wsup.web.SearchResultResponse;

public class DBSearchTask implements Task {
	private static final Logger LOG = LogManager.getLogger(DBSearchTask.class);

	@Autowired
	private DefDBHandler defHandler;
	
	@Autowired
	private RequestHolder requestHolder;

	public RequestHolder getRequestHolder() {
		return requestHolder;
	}

	public void setRequestHolder(RequestHolder requestHolder) {
		this.requestHolder = requestHolder;
	}

	public DefDBHandler getDefHandler() {
		return defHandler;
	}

	public void setDefHandler(DefDBHandler defHandler) {
		this.defHandler = defHandler;
	}

	@Override
	public TaskResult call() {
		SearchResultResponse searchResult = new SearchResultResponse();
		String query = requestHolder.getParam(JsonField.QUERY);
		query = SearchTaskUtil.sanitizeQueryWord(query);
		
		if(StringsUtils.isEmpty(query)) {
			return searchResult;
		}
		
		Set<String> defSet = new HashSet<String>();			
		try {
			List<Def> defObjList = defHandler.select(query);	
			for(Def def : defObjList) {
				defSet.add(def.getDef());
			}			
			
		} catch (Exception e) {
			LOG.error("DBSearch for " + query + " failed.", e);
		}		
		
		searchResult.setDefList(new ArrayList<String>(defSet));		
		searchResult.setQuery(query);
		
		return searchResult;
	}
}
