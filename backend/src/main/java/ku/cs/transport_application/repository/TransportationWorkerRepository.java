package ku.cs.transport_application.repository;

import ku.cs.transport_application.common.TransportationWorkerStatus;
import ku.cs.transport_application.entity.TransportationWorker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TransportationWorkerRepository extends JpaRepository<TransportationWorker, UUID> {
    List<TransportationWorker> findByStatus(TransportationWorkerStatus status);
    TransportationWorker findByUsername(String username);
}
