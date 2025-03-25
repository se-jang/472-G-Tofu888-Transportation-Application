package ku.cs.transport_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import ku.cs.transport_application.common.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderRequest {

    @NotBlank(message = "Customer name is required.")
    private String customerName;

    @NotBlank(message = "Customer address is required.")
    private String customerAddress;

    private String workerUsername;

    private String username;

    private OrderStatus status;

    private LocalDateTime date;
    private LocalDateTime deliveredDate;
    private String paymentMethod;

    @NotEmpty(message = "At least one product is required.")
    private List<ProductDetailRequest> productDetails;
}
