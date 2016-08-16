package io.github.soiff.jbpm.example.review;

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
public class Review {

    public static void main(String[] args) {
        // "sales-rep" reviews request
        TaskService taskService = Manager.getRuntimeEngine().getTaskService();
        TaskSummary task1 = taskService.getTasksAssignedAsPotentialOwner("sales-rep", "en-UK").get(0);
        System.out.println("Sales-rep executing task " + task1.getName() + "(" + task1.getId() + ": " + task1.getDescription() + ")");
        taskService.claim(task1.getId(), "sales-rep");
        taskService.start(task1.getId(), "sales-rep");
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("comment", "Agreed, existing laptop needs replacing");
        results.put("outcome", "Accept");
        taskService.complete(task1.getId(), "sales-rep", results);
    }
}
