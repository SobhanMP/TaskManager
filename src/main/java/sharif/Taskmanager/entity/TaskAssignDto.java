package sharif.Taskmanager.entity;

import java.util.List;

/**
 * Created by amirmhp on 1/27/2019.
 */
public class TaskAssignDto extends BaseEntity {
    List<Task> tasks;
    Long assignerId;
    Long assigneeId;
}
