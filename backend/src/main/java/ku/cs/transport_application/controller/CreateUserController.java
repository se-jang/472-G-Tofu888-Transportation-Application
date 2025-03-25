package ku.cs.transport_application.controller;

import jakarta.validation.Valid;
import ku.cs.transport_application.common.UserRole;
import ku.cs.transport_application.request.CreateUserRequest;
import ku.cs.transport_application.service.CreateUserService;
import ku.cs.transport_application.service.CreateWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CreateUserController {

    @Autowired
    private CreateUserService createUserService;

    @Autowired
    private CreateWorkerService createWorkerService;

    @PostMapping("/create-user")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserRequest user) {
        boolean isUsernameAvailable = createWorkerService.isUsernameWorkerAvailable(user.getUsername())
                && createUserService.isUsernameUserAvailable(user.getUsername());

        if (!isUsernameAvailable) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username not available");
        }

        if (user.getRole() == UserRole.WORKER) {
            createWorkerService.createWorker(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("Worker created successfully");
        } else if (user.getRole() == UserRole.USER) {
            createUserService.createUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid user role");
    }

}
