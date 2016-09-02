package wsup.task;

import java.util.List;

/**
 * A group of tasks to be executed sequentially
 * or in parallel.
 *
 * @author xdai2
 * @since Apr 12, 2015
 *
 */
public interface TaskGroup {
	public List<TaskResult> exec() throws Exception;
}
