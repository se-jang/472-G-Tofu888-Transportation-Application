package ku.cs.transport_application.controller;

import jakarta.validation.Valid;
import ku.cs.transport_application.request.OrderRequest;
import ku.cs.transport_application.service.CreateOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CreateOrderController {

    @Autowired
    private CreateOrderService createOrderService;

    @PostMapping("/create-order")
    public ResponseEntity<?> createOrder(@Valid @RequestBody OrderRequest request) throws Exception {
        createOrderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created successfully");
    }
}
