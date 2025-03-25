package ku.cs.transport_application.controller;

import ku.cs.transport_application.response.ReceiptResponse;
import ku.cs.transport_application.service.ReceiptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @GetMapping("/receipt/{id}")
    public ResponseEntity<ReceiptResponse> getReceiptByOrderId(@PathVariable UUID id) {
        ReceiptResponse receiptResponse = receiptService.getReceiptByOrderId(id);

        if (receiptResponse == null)
            return ResponseEntity.internalServerError().build();

        return ResponseEntity.ok(receiptResponse);
    }
}
