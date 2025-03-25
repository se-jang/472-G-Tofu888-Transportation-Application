package ku.cs.transport_application.controller;


import ku.cs.transport_application.DTO.TransportationWorkerDTO;
import ku.cs.transport_application.common.UserRole;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.service.FileService;
import ku.cs.transport_application.service.OrderService;
import ku.cs.transport_application.service.TransportationWorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/transportation-workers")
public class TransportationWorkerController {

    @Autowired
    private TransportationWorkerService transportationWorkerService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private FileService fileService;

    @GetMapping
    public ResponseEntity<List<TransportationWorkerDTO>> getTransportationWorkers() {
        List<TransportationWorkerDTO> workerDTO = transportationWorkerService.getAvailableTransportationWorker();
        return ResponseEntity.ok(workerDTO);
    }

    @GetMapping("/{workerId}")
    public ResponseEntity<TransportationWorkerDTO> getTransportationWorkerDetails(@PathVariable UUID workerId) {
        TransportationWorkerDTO workerDTO = transportationWorkerService.findWorkerByIdWithDTO(workerId);
        return ResponseEntity.ok(workerDTO);
    }

    @GetMapping("/{workerId}/orders")
    public ResponseEntity<?> getTransportationWorkerOrdersDetails(@PathVariable UUID workerId) {
        return ResponseEntity.ok(orderService.getOrdersByWorker(workerId));
    }

    @PostMapping("/worker/worker-detail/{workerId}/add-order")
    public ResponseEntity<?> assignWorkerToOrder(@RequestParam("orderId") UUID orderId, @PathVariable("workerId") UUID workerId) {
        try {
            orderService.upDateOrderToWorker(workerId, orderId);
            return new ResponseEntity<>(Map.of("message", "Worker assigned to order successfully"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("error", "Failed to assign worker to order"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update-profile/{workerId}")
    public ResponseEntity<?> updateUserProfile(@PathVariable("workerId") UUID workerId,
                                               @RequestParam(value = "profilePicture", required = false) MultipartFile profilePicture,
                                               @RequestParam("name") String name,
                                               @RequestParam("email") String email,
                                               @RequestParam("phoneNumber") String phoneNumber) {
        TransportationWorker record = transportationWorkerService.findWorkerById(workerId);
        record.setName(name);
        record.setEmail(email);
        record.setPhoneNumber(phoneNumber);
        transportationWorkerService.setTransportationWorker(record);

        if (profilePicture != null) {
            try {
                fileService.uploadProfilePicture(workerId, profilePicture, UserRole.WORKER);
            } catch (IOException e) {
                return new ResponseEntity<>(Map.of("error", "Failed to upload profile picture"), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return ResponseEntity.ok(record);
    }
}
