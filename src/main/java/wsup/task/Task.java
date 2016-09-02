package wsup.task;

import java.util.concurrent.Callable;

public interface Task extends Callable<TaskResult> {
	
	/**
	 * To return a TaskResult.
	 */
	@Override
	public TaskResult call();
}
