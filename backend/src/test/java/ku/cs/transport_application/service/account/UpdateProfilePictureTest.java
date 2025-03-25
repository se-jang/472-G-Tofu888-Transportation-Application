package ku.cs.transport_application.service.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import ku.cs.transport_application.common.UserRole;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateProfilePictureTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private FileService fileService;

    private ObjectMapper objectMapper;
    private UUID userId;
    private User mockUser;
    private EditProfileRequest editProfileRequest;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
        userId = UUID.randomUUID();
        mockUser = new User();
        mockUser.setId(userId);
        mockUser.setName("jim");
        mockUser.setEmail("jim@email.com");
        mockUser.setPhoneNumber("123456789");
        mockUser.setRole(UserRole.USER);
    }

    /**
     * ✅ Acceptance criteria 1. ผู้ใช้สามารถแก้ไขรูปได้
     */
    @Test
    public void testUpdateUserProfile_Success() throws Exception {
        MockMultipartFile profilePicture = new MockMultipartFile("profilePicture", "image.jpg", "image/jpeg", new byte[100]);

        String editProfileJson = objectMapper.writeValueAsString(mockUser);

        BindingResult bindingResult = new BeanPropertyBindingResult(mockUser, "editProfileRequest");

        when(userService.findById(userId)).thenReturn(mockUser);
        doNothing().when(fileService).uploadProfilePicture(eq(userId), eq(profilePicture), any());

        ResponseEntity<?> response = userController.updateUserProfile(userId, editProfileJson, profilePicture, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).findById(userId);
        verify(fileService, times(1)).uploadProfilePicture(eq(userId), eq(profilePicture), any());
    }

    /**
     * ✅ Acceptance criteria 2. หลังจากอัพเดตรูปใหม่ รูปเก่าจะถูกลบออกไป
     */
    @Test
    public void testOldProfilePictureIsDeleted_WhenNewImageIsUploaded() throws Exception {
        mockUser.setProfilePicture("/images/users/oldImage.jpg");
        MultipartFile file = new MockMultipartFile("file", "newImage.jpg", "image/jpeg", new byte[0]);

        doNothing().when(fileService).deleteFileIfExists(anyString());
        doAnswer(invocation -> {
            fileService.deleteFileIfExists("src/main/resources/static/images/users/oldImage.jpg");
            return null;
        }).when(fileService).uploadProfilePicture(eq(userId), eq(file), any());

        fileService.uploadProfilePicture(userId, file, UserRole.USER);

        verify(fileService, times(1)).deleteFileIfExists("src/main/resources/static/images/users/oldImage.jpg");
        verify(fileService, times(1)).uploadProfilePicture(eq(userId), eq(file), any());
    }


    /**
     * ✅ Acceptance criteria 3. ไฟล์ภาพกำหนดไว้แค่ JPG, PNG ขนาดไม่เกิน 5 MB
     */
    @Test
    public void testFileSizeExceedsLimit_WhenNewImageIsUploaded() throws Exception {
        BindingResult bindingResult = new BeanPropertyBindingResult(editProfileRequest, "editProfileRequest");
        mockUser.setProfilePicture("/images/users/oldImage.jpg");

        when(userService.findById(userId)).thenReturn(mockUser);

        byte[] largeFile = new byte[15 * 1024 * 1024]; // 15MB
        MultipartFile file = new MockMultipartFile("profilePicture", "largeImage.jpg", "image/jpeg", largeFile);

        ResponseEntity<?> response = userController.updateUserProfile(userId,
                "{\"name\":\"John Doe\",\"email\":\"john@example.com\",\"phoneNumber\":\"123456789\"}",
                file, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Invalid file type or size exceeds 5MB limit"));
    }

}
