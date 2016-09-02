package wsup.task;

import java.util.Map;

/**
 * Bean class for what any Task returns.
 *
 * @author xdai2
 * @since Apr 12, 2015
 *
 */
public interface TaskResult {
	public boolean isEmpty();
	
	public Map<String, Object> toMap();
}
