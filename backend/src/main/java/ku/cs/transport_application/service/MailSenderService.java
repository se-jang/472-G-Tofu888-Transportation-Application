package ku.cs.transport_application.service;

import ku.cs.transport_application.entity.Order;
import ku.cs.transport_application.entity.User;
import ku.cs.transport_application.repository.OrderRepository;
import ku.cs.transport_application.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class MailSenderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendEmail(UUID orderId) {
        SimpleMailMessage message = new SimpleMailMessage();
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        User user = userRepository.findByName(order.getUser().getName());
        String email = user.getEmail();

        String subject = String.format("Order Update: Your Order %s is now %s", orderId, order.getStatus());
        String body = String.format("Dear %s,\n\nWe would like to inform you that your order is now %s. " +
                "If you have any questions or require further assistance, please do not hesitate to contact us.\n\n" +
                "Thank you for choosing our service!\n\nBest regards,\nTransportation Application", user.getName(), order.getStatus());

        message.setTo(email);
        message.setSubject(subject);
        message.setText(body);
        message.setFrom(fromEmail);

        mailSender.send(message);
    }
}
