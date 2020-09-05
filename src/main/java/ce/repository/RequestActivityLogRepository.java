package ce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ce.entity.RequestActivityLog;

public interface RequestActivityLogRepository extends JpaRepository<RequestActivityLog, Long> {

}
