package ku.cs.transport_application.service.payment;

import com.stripe.exception.StripeException;
import ku.cs.transport_application.DTO.PaymentResponse;
import ku.cs.transport_application.entity.Order;

public interface PaymentService {
    PaymentResponse createPaymentLink(Order order) throws StripeException;
}
