package io.github.soiff.jbpm.example.manager;

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
public class MangerReview {

    public static void main(String[] args) {
        // "john" as manager reviews request
        final TaskService taskService = Manager.getRuntimeEngine().getTaskService();
        TaskSummary task3 = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK").get(0);
        System.out.println("john executing task " + task3.getName() + "(" + task3.getId() + ": " + task3.getDescription() + ")");
        taskService.claim(task3.getId(), "john");
        taskService.start(task3.getId(), "john");
        Map<String,Object> results = new HashMap<String, Object>();
        results.put("outcome", "Agree");
        taskService.complete(task3.getId(), "john", results);
    }
}
