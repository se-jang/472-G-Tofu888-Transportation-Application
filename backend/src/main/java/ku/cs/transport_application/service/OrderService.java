package ku.cs.transport_application.service;

import ku.cs.transport_application.DTO.OrderDTO;
import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.common.TransportationWorkerStatus;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.OrderLine;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.*;
import ku.cs.transport_application.request.OrderRequest;
import ku.cs.transport_application.request.ProductDetailRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static ku.cs.transport_application.common.OrderStatus.*;
import static ku.cs.transport_application.common.UserRole.*;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderLineRepository orderLineRepository;

    @Autowired
    private TransportationWorkerRepository twRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<OrderDTO> getOrdersByUser(UUID id) {
        Optional<User> recordOptional = userRepository.findById(id);
        if (recordOptional.isPresent()) {
            User record = recordOptional.get();
            if (record.getRole() == USER) {
                return orderRepository.findByUserId(record.getId()).stream()
                        .map(order -> new OrderDTO(
                                order.getId(),
                                order.getStatus(),
                                order.getCustomerName(),
                                order.getDate(),
                                order.getDeliveredDate(),
                                order.getUser().getName(),
                                order.getTotal(),
                                order.getPaymentLink()
                        ))
                        .collect(Collectors.toList());
            }
        }
        return null;
    }

    public Order getOrdersByOrderId(UUID id) {
        Optional<Order> recordOptional = orderRepository.findById(id);
        return recordOptional.orElse(null);
    }


    public List<OrderDTO> getOrdersByWorker(UUID id) {
        Optional<TransportationWorker> recordOptional = twRepository.findById(id);
        if (recordOptional.isPresent()) {
            TransportationWorker record = recordOptional.get();
            return orderRepository.findByWorkerId(record.getId()).stream()
                    .filter(order -> !order.getStatus().equals(COMPLETED))
                    .map(order -> new OrderDTO(
                            order.getId(),
                            order.getStatus(),
                            order.getCustomerName(),
                            order.getDate(),
                            order.getDeliveredDate(),
                            order.getUser().getName(),
                            order.getTotal(),
                            order.getPaymentLink()
                    ))
                    .collect(Collectors.toList());
        }
        return null;
    }

    public List<OrderDTO> getUnpaidOrder() {
        return orderRepository.findByStatus(UNPAID).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getUncheckOrder() {
        return orderRepository.findByStatus(UNCHECK).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getCheckedOrder() {
        return orderRepository.findByStatus(CHECKED).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getOnGoing() {
        return orderRepository.findByStatus(ONGOING).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getDelivered() {
        return orderRepository.findByStatus(DELIVERED).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getUploaded() {
        return orderRepository.findByStatus(UPLOADED).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getComplete() {
        return orderRepository.findByStatus(COMPLETED).stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public List<Order> getNotCompleteOrder() {
        List<Order> notUncheckOrders = orderRepository.findAll();
        return notUncheckOrders.stream()
                .filter(order -> !(order.getStatus() == COMPLETED))
                .collect(Collectors.toList());
    }

    public List<OrderDTO> getAllOrder() {
        return orderRepository.findAll().stream()
                .map(order -> new OrderDTO(
                        order.getId(),
                        order.getStatus(),
                        order.getCustomerName(),
                        order.getDate(),
                        order.getDeliveredDate(),
                        order.getUser().getName(),
                        order.getTotal(),
                        order.getPaymentLink()
                ))
                .collect(Collectors.toList());
    }

    public OrderRequest getOrderDetail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        List<OrderLine> orderLines = orderLineRepository.findByOrderId(orderId);

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setDate(order.getDate());
        orderRequest.setCustomerName(order.getCustomerName());
        orderRequest.setCustomerAddress(order.getCustomerAddress());
        if (order.getWorker() != null) {
            orderRequest.setWorkerUsername(order.getWorker().getUsername());
        } else {
            orderRequest.setWorkerUsername("N/A");
        }
        orderRequest.setStatus(order.getStatus());

        orderRequest.setDeliveredDate(order.getDeliveredDate());

        List<ProductDetailRequest> productDetails = orderLines.stream()
                .map(orderLine -> {
                    ProductDetailRequest productDetailRequest = new ProductDetailRequest();
                    productDetailRequest.setId(orderLine.getProduct().getId());
                    productDetailRequest.setProductName(orderLine.getProduct().getName());
                    productDetailRequest.setProductType(orderLine.getProduct().getType());
                    productDetailRequest.setQuantity(orderLine.getQuantity());
                    return productDetailRequest;
                })
                .collect(Collectors.toList());

        orderRequest.setProductDetails(productDetails);

        return orderRequest;
    }

    public void upDateOrderStatus(UUID id, OrderStatus status) {
        Order record = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        record.setStatus(status);
        orderRepository.save(record);

        if (status.equals(OrderStatus.COMPLETED)) {
            updateWorkerStatus(record.getWorker().getId());
        }
    }

    private void updateWorkerStatus(UUID workerId) {
        List<OrderDTO> orders = getOrdersByWorker(workerId);
        boolean allComplete = orders.stream().allMatch(order -> order.getStatus().equals(OrderStatus.COMPLETED));

        if (allComplete) {
            TransportationWorker worker = twRepository.findById(workerId)
                    .orElseThrow(() -> new RuntimeException("Worker not found"));
            worker.setStatus(TransportationWorkerStatus.AVAILABLE);
            twRepository.save(worker);
        }
    }

    public void upDateOrderToWorker(UUID workerId, UUID orderId) {
        Optional<TransportationWorker> recordWorkerOptional = twRepository.findById(workerId);
        Optional<Order> recordOrderOptional = orderRepository.findById(orderId);

        if (recordWorkerOptional.isPresent() && recordOrderOptional.isPresent()) {
            TransportationWorker recordWorker = recordWorkerOptional.get();
            Order recordOrder = recordOrderOptional.get();

            recordOrder.setWorker(recordWorker);
            orderRepository.save(recordOrder);
        }
    }

    public Optional<Order> getByOrderId(UUID orderId) {
        return orderRepository.findById(orderId);
    }

    public void deleteOrder(UUID orderId) {
        //หา order จากไอดี และแปลง Optional<Order> -> Order
        Optional<Order> order = orderRepository.findById(orderId);
        //ลบ order
        orderRepository.delete(order.get());
    }

    public boolean checkOrderCanDelete(UUID orderId) {
        //หา order จากไอดี และแปลง Optional<Order> -> Order
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        Order order = optionalOrder.get();
        // คืนค่า true หากออเดอร์ COMPLETED หรือ UNPAID นอกจากนั้น false
        return order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.UNPAID;
    }
}