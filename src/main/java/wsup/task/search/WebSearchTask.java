package wsup.task.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import def.util.ArraysUtils;
import def.util.PatternUtil;
import def.util.StringsUtils;
import def.util.Util;
import wsup.db.def.DefDBHandler;
import wsup.task.Task;
import wsup.task.TaskResult;
import wsup.utils.SearchTaskUtil;
import wsup.web.JsonField;
import wsup.web.RequestHolder;
import wsup.web.SearchResultResponse;

public class WebSearchTask implements Task {
	private static final Logger LOG = LogManager.getLogger(WebSearchTask.class);
	
	private String urlPrefix;
	private String blockStart;
	private String blockEnd;

	@Autowired
	private RequestHolder requestHolder;
	
	@Autowired
	private DefDBHandler defHandler;
	
	public DefDBHandler getDefHandler() {
		return defHandler;
	}

	public void setDefHandler(DefDBHandler defHandler) {
		this.defHandler = defHandler;
	}

	public RequestHolder getRequestHolder() {
		return requestHolder;
	}

	public void setRequestHolder(RequestHolder requestHolder) {
		this.requestHolder = requestHolder;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public void setUrlPrefix(String urlPrefix) {
		this.urlPrefix = urlPrefix;
	}
	
	
	public String getBlockStart() {
		return blockStart;
	}

	public void setBlockStart(String blockStart) {
		this.blockStart = blockStart;
	}

	public String getBlockEnd() {
		return blockEnd;
	}

	public void setBlockEnd(String blockEnd) {
		this.blockEnd = blockEnd;
	}

	@Override
	public TaskResult call() {
		SearchResultResponse searchResult = new SearchResultResponse();
		String query = requestHolder.getParam(JsonField.QUERY);
		query = SearchTaskUtil.sanitizeQueryWord(query);
		
		if(StringsUtils.isEmpty(query)) {
			return searchResult;
		}
		
		List<String> dirtyList = new ArrayList<>();
		List<String> cleanList = new ArrayList<>();
		
		try {
			String webSrc = Util.getWebSource(urlPrefix + query);
			webSrc = PatternUtil.parseOut(webSrc, blockStart, blockEnd);
			
			List<String> pList = PatternUtil.parseOut(webSrc, "<p.*?>", "</p>", 2);
			for(String p : pList) {
				if(!StringsUtils.isEmpty(p)) {
					List<String> list = ArraysUtils.array2List(p.split(":"));
					dirtyList.addAll(list);
				}
			}
			
			for(String str : dirtyList) {
				str = str.trim().replaceAll("\"", "");
				if(StringsUtils.isEmpty(str)) {
					continue;
				}
				
				cleanList.add(str);
			}
			
		} catch (IOException ioe) {
			LOG.error("WebSearch for " + query + " failed.", ioe);
		}
		
		/*
		 * Save def to database as sys_user as well so we always
		 * have a golden copy of defs.
		 */
		if(!cleanList.isEmpty()) {
			new Thread(new NewDefRunnable(query, cleanList, defHandler)).run();
		}
		
		searchResult.setQuery(query);
		searchResult.setDefList(cleanList);
		
		return searchResult;
	}
}
