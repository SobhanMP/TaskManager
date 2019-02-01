package sharif.Taskmanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sharif.Taskmanager.data.TaskRepository;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.Task;
import sharif.Taskmanager.entity.User;
import sharif.Taskmanager.manager.UserManager;

import java.util.ArrayList;

/**
 * Created by amirmhp on 12/15/2018.
 */
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserManager userManager;
    @Autowired
    private TaskRepository taskRepository;

    public AdminController(UserManager userManager, TaskRepository taskRepository) {
        this.userManager = userManager;
        this.taskRepository = taskRepository;
    }


    @GetMapping(value = "/{id}")
    public User deleteUser(@PathVariable String id, @RequestHeader(value = "token") String token) throws Exception {
        RequestObject requestObject = new RequestObject();
        User user = new User();
        user.setID(Long.parseLong(id));
        requestObject.setContent(user);
        requestObject.setToken(token);
        user = userManager.removeUser(requestObject);
        for (Task task : user.getTasks()) {
            taskRepository.deleteById(task.getId());
        }
        return user;
    }

    @GetMapping(value = "/getall")
    public ArrayList<User> getAllUsers(@RequestHeader(value = "token") String token) throws Exception {
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        return new ArrayList<>(userManager.getAllUsers(requestObject));
    }

}

