package sharif.Taskmanager.entity;

import java.util.List;

/**
 * Created by amirmhp on 1/27/2019.
 */
public class TaskAssignDto extends BaseEntity {
    private List<Task> tasks;
    private Long assignerId;
    private String assigneeUsername;

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public Long getAssignerId() {
        return assignerId;
    }
    public void setAssignerId(Long assignerId) {
        this.assignerId = assignerId;
    }
    public String getAssigneeUsername() {
        return assigneeUsername;
    }
    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }
}
