package ku.cs.transport_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import ku.cs.transport_application.common.TransportationWorkerStatus;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class TransportationWorker {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    private String username;
    private String name;
    private String password;
    private String phoneNumber;
    private String email;
    private String profilePicture;
    private TransportationWorkerStatus status;

    @OneToMany(mappedBy = "worker")
    private List<Order> orders = new ArrayList<>();

}
