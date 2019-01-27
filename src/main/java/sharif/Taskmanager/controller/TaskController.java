package sharif.Taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.Task;
import sharif.Taskmanager.entity.User;
import sharif.Taskmanager.manager.TaskManager;

/**
 * Created by amirmhp on 12/15/2018.
 */
@CrossOrigin
@RestController
@RequestMapping("/task")
public class TaskController {

    @Autowired
    private TaskManager taskManager;

    public TaskController(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public User addTask(@RequestBody Task task, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(task);
        return taskManager.addTask(requestObject);
    }

    @GetMapping(value = "/remove/{id}")
    public User removeTask (@PathVariable String taskId, @RequestHeader(value = "token") String token){
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        Task task = new Task();
        task.setTaskId(Long.parseLong(taskId));
        requestObject.setContent(task);
        return taskManager.removeTask(requestObject);
    }

    @GetMapping(value = "/{id}")
    public Task getTask(@PathVariable String taskId, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        Task task = new Task();
        task.setTaskId(Long.parseLong(taskId));
        requestObject.setContent(task);
        return taskManager.getTask(requestObject);
    }
}
