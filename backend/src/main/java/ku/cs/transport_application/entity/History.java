package ku.cs.transport_application.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "`history`")
public class History {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID historyId;

    private double amount;

    private LocalDateTime paymentDate;

    @OneToOne
    @JsonIgnore
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
}
