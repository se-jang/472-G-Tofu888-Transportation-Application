package ku.cs.transport_application.repository;

import ku.cs.transport_application.entity.Receipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, UUID> {
    Receipt findByOrderId(UUID orderId);
}
