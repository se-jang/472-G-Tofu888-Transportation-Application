package ku.cs.transport_application.repository;

import ku.cs.transport_application.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface OrderLineRepository extends JpaRepository<OrderLine, UUID> {
    List<OrderLine> findByOrderId(UUID orderId);

    @Modifying
    @Transactional
    @Query("DELETE FROM OrderLine ol WHERE ol.id.orderId = :orderId")
    void deleteByOrderId(@Param("orderId") UUID orderId);

}
