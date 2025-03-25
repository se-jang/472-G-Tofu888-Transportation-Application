package ku.cs.transport_application.service.order;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.hibernate.validator.internal.util.Contracts.assertTrue;

@SpringBootTest
@Transactional
public class DeleteOrderServiceDBTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    //acceptance criteria
    //1. สามารถลบออเดอร์ที่ต้องการลบได้
    //2. การลบออเดอร์จะสามารถทำได้หากออเดอร์มีสถานะออเดอร์เป็น COMPLETED หรือ UNPAID เท่านั้น
    //3. ข้อมูลของออเดอร์จะถูกลบจากฐานข้อมูลจริง ๆ หลังจากการดำเนินการ
    //ข้อ 1, 2 อยู่ใน DeleteOrderServiceTest

    //3. ข้อมูลของออเดอร์จะถูกลบจากฐานข้อมูลจริง ๆ หลังจากการดำเนินการ
    @Test
    void testRemoveOrderFromDatabase() {
        Order order = new Order();
        order.setStatus(OrderStatus.COMPLETED);
        order = orderRepository.save(order);

        UUID orderId = order.getId();

        orderService.deleteOrder(orderId);

        Optional<Order> deletedOrder = orderRepository.findById(orderId);
        assertTrue(deletedOrder.isEmpty(), "Order should be deleted from database");
    }
}
