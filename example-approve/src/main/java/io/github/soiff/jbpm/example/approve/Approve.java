package io.github.soiff.jbpm.example.approve;

import io.github.soiff.jbpm.example.lib.Manager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
public class Approve {

    public static void main(String[] args) {
        // "krisv" approves result
        final TaskService taskService = Manager.getRuntimeEngine().getTaskService();
        TaskSummary task2 = taskService.getTasksAssignedAsPotentialOwner("krisv", "en-UK").get(0);
        System.out.println("krisv executing task " + task2.getName() + "(" + task2.getId() + ": " + task2.getDescription() + ")");
        taskService.start(task2.getId(), "krisv");
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("outcome", "Agree");
        taskService.complete(task2.getId(), "krisv", results);
    }
}
