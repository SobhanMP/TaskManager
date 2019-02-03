package sharif.Taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.Task;
import sharif.Taskmanager.entity.TaskAssignDto;
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
    public Task addTask(@RequestBody Task task, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(task);
        requestObject.setToken(token);
        return taskManager.addTask(requestObject);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public Task editTask(@RequestBody Task task, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(task);
        requestObject.setToken(token);
        return taskManager.editTask(requestObject);
    }

    @RequestMapping(value = "/assign", method = RequestMethod.POST)
    public void assignTasks(@RequestBody TaskAssignDto taskAssignDto, @RequestHeader(value = "token") String token) {
        RequestObject requestObject = new RequestObject();
        requestObject.setContent(taskAssignDto);
        requestObject.setToken(token);
        taskManager.assignTasks(requestObject);
    }

    @GetMapping(value = "/remove/{taskId}")
    public void removeTask (@PathVariable String taskId, @RequestHeader(value = "token") String token, @RequestHeader(value = "userId") String userId, @RequestHeader(value = "withChilds") boolean withChilds){
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        Task task = new Task();
        task.setId(Long.parseLong(taskId));
        task.setUserId(Long.parseLong(userId));
        requestObject.setContent(task);
        taskManager.removeTask(requestObject);
    }

    @GetMapping(value = "/{taskId}")
    public Task getTask(@PathVariable String taskId, @RequestHeader(value = "token") String token, @RequestHeader(value = "userId") String userId) {
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        Task task = new Task();
        task.setId(Long.parseLong(taskId));
        task.setUserId(Long.parseLong(userId));
        requestObject.setContent(task);
        return taskManager.getTask(requestObject);
    }


}
