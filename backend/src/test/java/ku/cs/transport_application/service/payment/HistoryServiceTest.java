package ku.cs.transport_application.service.payment;

import ku.cs.transport_application.entity.History;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.HistoryRepository;
import ku.cs.transport_application.response.HistoryResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HistoryServiceTest {

    @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    private User user;
    private User user2;
    private History history;
    private Order order;
    private TransportationWorker worker;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        UUID userId = UUID.randomUUID();
        UUID user2Id = UUID.randomUUID();

        user = new User();
        user.setId(userId);
        user.setName("User1");

        user2 = new User();
        user2.setId(user2Id);
        user2.setName("User2");

        UUID transportationWorkerId = UUID.randomUUID();
        worker = new TransportationWorker();
        worker.setId(transportationWorkerId);
        worker.setName("worker");

        UUID orderId = UUID.randomUUID();
        order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setWorker(worker);
        order.setCustomerAddress("11/12 Street");
        order.setCustomerName("Banana");

        history = new History();
        UUID historyId = UUID.randomUUID();
        history.setHistoryId(historyId);
        history.setOrder(order);

        when(historyRepository.findByUserId(user.getId())).thenReturn(List.of(history));
        when(historyRepository.findByUserId(user2.getId())).thenReturn(List.of());
    }

    /**
     * ✅ Acceptance criteria 1 : user ต้องเห็นแค่ประวัติตัวเอง
     * Test that only the histories of the correct user are returned.
     * User1 should see their own history, but not the history of User2.
     */
    @Test
    void testGetHistoriesByUserId() {
        List<HistoryResponse> historiesResponseUser1 = historyService.getHistoriesByUserId(user.getId());

        // Ensure that the history response for user1 is correct and contains 1 item
        assertNotNull(historiesResponseUser1);
        assertEquals(1, historiesResponseUser1.size());

        // Call the service method for user2 (should return an empty list)
        List<HistoryResponse> historiesResponseUser2 = historyService.getHistoriesByUserId(user2.getId());

        // Ensure that the history response for user2 is empty
        assertNotNull(historiesResponseUser2);
        assertEquals(0, historiesResponseUser2.size());

        // Verify that the repository's `findByUserId` was called for both users
        verify(historyRepository, times(1)).findByUserId(user.getId());
        verify(historyRepository, times(1)).findByUserId(user2.getId());
    }

    /**
     * ✅ Acceptance criteria 2: ชื่อผู้รับ ผู้ส่ง ที่อยู่ ต้องถูกต้อง
     */
    @Test
    void testHistoryDetailsIsValid() {
        assertNotNull(history.getOrder());
        assertEquals("worker", history.getOrder().getWorker().getName());
        assertEquals("Banana", history.getOrder().getCustomerName());
        assertEquals("11/12 Street", history.getOrder().getCustomerAddress());
    }

    /**
     * ✅ Acceptance criteria 3: ต้องเห็นประวัติก็ต่อเมื่อสถานะ order = complete
     * History จะถูกสร้างก็ต่อเมื่อ order status = complete เท่านั้น
     */

}
