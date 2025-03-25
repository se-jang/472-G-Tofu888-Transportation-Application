package ku.cs.transport_application.repository;

import ku.cs.transport_application.common.UserRole;
import ku.cs.transport_application.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    User findByUsername(String username);
    User findByName(String name);
    List<User> findByRoleNot(UserRole role);
}
