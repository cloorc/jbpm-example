package io.github.soiff.jbpm.example.handle;

import io.github.soiff.jbpm.example.lib.Manager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;

import java.util.Map;

/**
 * Created by zhangh on 2016/8/16.
 *
 * @author : zhangh@dtdream.com
 * @version : 1.0.0
 * @since : 1.8
 */
public class Handle {

    public static void main(String[] args) {
        // "sales-rep" gets notification
        final TaskService taskService = Manager.getRuntimeEngine().getTaskService();
        TaskSummary task4 = taskService.getTasksAssignedAsPotentialOwner("sales-rep", "en-UK").get(0);
        System.out.println("sales-rep executing task " + task4.getName() + "(" + task4.getId() + ": " + task4.getDescription() + ")");
        taskService.start(task4.getId(), "sales-rep");
        Map<String, Object> content = taskService.getTaskContent(task4.getId());
        for (Map.Entry<?, ?> entry : content.entrySet()) {
            System.out.println(entry.getKey() + " = " + entry.getValue());
        }
        taskService.complete(task4.getId(), "sales-rep", null);
    }
}
