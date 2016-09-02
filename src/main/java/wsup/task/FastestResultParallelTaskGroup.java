package wsup.task;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Executes a group of tasks, and don't wait for
 * the subsequent tasks to finish if the prior task
 * returns non-empty result.
 * 
 * Make sure to arrange slower tasks towards the end.
 */
public class FastestResultParallelTaskGroup implements TaskGroup {
	private List<Task> taskList;

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}

	/**
	 * Take the fastest returned result and return, interrupt
	 * all other pending tasks.
	 */
	@Override
	public List<TaskResult> exec() throws Exception {
		ExecutorService executor = Executors.newFixedThreadPool(taskList.size());
		List<TaskResult> resultList = new ArrayList<>();
		List<Future<TaskResult>> futureList = new ArrayList<>();
		
		for(Task task : taskList) {
			futureList.add(executor.submit(task));
		}
		
		for(Future<TaskResult> future : futureList) {
			TaskResult tr = future.get();
			if(!tr.isEmpty()) {
				executor.shutdownNow();
				resultList.add(tr);
				break;
			}	
		}
		
		return resultList;
	}
}
