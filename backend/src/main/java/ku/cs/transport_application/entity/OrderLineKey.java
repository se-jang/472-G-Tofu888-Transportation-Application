package ku.cs.transport_application.entity;

import jakarta.persistence.Column;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.util.UUID;

@Data
public class OrderLineKey implements Serializable {
    @Column(name = "order_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID orderId;

    @Column(name = "product_id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID productId;
}
