package sharif.Taskmanager.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sharif.Taskmanager.entity.MembershipRequest;

/**
 * Created by amirmhp on 1/1/2019.
 */

@Transactional
@Repository
public interface MembershipRequestRepository extends CrudRepository<MembershipRequest, Long> {



}
