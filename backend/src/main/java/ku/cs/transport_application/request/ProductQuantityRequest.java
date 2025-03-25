package ku.cs.transport_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class ProductQuantityRequest {

    @NotBlank
    @Positive
    private int quantity;
}
