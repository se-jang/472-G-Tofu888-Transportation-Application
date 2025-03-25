package ku.cs.transport_application.response;

import ku.cs.transport_application.common.ProductType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class ProductResponse {
    private UUID id;
    private String name;
    private ProductType type;
    private int quantity;
}

