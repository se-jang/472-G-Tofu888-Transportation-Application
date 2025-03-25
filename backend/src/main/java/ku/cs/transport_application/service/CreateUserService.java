package ku.cs.transport_application.service;

import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.UserRepository;
import ku.cs.transport_application.request.CreateUserRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public boolean isUsernameUserAvailable(String username) {
        return userRepository.findByUsername(username) == null;
    }

    public void createUser(CreateUserRequest user) {
        User record = modelMapper.map(user, User.class);

        record.setProfilePicture("/images/default-profile.png");
        String hashedPassword = passwordEncoder.encode(user.getPassword());
        record.setPassword(hashedPassword);

        userRepository.save(record);
    }

}
