package ku.cs.transport_application.service.order;

import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.common.ProductType;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.OrderLine;
import ku.cs.transport_application.entity.Product;
import ku.cs.transport_application.repository.OrderLineRepository;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.ProductRepository;
import ku.cs.transport_application.repository.ReceiptRepository;
import ku.cs.transport_application.request.OrderRequest;
import ku.cs.transport_application.request.ProductDetailRequest;
import ku.cs.transport_application.service.EditOrderService;
import ku.cs.transport_application.service.ReceiptService;
import ku.cs.transport_application.service.payment.PaymentFactory;
import ku.cs.transport_application.service.payment.PaymentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EditOrderServiceTest {

    @InjectMocks
    private EditOrderService editOrderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineRepository orderLineRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private PaymentFactory paymentFactory;

    @Mock
    private ReceiptService receiptService;

    @Mock
    private PaymentService paymentService;

    //    acceptance criteria
    //1. ผู้ใช้สามารถแก้ไขออเดอร์ได้
    //2. ผู้ใช้สามารถแก้ไข product ได้
    //3. ราคาและ Link จ่ายเงินอัพเดตเมื่อทำการแก้ไขรายการสินค้าในออเดอร์

    @Test
    void testUpdateValidOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        Order existingOrder = new Order();
        existingOrder.setId(orderId);
        existingOrder.setCustomerName("Old Name");
        existingOrder.setCustomerAddress("Old Address");
        existingOrder.setTotal(100);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(existingOrder));
        when(paymentFactory.getPaymentService(any())).thenReturn(paymentService);
        when(paymentService.createPaymentLink(any())).thenReturn(new PaymentResponse());

        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setCustomerName("New Name");
        orderRequest.setCustomerAddress("New Address");
        orderRequest.setPaymentMethod("Credit Card");

        ProductDetailRequest productDetail = new ProductDetailRequest();
        productDetail.setProductName("New Product");
        productDetail.setProductType(ProductType.HEAVYWEIGHT);
        productDetail.setQuantity(2);
        orderRequest.setProductDetails(java.util.List.of(productDetail));

        editOrderService.editOrder(orderId, orderRequest);

        verify(orderRepository, Mockito.times(2)).save(existingOrder);
        verify(productRepository, Mockito.times(1)).save(any(Product.class));
        verify(orderLineRepository, Mockito.times(1)).save(any(OrderLine.class));
        verify(paymentService, Mockito.times(1)).createPaymentLink(any(Order.class));
        verify(receiptService, Mockito.times(1)).newReceipt(orderId);
    }

    @Test
    void testEditInvalidOrder() {
        UUID orderId = UUID.randomUUID();
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            editOrderService.editOrder(orderId, new OrderRequest());
        });

        assertEquals("Order not found", exception.getMessage());
    }
}

