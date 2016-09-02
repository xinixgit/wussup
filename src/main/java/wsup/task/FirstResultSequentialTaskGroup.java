package wsup.task;

import java.util.ArrayList;
import java.util.List;

public class FirstResultSequentialTaskGroup implements TaskGroup {
	private List<Task> taskList;

	public List<Task> getTaskList() {
		return taskList;
	}

	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	
	@Override
	public List<TaskResult> exec() throws Exception {
		List<TaskResult> resultList = new ArrayList<>();		
		for(Task t : taskList) {
			TaskResult tr = t.call();
			if(!tr.isEmpty()) {
				resultList.add(tr);
				break;
			}	
		}		
		
		return resultList;
	}

}
