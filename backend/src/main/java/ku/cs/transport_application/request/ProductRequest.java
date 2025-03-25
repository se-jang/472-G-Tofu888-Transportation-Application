package ku.cs.transport_application.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import ku.cs.transport_application.common.ProductType;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z]+$", message = "Name only contain letters")
    private String name;

    @NotBlank
    private ProductType type;
}
