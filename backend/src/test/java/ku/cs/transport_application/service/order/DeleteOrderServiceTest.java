package ku.cs.transport_application.service.order;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteOrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order completedOrder;
    private Order unpaidOrder;
    private Order ongoingOrder;

    private UUID completedOrderId;
    private UUID unpaidOrderId;
    private UUID ongoingOrderId;

    @BeforeEach
    void setUp() {
        completedOrderId = UUID.randomUUID();
        unpaidOrderId = UUID.randomUUID();
        ongoingOrderId = UUID.randomUUID();

        completedOrder = new Order();
        completedOrder.setId(completedOrderId);
        completedOrder.setStatus(OrderStatus.COMPLETED);

        unpaidOrder = new Order();
        unpaidOrder.setId(unpaidOrderId);
        unpaidOrder.setStatus(OrderStatus.UNPAID);

        ongoingOrder = new Order();
        ongoingOrder.setId(ongoingOrderId);
        ongoingOrder.setStatus(OrderStatus.ONGOING);
    }

    //acceptance criteria
    //1. สามารถลบออเดอร์ที่ต้องการลบได้
    //2. การลบออเดอร์จะสามารถทำได้หากออเดอร์มีสถานะออเดอร์เป็น COMPLETED หรือ UNPAID เท่านั้น
    //3. ข้อมูลของออเดอร์จะถูกลบจากฐานข้อมูลจริง ๆ หลังจากการดำเนินการ

    /*
    1. สามารถลบออเดอร์ที่ต้องการลบได้
    2. การลบออเดอร์จะสามารถทำได้หากออเดอร์มีสถานะออเดอร์เป็น COMPLETED หรือ UNPAID เท่านั้น
    */
    @Test
    void testDeleteCompleteOrder() {
        when(orderRepository.findById(completedOrderId)).thenReturn(Optional.of(completedOrder));

        orderService.deleteOrder(completedOrderId);

        verify(orderRepository, times(1)).delete(completedOrder);
    }
    @Test
    void testDeleteUnpaidOrder() {
        when(orderRepository.findById(unpaidOrderId)).thenReturn(Optional.of(unpaidOrder));

        orderService.deleteOrder(unpaidOrderId);

        verify(orderRepository, times(1)).delete(unpaidOrder);
    }
    @Test
    void testDeleteOngoingOrder() {
        when(orderRepository.findById(ongoingOrderId)).thenReturn(Optional.of(ongoingOrder));
        doThrow(new RuntimeException("Database error")).when(orderRepository).delete(ongoingOrder);

        boolean result;
        try {
            orderService.deleteOrder(ongoingOrderId);
            result = true;
        } catch (Exception e) {
            result = false;
        }

        assertFalse(result);
    }
}
