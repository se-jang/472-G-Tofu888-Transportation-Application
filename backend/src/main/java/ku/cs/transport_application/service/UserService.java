package ku.cs.transport_application.service;

import ku.cs.transport_application.common.UserRole;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers() {
        return userRepository.findByRoleNot(UserRole.ADMIN);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElse(null);
    }

    public void setUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}
