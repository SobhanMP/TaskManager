package sharif.Taskmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

/**
 * Created by amirmhp on 12/15/2018.
 */
@Entity
public class Task extends BaseEntity {
    private String name;
    private Date notifyDate;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long parentTaskId;
    private Long userId;
    private String AssignerUserId;
    private String deadline;
    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDeadline() {
        return deadline;
    }
    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }
    public Long getParentTaskId() {
        return parentTaskId;
    }
    public void setParentTaskId(Long parentTaskId) {
        this.parentTaskId = parentTaskId;
    }
    public String getName() {
        return name;
    }
    public void setName(String title) {
        this.name = title;
    }
    public Date getNotifyDate() {
        return notifyDate;
    }
    public void setNotifyDate(Date notifyDate) {
        this.notifyDate = notifyDate;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public String getAssignerUserId() {
        return AssignerUserId;
    }
    public void setAssignerUserId(String assignerUserId) {
        AssignerUserId = assignerUserId;
    }
}
