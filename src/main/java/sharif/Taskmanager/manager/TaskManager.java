package sharif.Taskmanager.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sharif.Taskmanager.data.TaskRepository;
import sharif.Taskmanager.entity.RequestObject;
import sharif.Taskmanager.entity.Task;
import sharif.Taskmanager.entity.User;

import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;

/**
 * Created by amirmhp on 12/11/2018.
 */
@Service
public class TaskManager {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserManager userManager;

    public TaskManager(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;

    }

    public Task addTask(RequestObject requestObject) {
        Task taskToAdd = (Task) requestObject.getContent();
        Long userId = userManager.getUserIdOfToken(requestObject.getToken());
        checkTokenAccessToUser(userId, requestObject.getToken());
        if (!validateTask(taskToAdd)) {
            throw new HTTPException(400);
        }
        User taskOwner = userManager.getUser(userId);
        taskToAdd.setUserId(taskOwner.getID());
        if ((taskToAdd.getParentTaskId() == null) && (taskOwner.getTasks().size() > 0)) {
            throw new HTTPException(400);
        }
        taskToAdd = taskRepository.save(taskToAdd);
        taskOwner.getTasks().add(taskToAdd);
        taskOwner.setTaskPoint(taskOwner.getTaskPoint() + 1);
        userManager.updateUserTasks(taskOwner);
        return taskToAdd;
    }

    public Task editTask(RequestObject requestObject) {
        Task task = (Task) requestObject.getContent();
        Long userId = userManager.getUserIdOfToken(requestObject.getToken());
        checkTokenAccessToUser(userId, requestObject.getToken());
        if (!validateTask(task)) {
            throw new HTTPException(400);
        }
        User taskOwner = userManager.getUser(userId);
        task.setUserId(taskOwner.getID());
        task = taskRepository.save(task);
        int index = 0;
        for (Task task1 : taskOwner.getTasks()) {
            if (task1.getId() == task.getId()) {
                index = taskOwner.getTasks().indexOf(task1);
            }
        }
        taskOwner.getTasks().remove(index);
        taskOwner.getTasks().add(task);
        userManager.updateUserTasks(taskOwner);
        return task;
    }


    private boolean validateTask(Task task) {
        return true;
        //todo implement
    }


    public void removeTask(RequestObject requestObject) {
        Task taskToRemove = (Task) requestObject.getContent();
        Long userId = taskToRemove.getUserId();
        checkTokenAccessToUser(userId, requestObject.getToken());
        User taskOwner = userManager.getUser(userId);
        removeTask(taskToRemove.getId(), taskOwner);
    }

    private void removeTask(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).get();
        ArrayList<Task> kids = new ArrayList<>(taskRepository.findByParentTaskId(task.getId()));
        if (kids.size() != 0) {
            for (Task kid : kids) {
                removeTask(kid.getId(), user);
            }
        }
        user = removeTaskFromUserTaskList(user, taskId);
        taskRepository.delete(task);
        userManager.updateUserTasks(user);
    }

    private User removeTaskFromUserTaskList(User user, Long taskId) {
        int taskToRemoveIndex = -1;
        for (Task task : user.getTasks()) {
            if (task.getId().equals(taskId)) {
                taskToRemoveIndex = user.getTasks().indexOf(task);
                break;
            }
        }
        if (taskToRemoveIndex == -1) {
            throw new HTTPException(400);
        }
        user.getTasks().remove(taskToRemoveIndex);
        return user;
    }


    public Task getTask(RequestObject requestObject) {
        Task taskToShow = (Task) requestObject.getContent();
        Long userId = userManager.getUserIdOfToken(requestObject.getToken());
        checkTokenAccessToUser(userId, requestObject.getToken());
        taskToShow = taskRepository.findById(taskToShow.getId()).get();
        if (taskToShow == null) {
            throw new HTTPException(400);
        }
        return taskToShow;
    }

    private boolean checkTokenAccessToUser(Long id, String token) {
        return userManager.checkTokenAccessToUser(id, token);
    }

}
