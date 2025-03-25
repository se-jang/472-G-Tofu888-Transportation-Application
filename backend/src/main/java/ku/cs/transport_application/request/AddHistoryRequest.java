package ku.cs.transport_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class AddHistoryRequest {
    @NotBlank
    private UUID orderId;
}
