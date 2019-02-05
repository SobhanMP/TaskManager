package sharif.Taskmanager.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sharif.Taskmanager.entity.Task;

import java.util.List;

/**
 * Created by amirmhp on 1/1/2019.
 */

@Transactional
@Repository
public interface TaskRepository extends CrudRepository<Task, Long> {

    List<Task> findByParentTaskId (Long parentTaskId);

    Task findByUserIdAndParentTaskId(Long userId, Long parentTaskId);

}
