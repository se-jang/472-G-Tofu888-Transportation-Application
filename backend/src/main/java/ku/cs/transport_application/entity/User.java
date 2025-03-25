package ku.cs.transport_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import ku.cs.transport_application.common.UserRole;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.List;
import java.util.UUID;

@Data
@Entity
public class User {
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
    private UserRole role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Order> orders;
}
