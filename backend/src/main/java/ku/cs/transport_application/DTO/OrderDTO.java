package ku.cs.transport_application.DTO;

import ku.cs.transport_application.common.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class OrderDTO {
    private UUID id;
    private OrderStatus status;
    private String customerName;
    private LocalDateTime date;
    private LocalDateTime deliveredDate;
    private String creatorName;

    private double total;
    private String paymentLink;
}
