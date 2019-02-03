package sharif.Taskmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by amirmhp on 1/27/2019.
 */
@Entity
public class MembershipRequest extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    Long requesterUserId;
    Long userId;


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getRequesterUserId() {
        return requesterUserId;
    }
    public void setRequesterUserId(Long requesterUserId) {
        this.requesterUserId = requesterUserId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
