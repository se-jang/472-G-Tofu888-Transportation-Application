package ku.cs.transport_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderLine {
    @EmbeddedId
    private OrderLineKey id;

    @Column(nullable = false)
    private int quantity;

    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id", foreignKey = @ForeignKey(name = "FK_order_orderLine", value = ConstraintMode.CONSTRAINT))
    @JsonIgnore
    private Order order;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "FK_product_orderLine", value = ConstraintMode.CONSTRAINT))
    @JsonIgnore
    private Product product;

}
