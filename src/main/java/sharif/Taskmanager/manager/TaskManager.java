package sharif.Taskmanager.manager;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sharif.Taskmanager.data.BackupRepository;
import sharif.Taskmanager.data.TaskRepository;
import sharif.Taskmanager.entity.*;

import javax.xml.ws.http.HTTPException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by amirmhp on 12/11/2018.
 */
@Service
public class TaskManager {
    @Autowired
    TaskRepository taskRepository;
    @Autowired
    BackupRepository backupRepository;
    @Autowired
    UserManager userManager;
    Gson gson = new Gson();

    public TaskManager(TaskRepository taskRepository, BackupRepository backupRepository, UserManager userManager) {
        this.taskRepository = taskRepository;
        this.backupRepository = backupRepository;
        this.userManager = userManager;
    }

    public Task addTask(RequestObject requestObject) {
        Long userId = userManager.getUserIdOfToken(requestObject.getToken());
        User backupUser = userManager.getUser(userId);
        BackupLog backupLog = new BackupLog();
        backupLog.setBackupJson(gson.toJson(backupUser));
        backupLog = backupRepository.save(backupLog);
        Task taskToAdd = (Task) requestObject.getContent();
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
        taskOwner.setPrevBackupId(backupLog.getId());
        userManager.updateUserTasks(taskOwner);
        return taskToAdd;
    }

    public Task editTask(RequestObject requestObject) {
        Task task = (Task) requestObject.getContent();
        String token = requestObject.getToken();
        Long userId = userManager.getUserIdOfToken(token);

        User backupUser = userManager.getUser(userId);
        BackupLog backupLog = new BackupLog();
        backupLog.setBackupJson(gson.toJson(backupUser));
        backupLog = backupRepository.save(backupLog);

        checkTokenAccessToUser(userId, token);
        Task editedTask = editTask(task, userId);
        User taskOwner = userManager.getUser(userId);
        taskOwner.setPrevBackupId(backupLog.getId());
        userManager.updateUserTasks(taskOwner);
        return editedTask;
    }

    public Task editTask(Task task, Long userId) {
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


    public void removeTask(RequestObject requestObject, boolean withChilds) {
        Task taskToRemove = (Task) requestObject.getContent();
        Long userId = taskToRemove.getUserId();

        User backupUser = userManager.getUser(userId);
        BackupLog backupLog = new BackupLog();
        backupLog.setBackupJson(gson.toJson(backupUser));
        backupLog = backupRepository.save(backupLog);

        checkTokenAccessToUser(userId, requestObject.getToken());
        User taskOwner = userManager.getUser(userId);
        if (withChilds) {
            removeTaskWithChilds(taskToRemove.getId(), taskOwner);
        } else {
            removeTaskOnly(taskToRemove.getId(), taskOwner);
        }
        taskOwner = userManager.getUser(userId);
        taskOwner.setPrevBackupId(backupLog.getId());
        userManager.updateUserTasks(taskOwner);
    }

    private void removeTaskOnly(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).get();
        Long parentId = task.getParentTaskId();
        ArrayList<Task> kids = new ArrayList<>(taskRepository.findByParentTaskId(task.getId()));
        for (Task kid : kids) {
            kid.setParentTaskId(parentId);
            editTask(kid, user.getID());
        }
        user = removeTaskFromUserTaskList(user, taskId);
        userManager.updateUserTasks(user);
    }

    private void removeTaskWithChilds(Long taskId, User user) {
        Task task = taskRepository.findById(taskId).get();
        ArrayList<Task> kids = new ArrayList<>(taskRepository.findByParentTaskId(task.getId()));
        if (kids.size() != 0) {
            for (Task kid : kids) {
                removeTaskWithChilds(kid.getId(), user);
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

    public void assignTasks(RequestObject requestObject) {
        TaskAssignDto taskAssignDto = (TaskAssignDto) requestObject.getContent();
        checkTokenAccessToUser(taskAssignDto.getAssignerId(), requestObject.getToken());
        User assigner = userManager.getUser(taskAssignDto.getAssignerId());
        Long assigneeId = -1L;
        boolean valid = false;
        for (User user : assigner.getMembers()) {
            if (user.getUserName().equals(taskAssignDto.getAssigneeUsername())) {
                valid = true;
                assigneeId = user.getID();
                break;
            }
        }
        if (!valid) {
            throw new HTTPException(401);
        }
        User assignee = userManager.getUser(assigneeId);
        assignee = addTaskTreeToUser(taskAssignDto.getTasks(), assignee, assigner.getUserName());
    }

    private User addTaskTreeToUser(List<Task> tasks, User assignee, String assignerUsername) {
        HashMap<Long, Long> taskidsMap = new HashMap<>(); // <oldId, newId>
        Task rootOfUser = taskRepository.findByUserIdAndParentTaskId(assignee.getID(), null);
        taskidsMap.put(-1L, rootOfUser.getId());
        for (Task task : tasks) {
            Long oldId = task.getId();
            task.setId(null);
            task.setParentTaskId(taskidsMap.get(task.getParentTaskId()));
            task.setAssignerUsername(assignerUsername);
            task = taskRepository.save(task);
            taskidsMap.put(oldId, task.getId());
            assignee.getTasks().add(task);
        }
        assignee = userManager.updateUserTasks(assignee);
        return assignee;
    }

    public User undo(Long userId, String token) {
        if (!checkTokenAccessToUser(userId, token)){
            throw new HTTPException(401);
        }
        User oldUser = userManager.getUser(userId);
        removeTaskWithChilds(taskRepository.findByUserIdAndParentTaskId(userId, null).getId(), oldUser);
        RequestObject requestObject = new RequestObject();
        requestObject.setToken(token);
        requestObject.setContent(oldUser);
        userManager.removeUser(requestObject);
        return userManager.importUser(requestObject);
    }
}
