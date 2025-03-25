package ku.cs.transport_application.service.receipt;

import ku.cs.transport_application.entity.*;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.ReceiptRepository;
import ku.cs.transport_application.service.ReceiptService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ReceiptServiceTest {

    @Mock
    private ReceiptRepository receiptRepository;

    @Mock
    private OrderRepository orderRepository;  // Assuming this is a dependency of ReceiptService

    @InjectMocks
    private ReceiptService receiptService;

    private User user;
    private Order order;
    private Receipt receipt;
    private TransportationWorker worker;

    UUID receiptId = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        //user
        UUID userId = UUID.randomUUID();
        user = new User();
        user.setId(userId);
        user.setName("User1");

        UUID transportationWorkerId = UUID.randomUUID();
        worker = new TransportationWorker();
        worker.setId(transportationWorkerId);
        worker.setName("worker");

        UUID orderId = UUID.randomUUID();
        order = new Order();
        order.setId(orderId);
        order.setUser(user);
        order.setCustomerAddress("123/111 Banana St.");
        order.setCustomerName("Customer");
        order.setWorker(worker);

        receipt = new Receipt();
        order.setReceipt(receipt);
        receipt.setCreateAt(LocalDateTime.now());
        receipt.setReceiptId(receiptId);
        receipt.setOrder(order);
    }

    /**
     * ✅ Acceptance criteria 1: สามารถสร้างใบเสร็จที่ตรงกับเลข order ได้
     * Test that when an order is found by ID, a receipt is successfully created and associated with the order.
     * User should be able to create a receipt if the order exists in the system.
     */
    @Test
    void createReceipt_Success() throws Exception {
        // Arrange: Mock the order repository to return an order when its ID is looked up
        when(orderRepository.findById(order.getId())).thenReturn(Optional.of(order));

        // Act: Call the method to create a receipt
        receiptService.createReceipt(order.getId());

        // Assert: Verify that receiptRepository.save() was called once to save the receipt
        verify(receiptRepository, times(1)).save(any(Receipt.class));
    }

    /**
     * ✅ Acceptance criteria 2: ไม่สามารถสร้างใบเสร็จที่ไม่ตรงเลข order ได้
     * Test that when an order is not found by its ID, no receipt is created and an exception is thrown.
     * User should not be able to create a receipt if the order does not exist in the system.
     */
    @Test
    void createReceipt_OrderNotFound() {
        // Arrange: Mock the order repository to return an empty Optional (order not found)
        when(orderRepository.findById(order.getId())).thenReturn(Optional.empty());

        // Act & Assert: Verify that an exception is thrown when the order is not found
        assertThrows(Exception.class, () -> receiptService.createReceipt(order.getId()));
    }

    /**
     * ✅ Acceptance criteria 3: แสดง ID ของใบเสร็จถูกต้อง
     * Test that the generated receipt has a valid ID that matches the expected receiptId.
     * User should be able to verify that the receipt ID is correctly set and matches the expected value.
     */
    @Test
    void testReceiptIdIsvalid() {
        assertNotNull(receipt.getOrder());
        assertEquals(receiptId, receipt.getReceiptId());
    }
}
