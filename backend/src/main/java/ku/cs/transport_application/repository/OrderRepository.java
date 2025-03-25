package ku.cs.transport_application.repository;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByUserId(UUID userId);
    List<Order> findByWorkerId(UUID workerId);
}