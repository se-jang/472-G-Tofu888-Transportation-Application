package ku.cs.transport_application.DTO;

import ku.cs.transport_application.common.TransportationWorkerStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransportationWorkerDTO {

    private UUID id;
    private String username;
    private String name;
    private String phoneNumber;
    private String email;
    private String profilePicture;
    private TransportationWorkerStatus status;
}
