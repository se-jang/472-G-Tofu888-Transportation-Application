package ku.cs.transport_application.service;

import ku.cs.transport_application.common.TransportationWorkerStatus;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.repository.TransportationWorkerRepository;
import ku.cs.transport_application.request.CreateUserRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CreateWorkerService {

    @Autowired
    private TransportationWorkerRepository transportationWorkerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public boolean isUsernameWorkerAvailable(String username) {
        return transportationWorkerRepository.findByUsername(username) == null;
    }

    public void createWorker(CreateUserRequest user) {
        TransportationWorker worker = new TransportationWorker();

        worker.setUsername(user.getUsername());
        worker.setName(user.getName());

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        worker.setPassword(hashedPassword);

        worker.setPhoneNumber(user.getPhoneNumber());
        worker.setEmail(user.getEmail());
        worker.setProfilePicture("/images/default-profile.png");
        worker.setStatus(TransportationWorkerStatus.AVAILABLE);

        transportationWorkerRepository.save(worker);
    }
}
