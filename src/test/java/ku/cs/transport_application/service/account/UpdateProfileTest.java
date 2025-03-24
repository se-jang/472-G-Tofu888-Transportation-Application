package ku.cs.transport_application.service.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import ku.cs.transport_application.controller.UserController;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.request.EditProfileRequest;
import ku.cs.transport_application.service.FileService;
import ku.cs.transport_application.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateProfileTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    private User existingUser;
    private EditProfileRequest editProfileRequest;
    private MockMultipartFile profilePicture;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();

        existingUser = new User();
        existingUser.setId(UUID.randomUUID());
        existingUser.setName("Old Name");
        existingUser.setEmail("oldemail@example.com");
        existingUser.setPhoneNumber("0987654321");

        existingUser.setRole(ku.cs.transport_application.common.UserRole.USER);

        editProfileRequest = new EditProfileRequest();
        editProfileRequest.setName("New Name");
        editProfileRequest.setEmail("newemail@example.com");
        editProfileRequest.setPhoneNumber("0123456789");

        profilePicture = new MockMultipartFile(
                "profilePicture",
                "default-profile.png",
                "image/png",
                "image content".getBytes());
    }

    /**
     * ✅ Acceptance criteria 1. ผู้ใช้สามารถแก้ไขข้อมูลโปรไฟล์ได้
     */
    @Test
    public void testUserCanEditProfile() throws Exception {
        String editProfileJson = objectMapper.writeValueAsString(editProfileRequest);
        BindingResult bindingResult = new BeanPropertyBindingResult(editProfileRequest, "editProfileRequest");

        when(userService.findById(existingUser.getId())).thenReturn(existingUser);

        doNothing().when(fileService).uploadProfilePicture(eq(existingUser.getId()), eq(profilePicture), any());

        ResponseEntity<?> responseEntity = userController.updateUserProfile(
                existingUser.getId(),
                editProfileJson,
                profilePicture,
                bindingResult);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        User updatedUser = (User) responseEntity.getBody();
        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        assertEquals("newemail@example.com", updatedUser.getEmail());
        assertEquals("0123456789", updatedUser.getPhoneNumber());

        verify(userService, times(1)).setUser(existingUser);
    }

    /**
     * ✅ Acceptance criteria 2. มีการตรวจสอบข้อมูลใหม่ว่าถูกต้องตามหลักที่เคย set ไว้ก่อนสมัครสมาชิกหรือปล่าว
     */
    @Test
    public void testProfileUpdateValidation() throws Exception {
        String editProfileJson = objectMapper.writeValueAsString(editProfileRequest);
        BindingResult bindingResult = new BeanPropertyBindingResult(editProfileRequest, "editProfileRequest");

        FieldError nameError = new FieldError("editProfileRequest", "name", "Name can only contain letters and spaces");
        FieldError emailError = new FieldError("editProfileRequest", "email", "Invalid email format");
        FieldError phoneError = new FieldError("editProfileRequest", "phoneNumber", "Phone number must be exactly 10 digits");
        bindingResult.addError(nameError);
        bindingResult.addError(emailError);
        bindingResult.addError(phoneError);

        ResponseEntity<?> response = userController.updateUserProfile(
                existingUser.getId(),
                editProfileJson,
                profilePicture,
                bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        String responseBody = response.getBody().toString();
        assertTrue(responseBody.contains("Name can only contain letters and spaces"));
        assertTrue(responseBody.contains("Invalid email format"));
        assertTrue(responseBody.contains("Phone number must be exactly 10 digits"));

        verify(userService, never()).setUser(any(User.class));
    }

    /**
     * ✅ Acceptance criteria 3. ระบบสามารถแสดงข้อมูลโปรไฟล์ได้อย่างถูกต้อง หลังจากทำการแก้ไขข้อมูลโปรไฟล์แล้ว
     */
    @Test
    public void testProfileDisplayAfterUpdate() throws Exception {
        String editProfileJson = objectMapper.writeValueAsString(editProfileRequest);
        BindingResult bindingResult = new BeanPropertyBindingResult(editProfileRequest, "editProfileRequest");

        when(userService.findById(existingUser.getId())).thenReturn(existingUser);
        doNothing().when(fileService).uploadProfilePicture(eq(existingUser.getId()), eq(profilePicture), any());

        userController.updateUserProfile(existingUser.getId(), editProfileJson, profilePicture, bindingResult);

        verify(userService, times(1)).setUser(argThat(user ->
                "New Name".equals(user.getName()) &&
                        "newemail@example.com".equals(user.getEmail()) &&
                        "0123456789".equals(user.getPhoneNumber())
        ));
    }
}
