package ku.cs.transport_application.service;

import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.entity.*;
import ku.cs.transport_application.repository.*;
import ku.cs.transport_application.request.OrderRequest;
import ku.cs.transport_application.request.ProductDetailRequest;
import ku.cs.transport_application.service.payment.CalculatePriceService;
import ku.cs.transport_application.service.payment.PaymentFactory;
import ku.cs.transport_application.service.payment.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class EditOrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderLineRepository orderLineRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ReceiptRepository receiptRepository;
    @Autowired
    private PaymentFactory paymentFactory;
    @Autowired
    private ReceiptService receiptService;

    @Transactional
    public void editOrder(UUID orderId, OrderRequest request) throws Exception {
        //โหลด Order เดิม
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        //ลบ OrderLine เก่าทั้งหมด
        orderLineRepository.deleteByOrderId(orderId);

        //ลบ Product ที่เกี่ยวข้องทั้งหมด
        productRepository.deleteByOrderId(orderId);

        //อัปเดตข้อมูล Order
        order.setCustomerName(request.getCustomerName());
        order.setCustomerAddress(request.getCustomerAddress());
        order.setTotal(0); // Reset total price

        //บันทึก Order
        orderRepository.save(order);

        int totalShippingCost = 0;

        //ใส่สินค้าใหม่ทั้งหมด
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

            orderLineRepository.save(orderLine);
        }

        //คำนวณราคาทั้งหมด(รวมค่าขนส่ง)
        order.setTotal(totalShippingCost);

        //สร้างลิงก์ชำระเงินใหม่
        PaymentService paymentService = paymentFactory.getPaymentService(request.getPaymentMethod());
        PaymentResponse response = paymentService.createPaymentLink(order);
        order.setPaymentLink(response.getPaymentLink());

        //บันทึก Order
        orderRepository.save(order);
        //สร้างใบเสร็จใหม่
        receiptService.newReceipt(orderId);
    }
}
