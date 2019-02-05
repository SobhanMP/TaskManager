package sharif.Taskmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * Created by amirmhp on 12/15/2018.
 */
@Entity
public class BackupLog extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;
    String backupJson;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackupJson() {
        return backupJson;
    }

    public void setBackupJson(String backupJson) {
        this.backupJson = backupJson;
    }
}
