package ku.cs.transport_application.repository;

import ku.cs.transport_application.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.id IN (SELECT ol.id.productId FROM OrderLine ol WHERE ol.id.orderId = :orderId)")
    void deleteByOrderId(@Param("orderId") UUID orderId);
}
