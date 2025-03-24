package ku.cs.transport_application.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditProfileRequest {

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z\\s]+$", message = "Name can only contain letters and spaces")
    public String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    public String email;

    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[0-9]*$", message = "Phone number must be numeric")
    @Size(min = 10, max = 10, message = "Phone number must be exactly 10 digits")
    public String phoneNumber;
}
