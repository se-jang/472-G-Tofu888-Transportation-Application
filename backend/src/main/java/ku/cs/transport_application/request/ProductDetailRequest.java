package ku.cs.transport_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ku.cs.transport_application.common.ProductType;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductDetailRequest {

    private UUID id;

    @NotBlank(message = "Product name is required")
    private String productName;

    @NotNull(message = "Product type is required")
    private ProductType productType;

    @NotBlank(message = "Quantity is required")
    private Integer quantity;
}
