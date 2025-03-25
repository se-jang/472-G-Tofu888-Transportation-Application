package ku.cs.transport_application.service;

import ku.cs.transport_application.common.UserRole;
import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.TransportationWorker;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.TransportationWorkerRepository;
import ku.cs.transport_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileService {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    TransportationWorkerRepository transportationWorkerRepository;

    public void uploadFile(UUID orderID, MultipartFile file) throws IOException {
        String uploadDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "uploads";
        String fileName = file.getOriginalFilename();
        assert fileName != null;

        Files.createDirectories(Paths.get(uploadDir));

        fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        File uploadedFile = new File(uploadDir + fileName);
        Path path = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        Order order = orderRepository.findById(orderID)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        order.setShipmentDocDir(String.valueOf(path));
        orderRepository.save(order);
    }

    public Resource getShipmentDoc(UUID orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));

        String dir = order.getShipmentDocDir();
        if (dir == null || dir.isEmpty()) {
            throw new IllegalArgumentException("Shipment document not found for this order");
        }

        try {
            Path filePath = Paths.get(dir).toAbsolutePath().normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                return resource;
            } else {
                throw new IllegalArgumentException("File not found at path: " + dir);
            }
        } catch (MalformedURLException ex) {
            throw new RuntimeException("Error retrieving file: " + dir, ex);
        }
    }

    public void uploadProfilePicture(UUID id, MultipartFile file, UserRole role) throws IOException {
        String uploadDir;
        String newFileName;
        String entityName;

        switch (role) {
            case WORKER:
                uploadDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "workers";
                entityName = "workers";
                break;
            case ADMIN:
            case USER:
            default:
                uploadDir = "src" + File.separator + "main" + File.separator + "resources" + File.separator + "static" + File.separator + "images" + File.separator + "users";
                entityName = "users";
                break;
        }

        Files.createDirectories(Paths.get(uploadDir));

        String fileName = file.getOriginalFilename();
        assert fileName != null;

        String fileExtension = fileName.substring(fileName.lastIndexOf("."));
        newFileName = UUID.randomUUID() + fileExtension;

        Path path = Paths.get(uploadDir, newFileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        String fileUrl = "/images/" + entityName + "/" + newFileName;

        if (role == UserRole.WORKER) {
            TransportationWorker worker = transportationWorkerRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Worker not found"));

            if (worker.getProfilePicture() != null && !worker.getProfilePicture().equals("/images/default-profile.png")) {
                deleteFileIfExists("src/main/resources/static" + worker.getProfilePicture());
            }

            worker.setProfilePicture(fileUrl);
            transportationWorkerRepository.save(worker);
        } else {
            User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            if (user.getProfilePicture() != null && !user.getProfilePicture().equals("/images/default-profile.png")) {
                deleteFileIfExists("src/main/resources/static" + user.getProfilePicture());
            }

            user.setProfilePicture(fileUrl);
            userRepository.save(user);
        }
    }

    private void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.isFile()) {
            if (!file.delete()) {
                System.err.println("Warning: Failed to delete file " + filePath);
            }
        }
    }

}
