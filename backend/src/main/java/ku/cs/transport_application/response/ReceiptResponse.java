package ku.cs.transport_application.response;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.common.ProductType;
import ku.cs.transport_application.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReceiptResponse {
    // order
    private UUID id;
    private LocalDateTime date;
    private OrderStatus status;
    private String cus_name;
    private String cus_address;
    // worker
    private String worker;
    private String phone;
    private String email;
    // user
    private String user;
    // products
    private List<ProductResponse> products;
}
