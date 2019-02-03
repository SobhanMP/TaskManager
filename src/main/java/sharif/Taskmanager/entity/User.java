package sharif.Taskmanager.entity;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by amirmhp on 12/11/2018.
 */
@Entity
public class User extends BaseEntity {
    @NotNull
    private String userName;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ID;
    @NotNull
    private String hashedPassword;
    private  String name;
    @OneToMany
    private List<MembershipRequest> membershipRequests;
    @OneToMany
    private List<Task> tasks;
    private int TaskPoint;
    private List<Long> membersUid;


    public List<MembershipRequest> getMembershipRequests() {
        return membershipRequests;
    }
    public void setMembershipRequests(List<MembershipRequest> membershipRequests) {
        this.membershipRequests = membershipRequests;
    }
    public List<Long> getMembersUid() {
        return membersUid;
    }
    public void setMembersUid(List<Long> membersUid) {
        this.membersUid = membersUid;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Long getID() {
        return ID;
    }
    public void setID(Long ID) {
        this.ID = ID;
    }
    public String getHashedPassword() {
        return hashedPassword;
    }
    public void setHashedPassword(String hashedPassword) {
        this.hashedPassword = hashedPassword;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
    public int getTaskPoint() {
        return TaskPoint;
    }
    public void setTaskPoint(int taskPoint) {
        this.TaskPoint = taskPoint;
    }
}
