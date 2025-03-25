package ku.cs.transport_application.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class JwtResponse {
    private String token;
    private String role;
    private String username;
    private UUID id;
}

