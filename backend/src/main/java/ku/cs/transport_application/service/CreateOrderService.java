package ku.cs.transport_application.service;

import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.entity.*;
import ku.cs.transport_application.repository.OrderLineRepository;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.ProductRepository;
import ku.cs.transport_application.repository.UserRepository;
import ku.cs.transport_application.request.OrderRequest;
import ku.cs.transport_application.request.ProductDetailRequest;
import ku.cs.transport_application.service.payment.CalculatePriceService;
import ku.cs.transport_application.service.payment.PaymentFactory;
import ku.cs.transport_application.service.payment.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateOrderService {
    private final OrderRepository orderRepository;
    private final OrderLineRepository orderLineRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PaymentFactory paymentFactory;
    private final ReceiptService receiptService;

    public void createOrder(OrderRequest request) throws Exception {
        Order order = new Order();
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setStatus(OrderStatus.UNPAID);
        order.setDate(LocalDateTime.now());
        order.setUser(userRepository.findByUsername(request.getUsername()));
        order.setTotal(0); // ยังไม่ได้คำนวณราคาสินค้า

        // บันทึก Order ก่อน ไม่งั้น order line จะ generate order id มั่ว
        orderRepository.save(order);

        int totalShippingCost = 0;

        // สร้าง OrderLine และบันทึกทีละตัว
        for (ProductDetailRequest productDetail : request.getProductDetails()) {
            Product product = new Product();
            product.setName(productDetail.getProductName());
            product.setType(productDetail.getProductType());
            productRepository.save(product);

            OrderLine orderLine = new OrderLine();
            orderLine.setOrder(order);
            orderLine.setProduct(product);
            orderLine.setQuantity(productDetail.getQuantity());

            OrderLineKey orderLineKey = new OrderLineKey();
            orderLineKey.setOrderId(order.getId());
            orderLineKey.setProductId(product.getId());
            orderLine.setId(orderLineKey);

            CalculatePriceService priceService = new CalculatePriceService();
            int shippingCost = priceService.calculateShipping(product.getType(), productDetail.getQuantity());
            totalShippingCost += shippingCost;

            // บันทึก OrderLine
            orderLineRepository.save(orderLine);
        }

        // คำนวณราคาทั้งหมด (รวมราคาสินค้าและค่าขนส่ง)
        order.setTotal(totalShippingCost);

        // สร้างลิงก์การชำระเงิน
        PaymentService paymentService = paymentFactory.getPaymentService(request.getPaymentMethod());
        PaymentResponse response = paymentService.createPaymentLink(order);
        order.setPaymentLink(response.getPaymentLink());

        // อัพเดตข้อมูล order ที่มี payment link
        orderRepository.save(order);

        //สร้างใบเสร็จหลังจากชำระเงินแล้ว
        receiptService.createReceipt(order.getId());
    }
}