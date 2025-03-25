package ku.cs.transport_application.controller;

import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.request.LoginRequest;
import ku.cs.transport_application.response.JwtResponse;
import ku.cs.transport_application.service.JwtService;
import ku.cs.transport_application.service.TransportationWorkerService;
import ku.cs.transport_application.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private TransportationWorkerService transportationWorkerService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        User user = userService.findByUsername(request.getUsername());
        TransportationWorker worker = transportationWorkerService.findWorkerByUsername(request.getUsername());

        if (user != null && passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            String token = jwtService.generateToken(user.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, user.getRole().name(), user.getUsername(), user.getId()));
        }

        if (worker != null && passwordEncoder.matches(request.getPassword(), worker.getPassword())) {
            String token = jwtService.generateToken(worker.getUsername());
            return ResponseEntity.ok(new JwtResponse(token, "WORKER", worker.getUsername(), worker.getId()));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }

}
