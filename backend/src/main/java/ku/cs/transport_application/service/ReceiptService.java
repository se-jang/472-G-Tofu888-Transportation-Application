package ku.cs.transport_application.service;

import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.Receipt;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.ReceiptRepository;
import ku.cs.transport_application.response.ProductResponse;
import ku.cs.transport_application.response.ReceiptResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ReceiptService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private OrderService orderService;

    public void createReceipt(UUID orderId) throws Exception {
        Receipt receipt = new Receipt();
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            receipt.setOrder(order);
            receipt.setCreateAt(LocalDateTime.now());
            receiptRepository.save(receipt);
        } else {
            throw new Exception("Order not found.");
        }
    }

    public void newReceipt(UUID orderId) throws Exception {
        Receipt receipt = receiptRepository.findByOrderId(orderId);
        Optional<Order> orderOptional = orderRepository.findById(orderId);

        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            receipt.setOrder(order);
            receipt.setCreateAt(LocalDateTime.now());
            receiptRepository.save(receipt);
        } else {
            throw new Exception("Order not found.");
        }
    }

    public ReceiptResponse getReceiptByOrderId(UUID id) {
        Order order = orderService.getOrdersByOrderId(id);

        // Map ข้อมูลจาก OrderLine ไปยัง ProductResponse
        List<ProductResponse> productResponses = order.getOrderLines().stream()
                .map(orderLine -> new ProductResponse(
                        orderLine.getProduct().getId(),          // Product ID
                        orderLine.getProduct().getName(),        // Product Name
                        orderLine.getProduct().getType(),        // Product Type
                        orderLine.getQuantity()                  // Quantity จาก OrderLine
                ))
                .collect(Collectors.toList());

        return new ReceiptResponse(
                order.getId(),
                order.getDate(),                        // วันที่สร้างออเดอร์
                order.getStatus(),                      // สถานะสินค้า
                order.getCustomerName(),                // ชื่อร้านค้า
                order.getCustomerAddress(),             // ที่อยู่ผู้รับ
                order.getWorker().getName()  ,          // ชื่อผู้ส่ง
                order.getWorker().getPhoneNumber() ,    // เบอร์ผู้ส่ง
                order.getWorker().getEmail(),           // เมลผู้ส่ง
                order.getUser().getName(),              // ชื่อผู้รับ
                productResponses                        // list of product
        );
    }

}
