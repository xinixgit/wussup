package wsup.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import def.util.JsonsUtils;
import wsup.task.TaskGroup;
import wsup.task.TaskResult;

/**
 * Entry point for vocab search. This class initiates a multi-threaded tasks
 * to find definition of searched word/phrase.
 *
 * @author xdai2
 * @since Apr 10, 2015
 *
 */
public class SearchService implements Service {
	private static final Logger LOG = LogManager.getLogger(SearchService.class);
	
	private TaskGroup taskGroup;
	
	public TaskGroup getTaskGroup() {
		return taskGroup;
	}

	public void setTaskGroup(TaskGroup taskGroup) {
		this.taskGroup = taskGroup;
	}

	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) {
		Map<String, Object> respMap = new HashMap<>();
		
		try {
			List<TaskResult> resultList = taskGroup.exec();
			
			if(!resultList.isEmpty()) {
				respMap.putAll(resultList.get(0).toMap());
			}
			
			String json = JsonsUtils.encode2Json(respMap);
			response.getWriter().print(json);
		} catch (Exception e) {
			LOG.error("SearchService failed.", e);
		}
	}
}
