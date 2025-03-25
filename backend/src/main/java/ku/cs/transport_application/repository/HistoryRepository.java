package ku.cs.transport_application.repository;

import ku.cs.transport_application.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HistoryRepository extends JpaRepository<History, UUID> {
    /*
     * @desc    Join entities <History -> Order -> User.id>
     * @param   id of a user
     * @return  payment histories
     */
    @Query("SELECT h FROM History h JOIN h.order o JOIN o.user u WHERE u.id = :userId")
    List<History> findByUserId(@Param("userId") UUID userId);

    List<History> findByOrderId(UUID orderId);
}
