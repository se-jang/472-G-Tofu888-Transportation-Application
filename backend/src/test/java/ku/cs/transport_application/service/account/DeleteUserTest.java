package ku.cs.transport_application.service.account;

import ku.cs.transport_application.common.OrderStatus;
import ku.cs.transport_application.controller.UserController;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DeleteUserTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private UUID userId;
    private User mockUser;

    @Before
    public void setUp() {
        userId = UUID.randomUUID();
        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setOrders(Collections.emptyList());
    }

    /**
     * ✅ Acceptance criteria 1. สามารถลบบัญชีผู้ใช้งานที่ไม่มีการใช้งาน
     */
    @Test
    public void testDeleteInactiveUser_Success() {
        when(userService.findById(userId)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(mockUser);
    }

    /**
     * ✅ Acceptance criteria 2. ข้อมูลของผู้ใช้จะถูกลบจากฐานข้อมูลจริง ๆ หลังจากการดำเนินการ
     */
    @Test
    public void testUserIsDeletedFromDatabase() {
        when(userService.findById(userId)).thenReturn(mockUser);

        userController.deleteUser(userId);

        verify(userService, times(1)).deleteUser(mockUser);
    }

    /**
     * ✅ Acceptance criteria 3. การลบผู้ใช้จะสามารถทำได้หากผู้ใช้มีสถานะออเดอร์เป็น COMPLETED หรือ UNPAID เท่านั้น
     */
    @Test
    public void testDeleteUserWithValidOrderStatus() {
        Order validOrder = new Order();
        validOrder.setStatus(OrderStatus.COMPLETED);
        mockUser.setOrders(List.of(validOrder));

        when(userService.findById(userId)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userService, times(1)).deleteUser(mockUser);
    }

    @Test
    public void testDeleteUserWithInvalidOrderStatus() {
        Order invalidOrder = new Order();
        invalidOrder.setStatus(OrderStatus.ONGOING);
        mockUser.setOrders(List.of(invalidOrder));

        when(userService.findById(userId)).thenReturn(mockUser);

        ResponseEntity<?> response = userController.deleteUser(userId);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertEquals("User cannot be deleted due to invalid orders status", response.getBody());
        verify(userService, never()).deleteUser(mockUser);
    }
}
