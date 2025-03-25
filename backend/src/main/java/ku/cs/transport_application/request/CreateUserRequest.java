package ku.cs.transport_application.request;

import jakarta.validation.constraints.*;
import ku.cs.transport_application.common.UserRole;
import lombok.Data;

@Data
public class CreateUserRequest {
    @NotBlank
    @Size(min=4, message = "Username must have at least 4 characters")
    private String username;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    private String name;

    @NotBlank
    @Size(min=8, max=128, message = "Password must have at least 8 characters")
    private String password;

    @Pattern(regexp = "^[0-9]*$", message = "Phone number must be numeric")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "User role is required")
    private UserRole role;
}
