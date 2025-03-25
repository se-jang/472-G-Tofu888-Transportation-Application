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
public class Receipt {
    @Id
    @GeneratedValue
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID receiptId;

    @OneToOne
    @JoinColumn(name = "order_id")
    @JsonIgnore
    private Order order;

    private LocalDateTime createAt;
}
